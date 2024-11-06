import axios, { AxiosResponse } from 'axios';
import { useAtom } from 'jotai';
import { errorAtom } from '@store/error';
import { ApiResponse } from 'types/api';
import { isLoginAtom } from '@store/auth';

const instance = axios.create({
  baseURL: '/api',
  timeout: 15000,
  withCredentials: true,
});

const useErrorHandler = () => {
  const [, setError] = useAtom(errorAtom);
  const [, setIsLogin] = useAtom(isLoginAtom);

  const handleError = (error: any) => {
    let message = '서버와의 연결에 문제가 발생했습니다.';

    if (error.response) {
      const errorData = error.response.data;
      console.log(error.response.status);
      if (error.response.status === 401) {
        setIsLogin(false);
        window.location.href = '/login';
        return;
      }

      if (errorData && errorData.status === 'ERROR') {
        message = errorData.message || '알 수 없는 오류가 발생했습니다.';
      }
    }

    setError(message);
    console.log(message);
  };

  return { handleError };
};

instance.interceptors.response.use(
  (response: AxiosResponse<ApiResponse<any>>) => {
    return response;
  },
  (error) => {
    const { handleError } = useErrorHandler();
    handleError(error);
    return Promise.reject(error);
  },
);

export default instance;
