import { atom } from 'jotai';

// localStorage 사용하지 않고 일반 atom으로 변경
export const isLoginAtom = atom<boolean>(false);
export const isSignupAtom = atom<boolean>(false);
export const isConnectAtom = atom<boolean>(false);
export const projectIdAtom = atom<string | null>(null);
