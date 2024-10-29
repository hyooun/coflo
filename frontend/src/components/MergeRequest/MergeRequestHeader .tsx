import { useState, useEffect } from 'react';
import { GitlabMergeRequest } from 'types/mergeRequest.ts';
import { PullRequestIcon } from '@components/TextDiv/Icons/PullRequestIcon.tsx';

const MergeRequestHeader = ({ mergeRequestId }: { mergeRequestId: number }) => {
  const [mergeRequest, setMergeRequest] = useState<GitlabMergeRequest | null>(null);

  useEffect(() => {
    const fetchMergeRequest = async () => {
      const response = await fetch(`/api/merge-request/${mergeRequestId}`);
      const data = await response.json();
      setMergeRequest(data);
    };

    fetchMergeRequest();
  }, [mergeRequestId]);

  if (!mergeRequest) return <div>Loading...</div>;

  const getStatusColor = (state: GitlabMergeRequest['state']) => {
    switch (state) {
      case 'opened':
        return <PullRequestIcon />;
      case 'merged':
        return <PullRequestIcon className="w-4 h-4" />;
      case 'closed':
        return 'bg-red-500';
      default:
        return 'bg-gray-500';
    }
  };
  return (
    <div className="space-y-1 border-b font-pretendard">
      <div className="flex items-center gap-2">
        <h1 className="text-2xl font-bold">{mergeRequest.title}</h1>
        <span className="text-2xl text-gray-600">#{mergeRequest.id}</span>
      </div>

      <div className="flex items-center gap-1 text-sm ">
        <div className="flex items-center gap-2 bg-primary-500 text-white rounded-2xl px-2">
          <span className="">{getStatusColor(mergeRequest.state)}</span>
          <span className="font-bold">{mergeRequest.state}</span>
        </div>
        <div>
          <span className="font-bold">{mergeRequest.assignee.username}</span> requested to merge{' '}
          <span className="font-SFMono text-xs bg-secondary text-white rounded-2xl px-2">
            {mergeRequest.sourceBranch}
          </span>{' '}
          to{' '}
          <span className="font-SFMono text-xs  bg-secondary text-white rounded-2xl px-2">
            {mergeRequest.targetBranch}{' '}
          </span>
        </div>
      </div>
    </div>
  );
};

export default MergeRequestHeader;
