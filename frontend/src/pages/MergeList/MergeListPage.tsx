import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAtom } from 'jotai';
import { CustomSearchBar } from '@components/Mr/MrSearchBar';
import { MrItem } from '@components/Mr/MrItem';
import { MrStatusFilter } from '@components/MergeRequest/MrStatusFilter';
import { EmptyMergeRequest } from '@components/MergeRequest/EmptyMergeRequest';
import Pagination from '@components/Pagination/Pagination';
import { MergeRequest } from '@apis/MergeRequest';
import { GitlabMergeRequest } from 'types/mergeRequest';
import { currentPageAtom, totalPagesAtom } from '@store/pagination';
import { projectIdAtom } from '@store/auth';

const MergeListPage = () => {
  const navigate = useNavigate();
  const [currentPage] = useAtom(currentPageAtom);
  const [, setTotalPages] = useAtom(totalPagesAtom);
  const [mergeRequests, setMergeRequests] = useState<GitlabMergeRequest[]>([]);
  const itemsPerPage = 7;
  const [projectId] = useAtom(projectIdAtom);
  const [currentStatus, setCurrentStatus] = useState('opened');
  const [searchKeyword, setSearchKeyword] = useState('');
  const [, setSearchType] = useState('All');

  const handleStatusChange = (status: string) => {
    setCurrentStatus(status);
  };

  const handleSearch = (keyword: string, searchType: string) => {
    setSearchKeyword(keyword);
    setSearchType(searchType);
  };

  const handleItemClick = (id: number) => {
    navigate(`/${projectId}/main/merge-request/reviews/${id}`);
  };

  useEffect(() => {
    const fetchMergeRequests = async () => {
      if (!projectId) {
        return;
      }
      const response = await MergeRequest.getMrList(projectId, currentStatus, {
        keyword: searchKeyword,
        page: Number(currentPage),
        size: Number(itemsPerPage),
      });

      if (response.data) {
        setTotalPages(response.data.totalPages ?? 1);
        setMergeRequests(response.data.gitlabMrList);
      }
    };

    fetchMergeRequests();
  }, [projectId, currentStatus, currentPage]);

  return (
    <div className="flex flex-col flex-grow overflow-auto p-6 items-stretch justify-between w-full">
      <div className="flex flex-col justify-between w-full">
        <MrStatusFilter onStatusChange={handleStatusChange} />
        <CustomSearchBar onSearch={handleSearch} />
      </div>

      <div className="bg-white flex flex-col justify-between h-full py-4">
        {mergeRequests.length === 0 ? (
          <EmptyMergeRequest />
        ) : (
          mergeRequests.map((mergeRequest) => (
            <div
              key={mergeRequest.id}
              onClick={() => handleItemClick(mergeRequest.id)}
              className="cursor-pointer"
            >
              <MrItem mergeRequest={mergeRequest} />
              <div className="border-t border-gray-300" />
            </div>
          ))
        )}
      </div>

      <Pagination />
    </div>
  );
};

export default MergeListPage;
