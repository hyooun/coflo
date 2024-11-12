import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import instance from '@config/apiConfig';
import { BestMergeRequest, GitlabMrListResponse, SearchParameter } from 'types/mergeRequest';

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),
};

export const MergeRequest = {
  getMrList: (
    projectId: string,
    state: string,
    gitlabSearchRequest: SearchParameter,
  ): Promise<ApiResponse<GitlabMrListResponse>> =>
    apiRequests.get<GitlabMrListResponse>('merge-requests', {
      projectId,
      state,
      ...gitlabSearchRequest,
    }),

  getBestMrList: (projectId: string): Promise<ApiResponse<BestMergeRequest[]>> =>
    apiRequests.get<BestMergeRequest[]>(`/merge-requests/best?projectId=${projectId}`),
};
