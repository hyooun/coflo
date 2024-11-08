import { GitPullRequest } from 'lucide-react';

export const EmptyMergeRequest = () => {
  return (
    <div className="flex flex-col items-center justify-center py-16 min-h-[600px]">
      <div className="w-32 h-32 bg-primary-500 rounded-full flex items-center justify-center mb-6">
        <GitPullRequest className="w-16 h-16 text-white" />
      </div>
      <h3 className="text-2xl font-semibold text-primary-500 mb-2">No Open Merge Request</h3>
    </div>
  );
};
