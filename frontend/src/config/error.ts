import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios, { AxiosError } from 'axios';
import { useSetAtom } from 'jotai';
import { setError } from '../store/error';
import { HTTP_STATUS } from '../constants/errorCodes';
import type { ApiErrorResponse, ErrorState, RetryConfig } from '../types/api';

// 재시도 설정
const RETRY_CONFIG: RetryConfig = {
  count: 3,
  delay: 1000,
} as const;

export const useAxiosInterceptor = () => {
  const navigate = useNavigate();
  const setErrorState = useSetAtom(setError);

  // 에러 로깅 함수
  const logError = (error: ApiErrorResponse) => {
    console.error('[Error Log]', {
      code: error.code,
      message: error.message,
      errors: error.data?.errors,
      timestamp: new Date().toISOString(),
      userAgent: navigator.userAgent,
    });
  };

  // 토스트 메시지 표시 함수
  const showErrorToast = (error: ApiErrorResponse) => {
    // 구체적인 필드 에러가 있는 경우
    if (error.data?.errors?.length) {
      error.data.errors.forEach((fieldError) => {
        console.error(`${fieldError.field}: ${fieldError.message}`);
      });
    } else {
      // 일반적인 에러 메시지
      console.error(error.message);
    }
  };

  // 재시도 함수
  const retryRequest = async (error: AxiosError) => {
    if (!error.config) {
      throw error;
    }

    let retryCount = RETRY_CONFIG.count;

    while (retryCount > 0) {
      try {
        await new Promise((resolve) => setTimeout(resolve, RETRY_CONFIG.delay));
        return await axios(error.config);
      } catch (retryError) {
        retryCount--;
        if (retryCount === 0) throw retryError;
      }
    }
  };

  useEffect(() => {
    const interceptor = axios.interceptors.response.use(
      (response) => response,
      async (error: AxiosError<ApiErrorResponse>) => {
        // 오프라인 상태 체크
        if (!navigator.onLine) {
          const offlineError: ErrorState = {
            status: 'ERROR',
            code: 'OFFLINE',
            message: '인터넷 연결을 확인해주세요',
            data: undefined,
          };
          setErrorState(offlineError);
          showErrorToast(offlineError);
          return Promise.reject(offlineError);
        }

        const errorResponse = error.response?.data;
        const status = error.response?.status;

        if (!errorResponse) {
          const unknownError: ErrorState = {
            status: 'ERROR',
            code: 'UNKNOWN',
            message: '알 수 없는 오류가 발생했습니다',
            data: undefined,
          };
          setErrorState(unknownError);
          showErrorToast(unknownError);
          return Promise.reject(error);
        }

        // 에러 상태 저장 및 로깅
        setErrorState({
          status: errorResponse.status,
          code: errorResponse.code,
          message: errorResponse.message,
          data: errorResponse.data,
        });
        logError(errorResponse);
        showErrorToast(errorResponse);

        // HTTP 상태 코드별 처리
        switch (status) {
          case HTTP_STATUS.UNAUTHORIZED: // 401
            navigate('/login', { state: { from: window.location.pathname } });
            break;

          case HTTP_STATUS.NOT_FOUND: // 404
            // B001은 컴포넌트에서 직접 처리
            if (errorResponse.code === 'B001') {
              return Promise.reject(error);
            }
            navigate('/error/not-found');
            break;

          case HTTP_STATUS.INTERNAL_SERVER_ERROR: // 500
            try {
              return await retryRequest(error);
            } catch {
              navigate('/error/server-error');
            }
            break;

          default:
            // 특정 에러 코드별 추가 처리
            if (errorResponse.code === 'Z009' && errorResponse.data?.errors) {
              return Promise.reject(error); // 폼 검증 에러는 컴포넌트에서 처리
            }
            navigate('/error/bad-request');
            break;
        }

        return Promise.reject(error);
      },
    );

    return () => {
      axios.interceptors.response.eject(interceptor);
    };
  }, [navigate, setErrorState]);
};
