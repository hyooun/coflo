import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import { instance } from '@config/apiConfig';
import { ChannelCode, WebhookChannel } from 'types/channel';

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),

  post: <T>(url: string, body: object) =>
    instance.post<ApiResponse<T>>(url, body).then(responseBody),

  patch: <T>(url: string, body: object) =>
    instance.patch<ApiResponse<T>>(url, body).then(responseBody),

  delete: <T>(url: string) => instance.delete<ApiResponse<T>>(url).then(responseBody),
};

export const Channels = {
  getWebhookChannelList: (projectId: number): Promise<ApiResponse<WebhookChannel[]>> =>
    apiRequests.get<WebhookChannel[]>(`channels/${projectId}`),

  getChannelCodeList: (): Promise<ApiResponse<ChannelCode[]>> =>
    apiRequests.get<ChannelCode[]>('channels/codes'),

  updateWebhookChannel: (
    webhookChannelId: number,
    webhookUrl: string,
  ): Promise<ApiResponse<void>> =>
    apiRequests.patch<void>(`channels/${webhookChannelId}`, { webhookUrl }),

  deleteWebhookChannel: (webhookChannelId: number): Promise<ApiResponse<void>> =>
    apiRequests.delete<void>(`channels/${webhookChannelId}`),

  testWebhookMessage: (projectId: number, channelCodeId: number): Promise<ApiResponse<void>> =>
    apiRequests.post<void>(`channels/test`, { projectId, channelCodeId }),

  addWebhookChannel: (
    projectId: number,
    channelCodeId: number,
    webhookUrl: string,
  ): Promise<ApiResponse<void>> =>
    apiRequests.post<void>(`channels`, { projectId, channelCodeId, webhookUrl }),
};
