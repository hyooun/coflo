import { CommonButton } from '@components/Button/CommonButton';

const labelColors: LabelColors = {
  CI: {
    bgColor: 'bg-yellow-500',
  },
  Backend: {
    bgColor: 'bg-blue-500',
  },
  '🐛 Fix': {
    bgColor: 'bg-red-500',
  },
  '✨ Feature': {
    bgColor: 'bg-[#D500FF]',
  },
  '♻️ Refactor': {
    bgColor: 'bg-[#00BC42]',
  },
  '📬 API': {
    bgColor: 'bg-gray-600',
  },
};

const LabelButton = ({ label }: { label: string }) => {
  const { bgColor } = labelColors[label] || { bgColor: 'bg-blue-500' };

  return (
    <CommonButton active={true} hoverColor="" bgColor={bgColor} className="w-[70px] h-[15px]">
      {label}
    </CommonButton>
  );
};

export default LabelButton;
