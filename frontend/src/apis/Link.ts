import { AxiosResponse } from 'axios';
import { GitlabProjectListResponse } from 'types/gitLab';
import { ApiResponse } from 'types/api';
import instance from '@config/apiConfig';

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
    apiRequests.get<GitlabProjectListResponse>('link/search', { keyword, page, size }),

  updateRepository: (repoId: number, data: { botToken: string }): Promise<ApiResponse<any>> =>
    apiRequests.post(`link/${repoId}`, data),

  getLinkStatus: (): Promise<ApiResponse<{ isLinked: boolean }>> =>
    apiRequests.get<{ isLinked: boolean }>(`link/status`),
};
