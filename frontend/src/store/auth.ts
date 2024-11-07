import { atomWithStorage } from 'jotai/utils';

// localStorage에 저장될 키 이름들
export const LOGIN_STORAGE_KEY = 'isLogin';
export const SIGNUP_STORAGE_KEY = 'isSignup';
export const CONNECT_STORAGE_KEY = 'isConnect';
export const PROJECT_ID_STORAGE_KEY = 'projectId';

// atom 정의 - 두 번째 파라미터는 localStorage에 저장될 키 이름, 세 번째 파라미터는 초기값
export const isLoginAtom = atomWithStorage<boolean>(LOGIN_STORAGE_KEY, false);
export const isSignupAtom = atomWithStorage<boolean>(SIGNUP_STORAGE_KEY, false);
export const isConnectAtom = atomWithStorage<boolean>(CONNECT_STORAGE_KEY, false);
export const projectIdAtom = atomWithStorage<string | null>(PROJECT_ID_STORAGE_KEY, null);
