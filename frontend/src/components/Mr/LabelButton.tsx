import { CommonButton } from '@components/Button/CommonButton';
import { ProjectLabel } from 'types/project';

const LabelButton = ({ labelInfo }: { labelInfo?: ProjectLabel }) => {
  const { name, bgColor, textColor } = labelInfo || {
    name: '',
    textColor: '#ffffff',
    bgColor: '#2C365B',
  };

  return (
    <CommonButton
      active={true}
      hoverColor=""
      style={{
        backgroundColor: bgColor,
        color: textColor,
      }}
      className="w-fit h-full px-2"
    >
      {name}
    </CommonButton>
  );
};

export default LabelButton;
