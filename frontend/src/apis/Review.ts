import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import instance from '@config/apiConfig';
import { MergeRequest } from 'types/review';
import { Reference, ReferenceRequest } from 'types/reference';

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),

  post: <T>(url: string, data?: object) =>
    instance.post<ApiResponse<T>>(url, data).then(responseBody),
};

export const Review = {
  getCodeReviewList: (
    projectId: string,
    mergeRequestIid: string,
  ): Promise<ApiResponse<MergeRequest>> =>
    apiRequests.get<MergeRequest>(`reviews`, { mergeRequestIid, projectId }),

  getReviewRetrievals: (reviewId: string): Promise<ApiResponse<Reference[]>> =>
    apiRequests.get<Reference[]>(`reviews/${reviewId}/retrievals`),

  regenerateReview: (
    projectId: string,
    gitlabMrIid: string,
    retrievals: ReferenceRequest[],
  ): Promise<ApiResponse<any>> =>
    apiRequests.post<ApiResponse<any>>(`reviews`, {
      projectId: Number(projectId),
      gitlabMrIid: Number(gitlabMrIid),
      retrievals,
    }),
};
