import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import instance from '@config/apiConfig';
import {
  ApiResponseMapStringBoolean,
  ApiResponseMapStringLong,
  ProjectLinkRequest,
  UserProjectResponse,
} from 'types/project';

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),

  post: <T>(url: string, body: object) =>
    instance.post<ApiResponse<T>>(url, body).then(responseBody),

  delete: <T>(url: string) => instance.delete<ApiResponse<T>>(url).then(responseBody),
};

export const UserProject = {
  getUserProject: (
    gitlabProjectId: number,
    data: ProjectLinkRequest,
  ): Promise<ApiResponse<ApiResponseMapStringLong>> =>
    apiRequests.post<ApiResponseMapStringLong>(`/api/user-project/${gitlabProjectId}`, data),

  deleteUserProject: (gitlabProjectId: number): Promise<ApiResponse<ApiResponseMapStringLong>> =>
    apiRequests.delete<ApiResponseMapStringLong>(`/api/user-project/${gitlabProjectId}`),

  getUserProjects: (query: {
    currentProjectId: number;
  }): Promise<ApiResponse<UserProjectResponse[]>> =>
    apiRequests.get<UserProjectResponse[]>(`/api/user-project`, query),

  getLinkedStatus: (): Promise<ApiResponse<ApiResponseMapStringBoolean>> =>
    apiRequests.get<ApiResponseMapStringBoolean>(`/api/user-project/status`),
};
