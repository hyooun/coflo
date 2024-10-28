import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { MergeRequest } from 'types/mergeRequest.ts';
import { PullRequestIcon } from '@components/TextDiv/Icons/PullRequestIcon.tsx';

const BestMergeRequestList = () => {
  const [mergeRequests, setMergeRequests] = useState<MergeRequest[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMergeRequests = async () => {
      const response = await fetch('/api/best-merge-requests');
      const data = await response.json();
      setMergeRequests(data.slice(0, 3)); // 상위 3개만 표시
    };

    fetchMergeRequests();
  }, []);

  const handleItemClick = (id: number) => {
    navigate(`/review/${id}`);
  };

  return (
    <div className="font-pretendard">
      <h2 className="text-lg font-bold mb-2">Best Merge Request</h2>
      <div className="p-2 bg-gray-400 border-2 border-gray-500 rounded-lg ">
        {mergeRequests.map((mr) => (
          <div
            key={mr.id}
            className="flex items-center justify-between p-4 rounded-lg mb-4 cursor-pointer group hover:bg-gray-200"
            onClick={() => handleItemClick(mr.id)}
          >
            <div className="flex flex-col min-w-0 flex-1">
              {/* Avatars and labels column */}
              <div className="flex items-center space-x-3 mb-2">
                {/* Overlapping avatars */}
                <div className="flex -space-x-3">
                  <img
                    src={mr.assignee}
                    alt="Assignee"
                    className="w-8 h-8 rounded-full border-2 border-white bg-white shadow-sm"
                  />
                  <img
                    src={mr.reviewer}
                    alt="Reviewer"
                    className="w-8 h-8 rounded-full border-2 border-white bg-white shadow-sm z-10"
                  />
                </div>
                <div className="flex items-center min-w-0">
                  <PullRequestIcon className="w-4 h-4 flex-shrink-0 mr-1" />
                  <span className="font-bold mr-1"> {mr.branchName} </span>
                  <span className="text-sm font-medium text-gray-700 truncate">{mr.title}</span>
                </div>
              </div>
              <div className="flex items-center mt-1 space-x-3">
                {/* Labels under avatars - maximum 2 labels */}
                <div className="flex space-x-1">
                  {mr.labels.slice(0, 2).map((label, index) => (
                    <span
                      key={index}
                      className="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium bg-secondary text-white"
                    >
                      {label}
                    </span>
                  ))}
                </div>
                <span className="text-xs text-gray-700 ml-4 flex-shrink-0">
                  created {mr.createdAt} by {mr.author}
                </span>
              </div>
            </div>

            <div className=" text-xl">→</div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default BestMergeRequestList;
