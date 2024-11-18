import { ProjectLabel } from 'types/project';
import LabelButton from './LabelButton';

interface LabelListProps {
  labels: string[];
  projectLabels: ProjectLabel[];
}

const LabelList = ({ labels, projectLabels = [] }: LabelListProps) => {
  const getLabelStyle = (labelName: string) => {
    // projectLabels가 배열인지 확인
    const validProjectLabels = Array.isArray(projectLabels) ? projectLabels : [];
    const projectLabel = validProjectLabels.find((label) => label.name === labelName);

    return (
      projectLabel || {
        name: labelName,
        textColor: '#FFFFFF',
        bgColor: '#2C365B',
      }
    );
  };

  return (
    <div className="flex space-x-2">
      {labels.map((labelName, index) => (
        <LabelButton key={index} labelInfo={getLabelStyle(labelName)} />
      ))}
    </div>
  );
};

export default LabelList;
