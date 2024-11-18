export interface ApiResponse<T> {
  status: 'SUCCESS' | 'ERROR';
  data?: T;
  code?: string;
  message?: string;
}

export interface ApiError {
  field?: string;
  message: string;
}

export interface ApiErrorResponse {
  status: 'ERROR';
  data?: {
    errors?: ApiError[];
  };
  code: string;
  message: string;
}

// 재시도 설정 타입
export interface RetryConfig {
  count: number;
  delay: number;
}

export interface UpdateRepositoryRequest {
  botToken?: string;
}

export interface ErrorState {
  status: 'ERROR';
  message: string;
  code: string;
  data?: {
    errors?: ApiError[];
  };
  timestamp?: Date;
  path?: string;
}
