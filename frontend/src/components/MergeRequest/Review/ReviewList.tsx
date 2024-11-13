import { Review } from 'types/review.ts';
import ReviewItem from './ReviewItem';
import { GitlabMergeRequest } from 'types/mergeRequest';

const ReviewList = ({
  reviews,
  mergeRequest,
  onReviewClick,
}: {
  reviews: Review[];
  mergeRequest: GitlabMergeRequest;
  onReviewClick: (reviewId: string) => void;
}) => {
  return (
    <div className="font-pretendard flex-[4] pl-4 min-w-[500px] relative">
      <div className="absolute top-0 left-[1rem] h-full border-l-[1px] border-background-bnavy" />
      {reviews.map((review, index) => (
        <div
          style={{ cursor: 'pointer' }}
          key={index}
          onClick={() => onReviewClick(String(Number(review.id)))}
        >
          <ReviewItem review={review} mergeRequest={mergeRequest} />
        </div>
      ))}
    </div>
  );
};

export default ReviewList;
