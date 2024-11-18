import { useEffect, useState } from 'react';
import { CircleCheck, Plus } from 'lucide-react';

import CommonReference from '@components/MergeRequest/Reference/CommonReference.tsx';
import { CommonButton } from '@components/Button/CommonButton';
import AddReferenceModal from '@components/Modal/AddReferenceModal';
import { Reference, ReferencesListProps } from 'types/reference.ts';
import ConfirmModal from '@components/Modal/ConfirmModal.tsx';
import { useParams, useNavigate } from 'react-router-dom';
import { Review } from '@apis/Review';
import { useAtom } from 'jotai';
import { projectIdAtom } from '@store/auth';
import AlertModal from '@components/Modal/AlertModal';

const ReferencesList = ({ references: initialReferences }: ReferencesListProps) => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string; selectedReviewId: string }>();
  const [projectId] = useAtom(projectIdAtom);
  const [references, setReferences] = useState<Reference[]>(initialReferences || []);
  const [isAddReferenceModalOpen, setIsAddReferenceModalOpen] = useState(false);
  const [isAlertModalOpen, setIsAlertModalOpen] = useState(false);
  const [alertModalContent, setAlertModalContent] = useState<string[]>([]);
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);
  const [confirmModalContent, setConfirmModalContent] = useState<string[]>([]);
  const [deleteTargetId, setDeleteTargetId] = useState<number | null>(null);
  const [, setIsLoading] = useState(true);

  useEffect(() => {
    setReferences(initialReferences || []);
    setIsLoading(false);
  }, [initialReferences]);

  const handleDeleteClick = (id: number) => {
    setDeleteTargetId(id);
    setConfirmModalContent(['정말 삭제하시겠습니까?']);
    setIsConfirmModalOpen(true);
  };

  const handleConfirmModal = () => {
    if (deleteTargetId !== null) {
      handleDelete(deleteTargetId);
      setDeleteTargetId(null);
    }
    setIsAlertModalOpen(false);
  };

  const handleConfirmModalCancel = () => {
    setDeleteTargetId(null);
    setIsAlertModalOpen(false);
  };

  const handleDelete = (id: number) => {
    setReferences((prevReferences) => prevReferences.filter((reference) => reference.id !== id));
  };

  const handleRegistReference = async () => {
    if (!id) {
      setAlertModalContent(['머지 리퀘스트 ID가 없습니다.']);
      setIsAlertModalOpen(true);
      return;
    }
    try {
      const retrievals = references.map((ref) => ({
        fileName: ref.fileName,
        content: ref.content,
        language: ref.language.toUpperCase(),
      }));

      if (!projectId) {
        setAlertModalContent(['프로젝트 ID가 없습니다.']);
        setIsAlertModalOpen(true);
        return;
      }

      await Review.regenerateReview(projectId, id, retrievals);
      setAlertModalContent(['리뷰 재생성 요청을 보냈습니다.']);
      setIsAlertModalOpen(true);
    } catch (error) {
      console.error('리뷰 재생성 중 오류 발생:', error);
      setAlertModalContent(['리뷰 재생성 중 오류가 발생했습니다.']);
      setIsAlertModalOpen(true);
    }
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
    };

    setReferences((prev) => [...prev, newReference]);
    setIsAddReferenceModalOpen(false);
  };

  const handleAlertModalConfirm = () => {
    setIsAlertModalOpen(false);
    navigate(`/${projectId}/main/merge-request/reviews/${id}`);
  };

  return (
    <div>
      {/* {isLoading ? (
        <Loading />
      ) : ( */}
      <div className="w-full my-4 min-w-[350px]">
        <div className="space-y-4">
          {references.map((reference) => {
            const referenceType =
              reference.language.toLowerCase() === 'plaintext' ? 'TEXT' : 'CODE';

            return (
              <CommonReference
                key={reference.id}
                id={reference.id}
                fileName={reference.fileName}
                content={reference.content}
                language={reference.language}
                type={referenceType}
                onDelete={handleDeleteClick}
                maxLength={3000}
              />
            );
          })}
          {references.length < 15 && (
            <div
              className="flex flex-col justify-center items-center border-2 rounded-lg border-background-bnavy py-10 space-y-4 hover:cursor-pointer"
              onClick={handleAddReference}
              aria-label="Add a new reference"
            >
              <Plus className="text-primary-500 w-14 h-14" strokeWidth={4} />
              <span className="text-primary-500 text-2xl">참고자료 추가하기</span>
            </div>
          )}
        </div>

        <AddReferenceModal
          isOpen={isAddReferenceModalOpen}
          onClose={() => setIsAddReferenceModalOpen(false)}
          onSubmit={handleAddReferenceSubmit}
        />

        {isConfirmModalOpen && (
          <ConfirmModal
            content={confirmModalContent}
            onConfirm={handleConfirmModal}
            onCancel={handleConfirmModalCancel}
            className="w-72 h-44"
          />
        )}

        <div className="flex justify-center my-8">
          <CommonButton
            className="w-fit px-10 py-4 cursor-pointer text-white"
            bgColor="bg-primary-500"
            active={true}
            onClick={handleRegistReference}
          >
            리뷰 재생성
          </CommonButton>
        </div>
      </div>
      {isAlertModalOpen && (
        <AlertModal
          content={alertModalContent}
          onConfirm={handleAlertModalConfirm}
          className="w-72 h-44"
          icon={CircleCheck}
          iconClassName="text-state-success"
          iconSize={48}
        />
      )}
    </div>
  );
};

export default ReferencesList;
