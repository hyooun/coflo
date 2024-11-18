import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAtom } from 'jotai';
import { CustomSearchBar } from '@components/Mr/MrSearchBar';
import { MrItem } from '@components/Mr/MrItem';
import { MrStatusFilter } from '@components/MergeRequest/MrStatusFilter';
import { EmptyMergeRequest } from '@components/MergeRequest/EmptyMergeRequest';
import Pagination from '@components/Pagination/Pagination';
import { currentPageAtom, totalPagesAtom } from '@store/pagination';
import { projectIdAtom } from '@store/auth';
import { MergeRequest } from '@apis/MergeRequest';
import { ProjectRequest } from '@apis/Project';
import { GitlabMergeRequest } from 'types/mergeRequest';
import { ProjectLabel } from 'types/project';

const MergeListPage = () => {
  const navigate = useNavigate();
  const itemsPerPage = 7;
  const [currentPage] = useAtom(currentPageAtom);
  const [, setTotalPages] = useAtom(totalPagesAtom);
  const [projectId] = useAtom(projectIdAtom);
  const [mergeRequests, setMergeRequests] = useState<GitlabMergeRequest[]>([]);
  const [currentStatus, setCurrentStatus] = useState('opened');
  const [searchKeyword, setSearchKeyword] = useState('');
  const [, setSearchType] = useState('All');
  const [projectLabels, setProjectLabels] = useState<ProjectLabel[]>([]);

  const fetchMergeRequests = async () => {
    if (!projectId) return;

    // MR 리스트와 라벨 정보를 병렬로 조회
    const [mrResponse, labelsResponse] = await Promise.all([
      MergeRequest.getMrList(projectId, currentStatus, {
        keyword: searchKeyword,
        page: Number(currentPage),
        size: Number(itemsPerPage),
      }),
      ProjectRequest.getProjectLabels(projectId),
    ]);

    if (mrResponse.data) {
      setTotalPages(mrResponse.data.totalPages ?? 1);
      setMergeRequests(mrResponse.data.gitlabMrList);
    }

    if (labelsResponse.data) {
      setProjectLabels(labelsResponse.data.labels);
    }
  };

  const handleStatusChange = (status: string) => {
    setCurrentStatus(status);
  };

  const handleSearch = async (keyword: string, searchType: string) => {
    setSearchKeyword(keyword);
    setSearchType(searchType);

    if (!projectId) return;
    await fetchMergeRequests();
  };

  const handleItemClick = (iid: number) => {
    navigate(`/${projectId}/main/merge-request/reviews/${iid}`);
  };

  useEffect(() => {
    fetchMergeRequests();
  }, [projectId, currentStatus, currentPage]);

  return (
    <div className="h-full overflow-auto w-full">
      <div className="flex flex-col justify-between min-h-full p-6">
        <div className="flex flex-col gap-4">
          <MrStatusFilter onStatusChange={handleStatusChange} />
          <CustomSearchBar onSearch={handleSearch} showOption={false} />
        </div>

        <div className="flex-1 bg-white">
          <div className="pb-4 h-full">
            {mergeRequests.length === 0 ? (
              <EmptyMergeRequest />
            ) : (
              mergeRequests.map((mergeRequest) => (
                <div
                  key={mergeRequest.id}
                  onClick={() => handleItemClick(mergeRequest.iid)}
                  className="cursor-pointer"
                >
                  <MrItem mergeRequest={mergeRequest} projectLabels={projectLabels} />
                  <div className="border-1px border-gray-300" />
                </div>
              ))
            )}
          </div>
        </div>

        <Pagination />
      </div>
    </div>
  );
};

export default MergeListPage;
