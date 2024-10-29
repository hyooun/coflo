export interface ApiResponse<T> {
  status: 'SUCCESS' | 'ERROR';
  data?: T;
  httpStatus?: string;
  code?: string;
  message?: string;
}

export interface UpdateRepositoryRequest {
  botToken: string;
}
