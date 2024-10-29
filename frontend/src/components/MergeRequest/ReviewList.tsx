// components/MergeRequestDetail/ReviewList.tsx
import { Review } from 'types/review.ts';
import ReactMarkdown from 'react-markdown';

const ReviewList = ({ reviews }: { reviews: Review[] }) => {
  return (
    <div className=" font-pretendard">
      {reviews.map((review, index) => (
        <div key={index} className="relative pl-6 border-l-2 py-6 border-secondary">
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
              <span className="text-gray-700">{review.createdAt}</span>
            </div>
          </div>

          {/* 메인 리뷰 코멘트 - 깃 트리와 살짝 겹치게 */}
          <div className="bg-white rounded-t-lg border-2 border-secondary rounded-lg -ml-10">
            {/* 리뷰 헤더 */}
            <div className="flex items-center gap-2 p-2 border-b-2 border-secondary bg-white rounded-t-lg">
              <span className="font-bold ">{review.reviewer.name}</span>
              <span className="text-gray-700"> left a comment</span>
            </div>
            {/* 리뷰 내용 */}
            <div className="p-4">
              <ReactMarkdown className="text-black whitespace-pre-wrap">
                {review.content}
              </ReactMarkdown>
            </div>
          </div>

          {/* 코드 리뷰 코멘트들 - 깃 트리 오른쪽에 표시 */}
          <div className="mt-4 space-y-4">
            {review.comments.map((comment) => (
              <div className="border-2 border-secondary rounded-lg">
                <div className="border-b-2 border-secondary bg-secondary/30 rounded-t-lg p-2 text-sm font-medium">
                  <span className="font-bold mr-1">{review.reviewer.name}</span>
                  <span className="text-gray-700">left a comment</span>
                </div>
                <div className="p-4">
                  <div className="flex items-center gap-2 mb-2">
                    <img
                      src={comment.reviewer.avatarUrl}
                      alt={comment.reviewer.name}
                      className="w-6 h-6 rounded-full"
                    />
                    <span className="font-bold">{comment.reviewer.name}</span>
                    <span className="text-gray-600">
                      @{comment.reviewer.username} {review.createdAt}
                    </span>
                  </div>
                  <ReactMarkdown className="text-gray-700">{comment.content}</ReactMarkdown>
                </div>
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
};

export default ReviewList;
