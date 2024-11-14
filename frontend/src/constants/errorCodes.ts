export type ApiErrorCode = 'G001' | 'B001' | 'Z009' | 'OFFLINE' | 'UNKNOWN';

export const ERROR_MESSAGES = {
  B001: '리포지토리를 찾을 수 없습니다',
  B002: '권한이 없습니다',
  B003: '잘못된 요청입니다',
  B004: '서버 오류가 발생했습니다',
} as const;

export const HTTP_STATUS = {
  UNAUTHORIZED: 401,
  NOT_FOUND: 404,
  INTERNAL_SERVER_ERROR: 500,
} as const;
