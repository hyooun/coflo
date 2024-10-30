import { useEffect, useState } from 'react';
import { useAtom } from 'jotai';
import { currentPageAtom, totalPagesAtom } from '@store/pagination';
import { CustomSearchBar } from '@components/Mr/MrSearchBar';
import { MrItem } from '@components/Mr/MrItem';
import Pagination from '@components/Pagination/Pagination';
import { MergeRequest } from '@apis/MergeRequest';
import { useNavigate } from 'react-router-dom';
import { GitlabMergeRequest } from 'types/mr';

const MergeListPage = () => {
  const [currentPage, setCurrentPage] = useAtom(currentPageAtom);
  const [totalPages, setTotalPages] = useAtom(totalPagesAtom);
  const [mergeRequests, setMergeRequests] = useState<GitlabMergeRequest[]>([]);
  const itemsPerPage = 5;
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMergeRequests = async () => {
      const response = await MergeRequest.getMrList('some-keyword', currentPage, itemsPerPage);

      if (response && response.data) {
        setMergeRequests(response.data.gitlabMrList);
        setTotalPages(response.data.totalPages);
      }
    };

    fetchMergeRequests();
  }, [currentPage, setTotalPages]);

  return (
    <div className="flex flex-col flex-grow overflow-auto px-8 pt-6">
      <div className="max-w-3xl  p-6">
        <CustomSearchBar></CustomSearchBar>

        <div className="bg-white w-[1000px] ">
          {mergeRequests.map((mergeRequest) => (
            <div key={mergeRequest.id}>
              <MrItem mergeRequest={mergeRequest} />
              <div className="border-t border-gray-300" />
            </div>
          ))}
        </div>

        <Pagination />
      </div>
    </div>
  );
};

export default MergeListPage;
