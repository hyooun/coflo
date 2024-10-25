import { atom } from 'jotai';

export const currentPageAtom = atom(1);
export const totalPagesAtom = atom(1);

export const updatePagination = (set: any, totalPages: number, currentPage: number) => {
  set(totalPagesAtom, totalPages);
  set(currentPageAtom, currentPage);
};
