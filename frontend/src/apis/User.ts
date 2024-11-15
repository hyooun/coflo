import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import { instance } from '@config/apiConfig';

interface GitlabAccountRequest {
  domain: string;
  userToken: string;
}

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  post: <T>(url: string, body: object) =>
    instance.post<ApiResponse<T>>(url, body).then(responseBody),

  patch: <T>(url: string) => instance.patch<ApiResponse<T>>(url).then(responseBody),
};

export const User = {
  addGitlabAccount: (data: GitlabAccountRequest): Promise<ApiResponse<boolean>> =>
    apiRequests.post<boolean>('users/me', data),

  synchronizeUserInfo: (): Promise<ApiResponse<boolean>> =>
    apiRequests.patch<boolean>('users/me/sync'),

  logout: (): Promise<ApiResponse<boolean>> => apiRequests.post<boolean>('users/logout', {}),
};
