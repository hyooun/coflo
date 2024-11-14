import { useEffect, useState } from 'react';
import { Plus } from 'lucide-react';

import CommonReference from '@components/MergeRequest/Reference/CommonReference.tsx';
import { CommonButton } from '@components/Button/CommonButton';
import AddReferenceModal from '@components/Modal/AddReferenceModal';
import { Reference, ReferencesListProps } from 'types/reference.ts';
import ConfirmModal from '@components/Modal/ConfirmModal.tsx';
import { useParams, useNavigate } from 'react-router-dom';
import { Review } from '@apis/Review';
import { useAtom } from 'jotai';
import { projectIdAtom } from '@store/auth';
// import { useNotification } from './useNotification';
// import { useLocation } from 'react-router-dom';
// import Loading from './Loading';

const ReferencesList = ({ references: initialReferences }: ReferencesListProps) => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string; selectedReviewId: string }>();
  const [projectId] = useAtom(projectIdAtom);
  const [references, setReferences] = useState<Reference[]>(initialReferences || []);
  const [isAddReferenceModalOpen, setIsAddReferenceModalOpen] = useState(false);
  const [isAlertModalOpen, setIsAlertModalOpen] = useState(false);
  const [alertModalContent, setAlertModalContent] = useState<string[]>([]);
  const [deleteTargetId, setDeleteTargetId] = useState<number | null>(null);
  const [, setIsLoading] = useState(true);

  // const location = useLocation();
  // const sendReviewId = location.state?.sendReviewId;

  // const { notify } = useNotification(sendReviewId || '', id || '', projectId || '', setIsLoading);

  useEffect(() => {
    setReferences(initialReferences || []);
    setIsLoading(false);
  }, [initialReferences]);

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

  const handleRegistReference = async () => {
    if (!id) {
      setAlertModalContent(['머지 리퀘스트 ID가 없습니다.']);
      setIsAlertModalOpen(true);
      return;
    }
    // setIsLoading(true);

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

      const response = await Review.regenerateReview(projectId, id, retrievals);
      console.log('response: ', response);
      setAlertModalContent(['리뷰가 재생성되었습니다.']);
      setIsAlertModalOpen(true);

      // notify();

      navigate(`/${projectId}/main/merge-request/reviews/${id}`);
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
              className="flex flex-col justify-center items-center border-2 rounded-lg border-primary-500 py-10 space-y-4 hover:cursor-pointer"
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
      {/* )} */}
    </div>
  );
};

export default ReferencesList;
