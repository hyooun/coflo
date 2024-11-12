import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { BestMergeRequest } from 'types/mergeRequest.ts';
import { PullRequestIcon } from '@components/TextDiv/Icons/PullRequestIcon.tsx';
import { MergeRequest } from '@apis/MergeRequest';
import { projectIdAtom } from '@store/auth';
import { useAtom } from 'jotai';
import Title from '@components/Title/Title';

const BestMergeRequestList = () => {
  const [bestMergeRequests, setBestMergeRequests] = useState<BestMergeRequest[]>([]);
  const navigate = useNavigate();
  const [projectId] = useAtom(projectIdAtom);

  useEffect(() => {
    if (!projectId) return;
    const fetchMergeRequests = async () => {
      const response = await MergeRequest.getBestMrList(projectId);
      if (response.data) {
        setBestMergeRequests(response.data);
      }
    };

    fetchMergeRequests();
  }, [projectId]);

  const handleItemClick = (iid: number) => {
    navigate(`/${projectId}/main/merge-request/reviews/${iid}`);
  };

  return (
    <>
      <Title title="Best Merge Request" textSize="text-lg" />
      <div className="flex-grow bg-primary-100/20 border-2 border-secondary rounded-lg">
        {bestMergeRequests.length === 0 ? (
          <div className="w-full h-full flex flex-col items-center justify-center text-gray-700 text-lg">
            <p>아직 BEST MR이 없습니다.</p>
            <p>MR을 올려주세요.</p>
          </div>
        ) : (
          <div className="flex flex-col justify-around h-full overflow-y-auto p-4">
            {bestMergeRequests.map((mr) => (
              <div
                key={mr.id}
                className="flex justify-between items-center rounded-lg hover:bg-gray-200 cursor-pointer p-4"
                onClick={() => handleItemClick(mr.iid)}
              >
                <div className="flex flex-col min-w-0 flex-1 space-y-3">
                  <div className="flex items-center gap-2">
                    <div className="flex -space-x-2 flex-shrink-0">
                      <img
                        src={mr.assignee.avatarUrl}
                        alt="Assignee"
                        className="w-8 h-8 rounded-full border-2 border-white bg-white shadow-sm"
                      />
                      <img
                        src={mr.reviewer.avatarUrl}
                        alt="Reviewer"
                        className="w-8 h-8 rounded-full border-2 border-white bg-white shadow-sm z-10"
                      />
                    </div>
                    <div className="flex items-center min-w-0 flex-1">
                      <PullRequestIcon className="w-5 h-5 flex-shrink-0 mr-2" />
                      <span className="font-bold text-sm mr-3">{mr.targetBranch}</span>
                      <span className="text-base font-medium text-gray-700 truncate">
                        {mr.title}
                      </span>
                    </div>
                  </div>
                  <div className="flex items-center mt-1 space-x-3">
                    <div className="flex space-x-1">
                      {mr.labels.nodes.slice(0, 2).map((label, index) => (
                        <span
                          key={index}
                          className="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium text-white"
                          style={{ backgroundColor: label.color }}
                        >
                          {label.title}
                        </span>
                      ))}
                    </div>
                    <span className="text-sm text-gray-700 ml-2 flex-shrink-0">
                      created {mr.closedAt} by {mr.assignee.username}
                    </span>
                  </div>
                </div>
                <div className="font-extrabold text-xl">→</div>
              </div>
            ))}
          </div>
        )}
      </div>
    </>
  );
};

export default BestMergeRequestList;
