// components/MergeRequestDetail/ReviewList.tsx
import { Review } from 'types/review.ts';
import ReviewItem from './ReviewItem';

const ReviewList = ({ reviews }: { reviews: Review[] }) => {
  return (
    <div className="font-pretendard flex-[4] pl-4 min-w-[500px]">
      {reviews.map((review, index) => (
        <ReviewItem key={index} review={review} />
      ))}
    </div>
  );
};

export default ReviewList;
