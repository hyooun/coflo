import CommonSelector from '@components/Common/CommonSelector';

interface GitUrlOption {
  value: string;
  label: string;
}

const gitUrlOptions: GitUrlOption[] = [
  {
    value: 'lab.ssafy.com',
    label: 'lab.ssafy.com',
  },
];

interface GitUrlSelectorProps {
  value: string;
  onChange: (value: string) => void;
  labelText?: string;
}

const GitUrlSelector = ({ value, onChange, labelText }: GitUrlSelectorProps) => {
  const selectedOption = gitUrlOptions.find((option) => option.value === value) || gitUrlOptions[0];

  return (
    <div className="w-full">
      {labelText && <label className="block mb-2 text-xl">{labelText}</label>}
      <CommonSelector<GitUrlOption>
        selectedItems={[selectedOption]}
        items={gitUrlOptions}
        onSelect={(option: GitUrlOption[]) => onChange(option[0].value)}
        displayValue={(option) => option.label}
        showSearch={false}
        className="relative"
        buttonClassName="w-full px-4 py-3 rounded-xl text-primary-500 border border-primary-500 bg-transparent flex justify-between items-center"
        dropdownClassName="absolute z-20 w-full mt-1 bg-white rounded-xl shadow-lg border border-primary-500"
        itemClassName="w-full text-left px-4 py-3 text-lg hover:bg-secondary/30 transition-colors rounded-xl"
      />
    </div>
  );
};

export default GitUrlSelector;
