// components/MergeRequestDetail/ReviewList.tsx
import { Review } from 'types/review.ts';
import ReviewItem from './ReviewItem';

const ReviewList = ({ reviews }: { reviews: Review[] }) => {
  return (
    <div className="font-pretendard w-[700px]">
      {reviews.map((review, index) => (
        <ReviewItem key={index} review={review} />
      ))}
    </div>
  );
};

export default ReviewList;
