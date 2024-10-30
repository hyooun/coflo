// components/references/ReferencesList.tsx
import { Link, useParams } from 'react-router-dom';
import { Reference } from 'types/review';
import ReviewComment from 'components/MergeRequest/Review/ReviewComment';

interface ReviewReferencesListProps {
  references: Reference[];
}

const ReviewReferencesList = ({ references }: ReviewReferencesListProps) => {
  const { id } = useParams();

  return (
    <div className="p-4 font-pretendard w-[500px]">
      {/* Header */}
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-2xl font-bold">References</h2>
        <Link
          to={`/main/merge-request/reviews/${id}/references`}
          className="hover:text-gray-800 transition-colors"
        >
          전체보기
        </Link>
      </div>

      {/* References List */}
      <div className="space-y-4">
        {references.map((reference) => (
          <div key={reference.id} className="max-h-[330px] rounded-lg border-2 border-secondary">
            <ReviewComment
              title={reference.fileName}
              content={reference.content}
              type={reference.type}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default ReviewReferencesList;
