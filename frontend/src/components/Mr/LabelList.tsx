import LabelButton from './LabelButton';

interface LabelListProps {
  labels: string[];
}

const LabelList = ({ labels }: LabelListProps) => {
  return (
    <div className="flex space-x-2">
      {labels.map((label, index) => (
        <LabelButton key={index} label={label} />
      ))}
    </div>
  );
};

export default LabelList;
