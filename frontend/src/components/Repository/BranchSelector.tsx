import { useEffect, useState } from 'react';
import CommonSelector from '@components/Common/CommonSelector';
import { Gitlab } from '@apis/Gitlab';

export interface BranchOption {
  value: string;
}

interface BranchSelectorProps {
  value: BranchOption[];
  onChange: (value: BranchOption[]) => void;
  gitlabProjectId: number;
}

const BranchSelector = ({ value, onChange, gitlabProjectId }: BranchSelectorProps) => {
  const [branchOptions, setBranchOptions] = useState<BranchOption[]>([]);
  const [selectedBranches, setSelectedBranches] = useState<BranchOption[]>(value);

  useEffect(() => {
    setSelectedBranches(value);
  }, [value]);

  useEffect(() => {
    const fetchBranches = async () => {
      const response = await Gitlab.getGitlabProjectBranches(gitlabProjectId);
      if (response.data) {
        const formattedOptions = response.data.map((branch: string) => ({
          value: branch,
        }));
        setBranchOptions(formattedOptions);
      }
    };

    fetchBranches();
  }, [gitlabProjectId]);

  const handleSelect = (selected: BranchOption[]) => {
    setSelectedBranches(selected);
    onChange(selected);
  };

  return (
    <div className="w-full">
      <CommonSelector<BranchOption>
        selectedItems={selectedBranches}
        onSelect={handleSelect}
        items={branchOptions}
        displayValue={(item) => item.value}
        multiSelect={true}
        showCheckbox={true}
        className="relative"
        buttonClassName="w-full px-4 py-3 rounded-xl text-primary-500 border border-primary-500 bg-transparent flex justify-between items-center"
        dropdownClassName="absolute z-20 w-full mt-1 bg-white rounded-xl shadow-lg border border-primary-500"
        itemClassName="w-full text-left px-4 py-3 text-lg hover:bg-secondary/30 transition-colors rounded-xl"
      />
    </div>
  );
};

export default BranchSelector;
