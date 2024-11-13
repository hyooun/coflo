import { atom } from 'jotai';
import { ErrorState } from '../types/api';

export const errorAtom = atom<ErrorState | null>(null);

export const setError = atom(null, (get, set, error: ErrorState) => {
  set(errorAtom, {
    ...error,
    timestamp: new Date(),
  });
});
