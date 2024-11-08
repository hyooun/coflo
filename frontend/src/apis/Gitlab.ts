import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import instance from '@config/apiConfig';
import {
  GitlabProjectListResponse,
  ValidateBotTokenRequest,
  ValidateUserTokenRequest,
} from 'types/gitLab';

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),

  post: <T>(url: string, body: object) =>
    instance.post<ApiResponse<T>>(url, body).then(responseBody),
};

export const Gitlab = {
  validateUserToken: (data: ValidateUserTokenRequest): Promise<ApiResponse<boolean>> =>
    apiRequests.post<boolean>('gitlab/user-token/validate', data),

  validateBotToken: (data: ValidateBotTokenRequest): Promise<ApiResponse<boolean>> =>
    apiRequests.post<boolean>('gitlab/bot-token/validate', data),

  getGitlabProjectBranches: (gitlabProjectId: number): Promise<ApiResponse<string[]>> =>
    apiRequests.get<string[]>(`gitlab/${gitlabProjectId}/branches`),

  getGitlabProjects: (
    keyword?: string,
    page?: number,
    size?: number,
  ): Promise<ApiResponse<GitlabProjectListResponse>> =>
    apiRequests.get<GitlabProjectListResponse>('gitlab/search', { keyword, page, size }),
};
