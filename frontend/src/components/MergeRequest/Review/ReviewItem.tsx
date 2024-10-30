import useTimeAgo from '@hooks/time';
import { Review } from 'types/review';
import ReviewComment from './ReviewComment';

const ReviewItem = ({ review }: { review: Review }) => {
  const timeAgo = useTimeAgo(review.createdAt);

  return (
    <div className="relative pl-6 border-l-2 py-6 border-secondary">
      {/* 리뷰어 정보 헤더 - 깃 트리의 시작점 */}
      <div className="flex items-center gap-2 mb-4">
        <div className="absolute -left-4">
          <img
            src={review.reviewer.avatarUrl}
            alt={review.reviewer.name}
            className="w-8 h-8 rounded-full bg-secondary"
          />
        </div>
        <div className="flex items-center gap-2">
          <span className="font-bold">{review.reviewer.name}</span>
          <span className="text-gray-700">reviewed</span>
          <span className="text-gray-700">{timeAgo}</span>
        </div>
      </div>

      {/* 메인 리뷰 코멘트 - 깃 트리와 살짝 겹치게 */}
      <div className="bg-white rounded-t-lg border-2 border-secondary rounded-lg -ml-10">
        <ReviewComment name={review.reviewer.name} content={review.content} createdAt={timeAgo} />
      </div>

      {/* 코드 리뷰 코멘트들 - 깃 트리 오른쪽에 표시 */}
      <div className="mt-4 space-y-4">
        {review.comments.map((comment) => (
          <div className="border-2 border-secondary rounded-lg" key={comment.id}>
            <ReviewComment
              name={comment.reviewer.name}
              content={comment.content}
              createdAt={comment.createdAt}
              backgroundColor="bg-secondary/30"
              reviewer={comment.reviewer}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default ReviewItem;
