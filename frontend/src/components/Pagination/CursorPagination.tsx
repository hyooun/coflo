import { ChevronLeft, ChevronRight } from 'lucide-react';

interface CursorPaginationProps {
  hasNextPage: boolean;
  hasPreviousPage: boolean;
  onNext: () => void;
  onPrevious: () => void;
}

export default function CursorPagination({
  hasNextPage,
  hasPreviousPage,
  onNext,
  onPrevious,
}: CursorPaginationProps) {
  return (
    <div className="flex justify-center items-center gap-4 mt-4">
      <button
        onClick={onPrevious}
        disabled={!hasPreviousPage}
        className={`p-2 rounded ${
          !hasPreviousPage ? 'text-gray-300 cursor-not-allowed' : 'text-gray-600 hover:bg-gray-100'
        }`}
      >
        <ChevronLeft size={24} />
      </button>
      <button
        onClick={onNext}
        disabled={!hasNextPage}
        className={`p-2 rounded ${
          !hasNextPage ? 'text-gray-300 cursor-not-allowed' : 'text-gray-600 hover:bg-gray-100'
        }`}
      >
        <ChevronRight size={24} />
      </button>
    </div>
  );
}
