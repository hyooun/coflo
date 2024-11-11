// components/references/ReferencesList.tsx
import { Link, useParams } from 'react-router-dom';
import { Reference } from 'types/reference';
import ReviewComment from 'components/MergeRequest/Review/ReviewComment';
import { projectIdAtom } from '@store/auth';
import { useAtom } from 'jotai';

interface ReviewReferencesListProps {
  references: Reference[];
  selectedReviewId: string | null;
}

const ReviewReferencesList = ({ references, selectedReviewId }: ReviewReferencesListProps) => {
  const { id } = useParams();
  const [projectId] = useAtom(projectIdAtom);

  return (
    <div className="p-4 font-pretendard flex-[3] min-w-[330px]">
      {/* Header */}
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-2xl font-bold">References</h2>
        <Link
          to={`/${projectId}/main/merge-request/reviews/${id}/references/${selectedReviewId}`}
          className="hover:text-gray-800 transition-colors"
        >
          전체보기
        </Link>
      </div>

      {/* References List */}
      <div className="space-y-4">
        {references.map((reference) => {
          const referenceType: 'CODE' | 'TEXT' =
            reference.language === 'PLAINTEXT' ? 'TEXT' : 'CODE';

          return (
            <div key={reference.id} className="max-h-[330px] rounded-lg border-2 border-secondary">
              <ReviewComment
                title={reference.fileName}
                content={reference.content}
                type={referenceType}
              />
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default ReviewReferencesList;
