import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import { instance } from '@config/apiConfig';
import { BadgeResponse } from 'types/badge';

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),

  patch: <T>(url: string, data?: object) =>
    instance.patch<ApiResponse<T>>(url, data).then(responseBody),
};

export const Badge = {
  getBadge: (): Promise<ApiResponse<BadgeResponse>> => apiRequests.get<BadgeResponse>('badges'),

  updateMainBadge: (badgeCodeId: string): Promise<ApiResponse<void>> =>
    apiRequests.patch<void>('badges', { badgeCodeId }),
};
