import { Reference } from 'types/review.ts';
import CommonReference from '@components/MergeRequest/Reference/CommonReference.tsx';

interface ReferencesListProps {
  references: Reference[];
}

const ReferencesList = ({ references }: ReferencesListProps) => {
  const handleEdit = (id: number, content: string) => {
    // API 호출 및 상태 업데이트 로직
  };

  const handleDelete = (id: number) => {
    // API 호출 및 상태 업데이트 로직
  };

  return (
    <div className="w-5/6">
      <div className="space-y-4">
        {references.map((reference) => (
          <CommonReference
            key={reference.id}
            id={reference.id}
            fileName={reference.fileName}
            content={reference.content}
            type={reference.type}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />
        ))}
      </div>
    </div>
  );
};

export default ReferencesList;
