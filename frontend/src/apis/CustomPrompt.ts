import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import { CustomPromptResponse } from 'types/customPrompt';
import instance from '@config/apiConfig';

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;

const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),

  put: <T>(url: string, body: object) => instance.put<ApiResponse<T>>(url, body).then(responseBody),

  post: <T>(url: string, body: object) =>
    instance.post<ApiResponse<T>>(url, body).then(responseBody),
};

export const customPrompt = {
  getCustomPrompt: (projectId: string): Promise<ApiResponse<CustomPromptResponse>> =>
    apiRequests.get<CustomPromptResponse>(`custom-prompts/${projectId}`),

  updateCustomPrompt: (
    projectId: string,
    data: { promptText: string },
  ): Promise<ApiResponse<any>> => {
    const formData = new FormData();
    formData.append('promptText', data.promptText);

    return instance.put(`/custom-prompts/${projectId}`, formData).then(responseBody);
  },
};
