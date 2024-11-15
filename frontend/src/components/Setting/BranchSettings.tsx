import { useState } from 'react';
import { useAtom } from 'jotai';
import { projectIdAtom } from '@store/auth';
import BranchSelector from '@components/Repository/BranchSelector';

export interface BranchOption {
  value: string;
}

const BranchSettings = () => {
  const [projectId] = useAtom(projectIdAtom);
  const [selectedBranches, setSelectedBranches] = useState<BranchOption[]>([]);

  const handleSelect = (selected: BranchOption[]) => {
    setSelectedBranches(selected);
  };

  const handleSave = async () => {
    // TODO: API 연동하여 선택된 브랜치 저장
    console.log('Selected branches:', selectedBranches);
  };

  return (
    <div className="flex flex-col gap-4">
      <div className="flex flex-col gap-2">
        <h2 className="text-xl font-bold">참조 브랜치 설정</h2>
        <div className="flex items-center gap-2">
          <span className="text-gray-600">현재 참조중인 브랜치</span>
          <span className="bg-gray-100 px-3 py-1 rounded-lg">main, fe/dev, be/dev</span>
        </div>
      </div>

      <div className="w-[400px]">
        <BranchSelector
          value={selectedBranches}
          onChange={handleSelect}
          gitlabProjectId={Number(projectId)}
        />
      </div>

      <button
        onClick={handleSave}
        className="w-[100px] px-4 py-2 bg-primary-500 text-white rounded-xl hover:bg-primary-600 transition-colors"
      >
        설정
      </button>
    </div>
  );
};

export default BranchSettings;
