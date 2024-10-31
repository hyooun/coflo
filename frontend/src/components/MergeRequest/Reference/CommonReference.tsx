import { useState } from 'react';

import CodeReference from './CodeReference';
import TextReference from './TextReference';
import { CommonReferenceProps } from 'types/review.ts';
import { PencilIcon } from '@components/TextDiv/Icons/PencilIcon';
import { TrashIcon } from '@components/TextDiv/Icons/TrashIcon';

const CommonReference = ({
  id,
  fileName,
  content,
  type,
  onEdit,
  onDelete,
}: CommonReferenceProps) => {
  const [isEditing, setIsEditing] = useState(false);

  return (
    <div className="rounded-lg border-2 border-secondary">
      <div className="flex justify-between items-center p-2 bg-white rounded-t-lg border-b-2 border-secondary">
        <h3 className={`font-bold ${type === 'CODE' ? 'font-SFMono text-sm' : ''}`}>{fileName}</h3>
        <div className="flex gap-2">
          <button onClick={() => setIsEditing(!isEditing)}>
            <PencilIcon className="w-4 h-4 text-primary-500" />
          </button>
          <button onClick={() => onDelete(id)}>
            <TrashIcon className="w-4 h-4 text-primary-500" />
          </button>
        </div>
      </div>
      <div className="">
        {isEditing ? (
          type === 'CODE' ? (
            <CodeReference
              id={id}
              content={content}
              onEdit={onEdit}
              onCancel={() => setIsEditing(false)}
            />
          ) : (
            <TextReference
              id={id}
              content={content}
              onEdit={onEdit}
              onCancel={() => setIsEditing(false)}
            />
          )
        ) : (
          <div className={`${type === 'CODE' ? 'font-SFMono bg-secondary/30 ' : ''} p-4`}>
            {content}
          </div>
        )}
      </div>
    </div>
  );
};

export default CommonReference;
