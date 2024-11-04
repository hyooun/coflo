import { atom, Setter } from 'jotai';

export const currentPageAtom = atom(1);
export const totalPagesAtom = atom(1);

export const updatePagination = (set: Setter, totalPages: number, currentPage: number) => {
  set(totalPagesAtom, totalPages);
  set(currentPageAtom, currentPage);
};
