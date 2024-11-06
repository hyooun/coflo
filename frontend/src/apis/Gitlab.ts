import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import instance from '@config/apiConfig';

interface ValidateTokenRequest {
  domain: string;
  userToken: string;
}

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),

  post: <T>(url: string, body: object) =>
    instance.post<ApiResponse<T>>(url, body).then(responseBody),
};

export const Gitlab = {
  validateUserToken: (data: ValidateTokenRequest): Promise<ApiResponse<boolean>> =>
    apiRequests.post<boolean>('gitlab/user-token/validate', data),

  getGitlabProjectBranched: (gitlabProjectId: number): Promise<ApiResponse<boolean>> =>
    apiRequests.get<boolean>(`gitlab/${gitlabProjectId}/branches`),

  getGitlabProjects: (query: {
    keyword: string;
    page: number;
    size: number;
  }): Promise<ApiResponse<boolean>> => apiRequests.get<boolean>('gitlab/projects'),
};
