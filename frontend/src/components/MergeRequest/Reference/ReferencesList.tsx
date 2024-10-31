import { Reference } from 'types/review.ts';
import CommonReference from '@components/MergeRequest/Reference/CommonReference.tsx';
import { CommonButton } from '@components/Button/CommonButton';

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

  const handleRegistReference = () => {
    console.log('리뷰 재생성');
  };

  return (
    <div className="w-full my-4">
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

      <div className="flex justify-end my-4">
        <CommonButton
          className="w-fit px-10 py-4"
          bgColor="bg-primary-500"
          active={true}
          onClick={handleRegistReference}
        >
          리뷰 재생성
        </CommonButton>
      </div>
    </div>
  );
};

export default ReferencesList;
