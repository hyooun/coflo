import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import instance from '@config/apiConfig';
import {
  UserProjectData,
  ProjectLinkRequest,
  UserProjectResponse,
  GetLinkedStatusData,
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
  addUserProject: (
    gitlabProjectId: number,
    data: ProjectLinkRequest,
  ): Promise<ApiResponse<UserProjectData>> =>
    apiRequests.post<UserProjectData>(`user-project/${gitlabProjectId}`, data),

  deleteUserProject: (gitlabProjectId: number): Promise<ApiResponse<UserProjectData>> =>
    apiRequests.delete<UserProjectData>(`user-project/${gitlabProjectId}`),

  getUserProjects: (query: {
    currentProjectId: number;
  }): Promise<ApiResponse<UserProjectResponse[]>> =>
    apiRequests.get<UserProjectResponse[]>(`user-project`, query),

  getLinkedStatus: (): Promise<ApiResponse<GetLinkedStatusData>> =>
    apiRequests.get<GetLinkedStatusData>(`user-project/status`),
};
