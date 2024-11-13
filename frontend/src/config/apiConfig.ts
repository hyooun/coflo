import axios from 'axios';

const VITE_API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

export const instance = axios.create({
  baseURL: `${VITE_API_BASE_URL}/api`,
  timeout: 15000,
  withCredentials: true,
});

// 전역 에러 핸들러 설정
export const handleError = (error: any) => {
  // 네트워크 에러 처리
  if (!error.response) {
    console.error('네트워크 에러가 발생했습니다.');
    return Promise.reject(error);
  }

  // HTTP 상태 코드별 처리
  switch (error.response.status) {
    case 401:
      console.log('401 Unauthorized - 로그인 페이지로 이동');
      alert('로그인이 필요한 서비스입니다.');
      window.location.href = '/login';
      break;

    case 403:
      if (error.response.data?.message === 'Project ID Not Found') {
        console.error('프로젝트가 선택되지 않았습니다.');
        window.location.href = '/repository';
      } else {
        console.error('접근 권한이 없습니다.');
        window.location.href = '/error/unauthorized';
      }
      break;

    case 404:
      console.error('요청하신 리소스를 찾을 수 없습니다.');
      window.location.href = '/error/not-found';
      break;

    case 500:
      console.error('서버 에러가 발생했습니다.');
      window.location.href = '/error/server-error';
      break;

    default:
      console.error('알 수 없는 에러가 발생했습니다.');
  }

  return Promise.reject(error);
};

// axios 인터셉터 설정
instance.interceptors.response.use((response) => response, handleError);
