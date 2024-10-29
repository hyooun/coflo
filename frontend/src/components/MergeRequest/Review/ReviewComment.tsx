import ReactMarkdown from 'react-markdown';
import { ReviewCommentProps } from 'types/review.ts';

export default function ReviewComment({
  name,
  content,
  createdAt,
  reviewer,
  backgroundColor = 'bg-white',
}: ReviewCommentProps) {
  return (
    <div>
      <div
        className={`flex items-center gap-2 p-2 border-b-2 border-secondary rounded-t-md ${backgroundColor}`}
      >
        <span className="font-bold">{name}</span>
        <span className="text-gray-700">left a comment</span>
      </div>
      {/* 코멘트 내용 */}
      <div className="p-4">
        {reviewer && (
          <div className="flex items-center gap-2 mb-2">
            <img src={reviewer.avatarUrl} alt={reviewer.name} className="w-6 h-6 rounded-full" />
            <span className="font-bold">{reviewer.name}</span>
            <span className="text-gray-600">
              @{reviewer.username} {createdAt}
            </span>
          </div>
        )}
        <ReactMarkdown className="prose max-w-none whitespace-pre-wrap">{content}</ReactMarkdown>
      </div>
    </div>
  );
}
