import { Reference } from 'types/review.ts';
import CommonReference from '@components/MergeRequest/Reference/CommonReference.tsx';
import { CommonButton } from '@components/Button/CommonButton';
import { Plus } from 'lucide-react';

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

  const handleAddReference = () => {
    console.log('참고 추가');
  };

  return (
    <div className="w-full my-4 min-w-[350px]">
      <div className="space-y-4">
        {references.map((reference) => (
          <CommonReference
            key={reference.id}
            id={reference.id}
            fileName={reference.fileName}
            content={reference.content}
            language={reference.language}
            type={reference.type}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />
        ))}
        <div
          className="flex flex-col justify-center items-center border-2 rounded-lg border-primary-500 py-10 space-y-4 hover:cursor-pointer"
          onClick={handleAddReference}
        >
          <Plus className="text-primary-500 w-14 h-14" strokeWidth={4} />
          <span className="text-primary-500 text-2xl"> 참고자료 추가하기 </span>
        </div>
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
