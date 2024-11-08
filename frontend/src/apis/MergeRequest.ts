import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import instance from '@config/apiConfig';
import { GitlabMrListResponse, SearchParameter } from 'types/mergeRequest';
import { GitlabMergeRequest } from 'types/mergeRequest';

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),

  post: <T>(url: string, params?: object, body?: object) =>
    instance.post<ApiResponse<T>>(url, body, { params }).then(responseBody),

  // post: <T>(url: string, body: object) =>
  //   instance.post<ApiResponse<T>>(url, body).then(responseBody),
};

export const MergeRequest = {
  getMrList: (
    projectId: string,
    state: string,
    gitlabSearchRequest: SearchParameter,
  ): Promise<ApiResponse<GitlabMrListResponse>> =>
    apiRequests.post<GitlabMrListResponse>(
      'merge-requests',
      { projectId, state },
      gitlabSearchRequest,
    ),

  getBestMrList: (projectId: string): Promise<ApiResponse<GitlabMergeRequest[]>> =>
    apiRequests.get<GitlabMergeRequest[]>(`/merge-requests/best?projectId=${projectId}`),
};
