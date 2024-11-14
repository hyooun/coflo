import { atom } from 'jotai';
import { atomWithStorage } from 'jotai/utils';

// localStorage 사용하지 않고 일반 atom으로 변경
export const isLoginAtom = atomWithStorage<boolean>('isLogin', false);
export const isSignupAtom = atom<boolean>(false);
export const isConnectAtom = atom<boolean>(false);
export const projectIdAtom = atomWithStorage<string | null>('projectId', null);
export const projectFullPathAtom = atomWithStorage<string | null>('projectFullPath', null);
export const notificationAtom = atomWithStorage('notification', null);
