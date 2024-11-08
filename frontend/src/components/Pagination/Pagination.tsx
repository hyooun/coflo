import { useAtomValue } from 'jotai';
import { useAtom } from 'jotai';
import { currentPageAtom, totalPagesAtom } from '@store/pagination';

import { LeftArrow } from './icons/LeftArrow';
import { RightArrow } from './icons/RightArrow';

const Pagination = () => {
  const [currentPage, setCurrentPage] = useAtom(currentPageAtom);
  const totalPages = useAtomValue(totalPagesAtom);
  const totalDisplayedPages = 10;

  console.log('currentPage:', currentPage, 'totalPages:', totalPages);
  if (typeof currentPage !== 'number' || typeof totalPages !== 'number') {
    console.error('currentPage 또는 totalPages가 숫자가 아닙니다:', { currentPage, totalPages });
    return <div></div>;
  }

  const handlePageChange = (page: number) => {
    if (currentPage === page) {
      return;
    }
    setCurrentPage(page);
  };

  const getPaginationItems = () => {
    const items = [];
    if (totalPages <= totalDisplayedPages) {
      for (let i = 1; i <= totalPages; i++) {
        items.push(i);
      }
    } else {
      const leftPages = 3;
      const rightPages = 3;

      if (currentPage > leftPages + 2) {
        items.push(1);
        items.push('...');
        for (let i = Math.max(2, currentPage - leftPages); i < currentPage; i++) {
          items.push(i);
        }
      } else {
        for (let i = 1; i < currentPage; i++) {
          items.push(i);
        }
      }

      items.push(currentPage);

      if (currentPage < totalPages - rightPages - 1) {
        for (
          let i = currentPage + 1;
          i <= Math.min(currentPage + rightPages, totalPages - 1);
          i++
        ) {
          items.push(i);
        }
        items.push('...');
      } else {
        for (let i = currentPage + 1; i <= totalPages; i++) {
          items.push(i);
        }
      }

      if (!items.includes(totalPages)) {
        items.push(totalPages);
      }
    }
    return items;
  };

  const paginationItems = getPaginationItems();

  return (
    <div className="flex justify-center">
      <button
        className="flex items-center px-4 py-2 rounded mx-1"
        onClick={() => handlePageChange(Math.max(currentPage - 1, 1))}
        disabled={currentPage === 1}
      >
        <div className="mr-2">
          <LeftArrow />
        </div>
      </button>
      <div className="flex justify-center">
        {paginationItems.map((item, index) => (
          <button
            key={index}
            className={`w-[40px] h-[40px] flex items-center justify-center ${item === currentPage ? 'bg-primary-500 text-white' : ''} rounded mx-1`}
            onClick={() => typeof item === 'number' && handlePageChange(item)}
            disabled={item === '...'}
          >
            {item}
          </button>
        ))}
      </div>
      <button
        className="flex items-center px-4 py-2 rounded mx-1"
        onClick={() => handlePageChange(Math.min(currentPage + 1, totalPages))}
        disabled={currentPage === totalPages}
      >
        <div className="ml-2">
          <RightArrow />
        </div>
      </button>
    </div>
  );
};

export default Pagination;
