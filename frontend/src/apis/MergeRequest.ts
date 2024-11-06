import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import instance from '@config/apiConfig';
import { GitlabMrListResponse } from 'types/mergeRequest';
import { GitlabMergeRequest } from 'types/mergeRequest';

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),
};

export const MergeRequest = {
  getMrList: (
    keyword?: string,
    page?: string,
    size?: string,
  ): Promise<ApiResponse<GitlabMrListResponse>> =>
    apiRequests.get<GitlabMrListResponse>('merge-requests', { keyword, page, size }),

  getBestMrList: (projectId: string): Promise<ApiResponse<GitlabMergeRequest[]>> =>
    apiRequests.get<GitlabMergeRequest[]>(`/merge-requests/best?projectId=${projectId}`),
};
