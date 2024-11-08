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
  const [setTotalPages] = useAtom(totalPagesAtom);
  const [mergeRequests, setMergeRequests] = useState<GitlabMergeRequest[]>([]);
  const itemsPerPage = 5;
  const [projectId] = useAtom(projectIdAtom);
  const [currentStatus, setCurrentStatus] = useState('opened');

  const handleStatusChange = (status: string) => {
    setCurrentStatus(status);
  };

  const handleItemClick = (id: number) => {
    navigate(`/main/merge-request/reviews/${id}`);
  };

  useEffect(() => {
    const fetchMergeRequests = async () => {
      if (!projectId) {
        return;
      }
      const response = await MergeRequest.getMrList(projectId, currentStatus, {
        keyword: '',
        page: Number(currentPage),
        size: Number(itemsPerPage),
      });

      if (response && response.data) {
        setMergeRequests(response.data.gitlabMrList);
      }
    };

    fetchMergeRequests();
  }, [projectId, currentStatus, currentPage]);

  return (
    <div className="flex flex-col flex-grow overflow-auto px-8 pt-6">
      <div className="max-w-3xl  p-6">
        <MrStatusFilter onStatusChange={handleStatusChange} />
        <CustomSearchBar></CustomSearchBar>

        <div className="bg-white w-[1000px]">
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
    </div>
  );
};

export default MergeListPage;
