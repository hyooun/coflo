import { useEffect, useState } from 'react';
import { Plus } from 'lucide-react';

import CommonReference from '@components/MergeRequest/Reference/CommonReference.tsx';
import { CommonButton } from '@components/Button/CommonButton';
import AddReferenceModal from '@components/Modal/AddReferenceModal';
import { Reference, ReferencesListProps } from 'types/reference.ts';
import ConfirmModal from '@components/Modal/ConfirmModal.tsx';

const ReferencesList = ({ references: initialReferences }: ReferencesListProps) => {
  const [references, setReferences] = useState<Reference[]>(initialReferences || []);
  const [isAddReferenceModalOpen, setIsAddReferenceModalOpen] = useState(false);
  const [isAlertModalOpen, setIsAlertModalOpen] = useState(false);
  const [alertModalContent, setAlertModalContent] = useState<string[]>([]);
  const [deleteTargetId, setDeleteTargetId] = useState<number | null>(null);

  useEffect(() => {
    setReferences(initialReferences || []);
  }, [initialReferences]);

  const handleEdit = (id: number, content: string) => {
    // API 호출 및 상태 업데이트 로직이 필요한가?
  };

  const handleDeleteClick = (id: number) => {
    setDeleteTargetId(id);
    setAlertModalContent(['정말 삭제하시겠습니까?']);
    setIsAlertModalOpen(true);
  };

  const handleAlertModalConfirm = () => {
    if (deleteTargetId !== null) {
      handleDelete(deleteTargetId);
      setDeleteTargetId(null);
    }
    setIsAlertModalOpen(false);
  };

  const handleAlertModalCancel = () => {
    setDeleteTargetId(null);
    setIsAlertModalOpen(false);
  };

  const handleDelete = (id: number) => {
    setReferences((prevReferences) => prevReferences.filter((reference) => reference.id !== id));
  };

  const handleRegistReference = () => {
    console.log('리뷰 재생성 : ', references);
  };

  const handleAddReference = () => {
    setIsAddReferenceModalOpen(true);
  };

  const handleAddReferenceSubmit = (language: string, content: string, fileName: string) => {
    const newReference: Reference = {
      id: Date.now(),
      fileName: fileName,
      content,
      language: language.toLowerCase(),
      type: language.toLowerCase() === 'plaintext' ? 'TEXT' : 'CODE',
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
            onDelete={handleDeleteClick}
            maxLength={3000}
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

      {isAlertModalOpen && (
        <ConfirmModal
          content={alertModalContent}
          onConfirm={handleAlertModalConfirm}
          onCancel={handleAlertModalCancel}
          className="w-72 h-44"
        />
      )}

      <div className="flex justify-center my-8">
        <CommonButton
          className="w-fit px-10 py-4 cursor-pointer"
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
