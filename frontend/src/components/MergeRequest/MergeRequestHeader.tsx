import { GitlabMergeRequest } from 'types/mergeRequest.ts';
import { PullRequestIcon } from '@components/TextDiv/Icons/PullRequestIcon.tsx';
import { BranchIcon } from '@components/TextDiv/Icons/BranchIcon.tsx';
import { GitPullRequestClosed } from 'lucide-react';

const MergeRequestHeader = ({ mergeRequest }: { mergeRequest: GitlabMergeRequest }) => {
  const getStatusIcon = (state: GitlabMergeRequest['state']) => {
    switch (state) {
      case 'opened':
        return <PullRequestIcon className="w-4 h-4" />;
      case 'merged':
        return <BranchIcon className="w-4 h-4" />;
      case 'closed':
        return <GitPullRequestClosed className="w-4 h-4" />;
    }
  };

  return (
    <div className="flex flex-row justify-between items-center border-b-[1px] border-background-bnavy ">
      <div className="space-y-1 font-pretendard pb-2 w-full min-w-[700px]">
        {/* <div className="space-y-1 border-b-2 border-background-bnavy font-pretendard pb-4"> */}
        <div className="flex items-center gap-2 mb-4">
          <h1 className="text-2xl font-bold truncate">{mergeRequest.title}</h1>
          <span className="text-2xl text-gray-600">#{mergeRequest.iid}</span>
        </div>

        <div className="flex items-center gap-1 text-sm ">
          <div className="flex items-center gap-1 bg-primary-500 text-white rounded-2xl px-2">
            <span className="">{getStatusIcon(mergeRequest.state)}</span>
            <span className="font-bold">{mergeRequest.state}</span>
          </div>
          <div>
            <span className="font-bold">{mergeRequest.assignee.name}</span>
            <span className="mx-1">requested to merge</span>
            <span className="font-SFMono text-xs bg-secondary text-white rounded-2xl px-2">
              {mergeRequest.sourceBranch}
            </span>
            <span className="mx-1">to</span>
            <span className="font-SFMono text-xs  bg-secondary text-white rounded-2xl px-2">
              {mergeRequest.targetBranch}
            </span>
          </div>
        </div>
      </div>
      <div
        className="flex items-center justify-center bg-primary-500 text-white rounded-xl px-4 whitespace-nowrap w-fit h-10 cursor-pointer"
        onClick={() => window.open(mergeRequest.gitlabMrDetailUrl, '_blank', 'noopener')}
      >
        보러 가기
      </div>
    </div>
  );
};

export default MergeRequestHeader;
