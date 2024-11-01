import { useEffect, useState } from 'react';
import { Plus } from 'lucide-react';

import CommonReference from '@components/MergeRequest/Reference/CommonReference.tsx';
import { CommonButton } from '@components/Button/CommonButton';
import AddReferenceModal from '@components/Modal/AddReferenceModal';
import { Reference, ReferencesListProps } from 'types/reference.ts';

const ReferencesList = ({ references: initialReferences }: ReferencesListProps) => {
  const [references, setReferences] = useState<Reference[]>(initialReferences || []);
  const [isAddReferenceModalOpen, setIsAddReferenceModalOpen] = useState(false);

  useEffect(() => {
    setReferences(initialReferences || []);
  }, [initialReferences]);

  const handleEdit = (id: number, content: string) => {
    // API 호출 및 상태 업데이트 로직
  };

  const handleDelete = (id: number) => {
    setReferences((prevReferences) => prevReferences.filter((reference) => reference.id !== id));
  };

  const handleRegistReference = () => {
    console.log('리뷰 재생성');
  };

  const handleAddReference = () => {
    setIsAddReferenceModalOpen(true);
  };

  const handleAddReferenceSubmit = (language: string, content: string, fileName: string) => {
    console.log(language, content);
    const newReference: Reference = {
      id: Date.now(), // 임시 ID, API 응답에서 실제 ID를 받아야 함
      fileName: fileName, // 파일 이름도 필요하다면 모달에서 입력받을 수 있음
      content,
      language: language.toLowerCase(),
      type: 'CODE', // 또는 'TEXT'로 구분이 필요하다면 모달에서 선택할 수 있게 수정
    };

    setReferences((prev) => [...prev, newReference]);
    setIsAddReferenceModalOpen(false);
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

      <AddReferenceModal
        isOpen={isAddReferenceModalOpen}
        onClose={() => setIsAddReferenceModalOpen(false)}
        onSubmit={handleAddReferenceSubmit}
      />

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
