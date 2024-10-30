import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import instance from '@config/apiConfig';
import { GitlabMrListResponse } from 'types/mr';

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),
};

export const MergeRequest = {
  getMrList: (
    keyword: string,
    page: number,
    size: number,
  ): Promise<ApiResponse<GitlabMrListResponse>> =>
    apiRequests.get<GitlabMrListResponse>('merge-requests', { keyword, page, size }),
};
