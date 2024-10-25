import axios, { AxiosResponse } from 'axios';
import { GitlabProjectListResponse } from 'types/gitLab';
import { ApiResponse } from 'types/api';
import { useAtom } from 'jotai';
import { errorAtom } from '@store/error';

const instance = axios.create({
  baseURL: '/api/link',
  timeout: 15000,
  withCredentials: true,
});

const useErrorHandler = () => {
  const [, setError] = useAtom(errorAtom);

  const handleError = (error: any) => {
    let message = '서버와의 연결에 문제가 발생했습니다.';

    if (error.response) {
      const errorData = error.response.data;

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

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),

  put: <T>(url: string, body: object) => instance.put<ApiResponse<T>>(url, body).then(responseBody),

  post: <T>(url: string, body: object) =>
    instance.post<ApiResponse<T>>(url, body).then(responseBody),
};

export const Link = {
  getLinkRepository: (
    keyword: string,
    page: number,
    size: number,
  ): Promise<ApiResponse<GitlabProjectListResponse>> =>
    apiRequests.get<GitlabProjectListResponse>('search', { keyword, page, size }),

  updateRepository: (repoId: number, data: { token: string }): Promise<ApiResponse<any>> =>
    apiRequests.post(`repository/${repoId}/update`, data),
};
