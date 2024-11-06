import { atom } from 'jotai';

export const isLoginAtom = atom<boolean>(false);
export const isSignupAtom = atom<boolean>(false);
export const isConnectAtom = atom<boolean>(false);
export const projectIdAtom = atom<string | null>(null);
