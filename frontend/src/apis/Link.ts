import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import instance from '@config/apiConfig';
import { UserProjectResponse } from 'types/project';

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),

  put: <T>(url: string, body: object) => instance.put<ApiResponse<T>>(url, body).then(responseBody),

  post: <T>(url: string, body: object) =>
    instance.post<ApiResponse<T>>(url, body).then(responseBody),

  delete: <T>(url: string) => instance.delete<ApiResponse<T>>(url).then(responseBody),
};

export const UserProject = {
  updateRepository: (
    repoId: number,
    data: { botToken?: string; branches?: string[] },
  ): Promise<ApiResponse<any>> => apiRequests.post(`user-project/${repoId}`, data),

  getLinkStatus: (): Promise<ApiResponse<{ hasLinkedProject: boolean }>> =>
    apiRequests.get<{ hasLinkedProject: boolean }>(`user-project/status`),

  getUserProjects: (query: {
    currentProjectId: number;
  }): Promise<ApiResponse<UserProjectResponse[]>> =>
    apiRequests.get<UserProjectResponse[]>(`user-project`, query),

  deleteRepository: (repoId: number): Promise<ApiResponse<any>> =>
    apiRequests.delete(`user-project/${repoId}`),
};
