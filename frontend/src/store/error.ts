import { atom } from 'jotai';

export const errorAtom = atom<string>(''); // 기본값은 빈 문자열

export const setError = atom(null, (get, set, errorMessage: string) => {
  set(errorAtom, errorMessage); // 에러 메시지 업데이트
});
