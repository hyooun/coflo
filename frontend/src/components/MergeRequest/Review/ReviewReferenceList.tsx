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
          {references.length > 0 ? '전체보기' : '추가하기'}
        </Link>
      </div>

      {/* References List */}
      <div className="space-y-4">
        {references.length === 0 ? (
          <div className="rounded-lg border-[1px] border-background-bnavy">
            <ReviewComment title="EMPTY" content="참고 자료가 없습니다." type="TEXT" />
          </div>
        ) : (
          references.slice(0, 5).map((reference) => {
            const referenceType: 'CODE' | 'TEXT' =
              reference.language === 'PLAINTEXT' ? 'TEXT' : 'CODE';

            return (
              <div
                key={reference.id}
                className="max-h-[330px] rounded-lg border-[1px] border-background-bnavy"
              >
                <ReviewComment
                  title={reference.fileName}
                  content={reference.content}
                  type={referenceType}
                />
              </div>
            );
          })
        )}
      </div>
    </div>
  );
};

export default ReviewReferencesList;
