import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import { instance } from '@config/apiConfig';
import { NotificationResponse } from 'types/notification';

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),

  patch: <T>(url: string, data?: object) =>
    instance.patch<ApiResponse<T>>(url, data).then(responseBody),
};

export const Notification = {
  getNotification: (projectId: string): Promise<ApiResponse<NotificationResponse[]>> =>
    apiRequests.get<NotificationResponse[]>(`notifications?projectId=${projectId}`),

  getUnreadNotificationCount: (projectId: string): Promise<ApiResponse<{ unreadCount: number }>> =>
    apiRequests.get<{ unreadCount: number }>(`notifications/unread-counts?projectId=${projectId}`),

  patchNotificationReadStatus: (
    notificationId: string,
  ): Promise<ApiResponse<NotificationResponse>> =>
    apiRequests.patch<NotificationResponse>(`notifications/${notificationId}`),
};
