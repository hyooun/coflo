import ReactMarkdown from 'react-markdown';
import { ReviewCommentProps } from 'types/review.ts';

export default function ReviewComment({
  name,
  title,
  content,
  type,
  createdAt,
  reviewer,
  backgroundColor = 'bg-white',
}: ReviewCommentProps) {
  return (
    <div className="flex flex-col">
      <div
        className={`flex items-center gap-2 p-2 border-b-2 border-secondary rounded-t-md ${backgroundColor} flex-shrink-0`}
      >
        {name && (
          <>
            <span className="font-bold">{name}</span>
            <span className="text-gray-700">left a comment</span>
          </>
        )}

        {title && (
          <span className={`${type === 'code' ? 'font-SFMono text-sm' : ''} font-bold`}>
            {title}
          </span>
        )}
      </div>
      {/* 코멘트 내용 */}
      <div className={` p-4 ${type === 'code' ? 'bg-secondary/30 font-SFMono text-sm' : ''}`}>
        {reviewer && (
          <div className="flex items-center gap-2 mb-2">
            <img src={reviewer.avatarUrl} alt={reviewer.name} className="w-6 h-6 rounded-full" />
            <span className="font-bold">{reviewer.name}</span>
            <span className="text-gray-600">
              @{reviewer.username} {createdAt}
            </span>
          </div>
        )}

        {type ? (
          <div
            className={`${type === 'code' ? 'overflow-auto max-h-[220px]' : 'overflow-y-auto max-h-[220px]'}`}
          >
            <ReactMarkdown className={`${type ? '' : 'prose'} max-w-none whitespace-pre-wrap`}>
              {content}
            </ReactMarkdown>
          </div>
        ) : (
          <ReactMarkdown className="prose max-w-none whitespace-pre-wrap">{content}</ReactMarkdown>
        )}
      </div>
    </div>
  );
}
