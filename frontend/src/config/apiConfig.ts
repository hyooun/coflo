import axios from 'axios';

const VITE_API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

const instance = axios.create({
  baseURL: `${VITE_API_BASE_URL}/api`,
  timeout: 15000,
  withCredentials: true,
});

instance.interceptors.response.use(
  (response) => response,
  (error) => {
    console.log('Response error:', error);
    if (error.response?.status === 401) {
      console.log('401 Unauthorized - 로그인 페이지로 이동');

      alert('로그인 정보가 만료되었습니다. 로그인 페이지로 이동합니다.');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  },
);

export default instance;
