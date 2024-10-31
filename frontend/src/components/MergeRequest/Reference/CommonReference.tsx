import { useState } from 'react';

import CodeReference from './CodeReference';
import TextReference from './TextReference';
import { Pencil, Trash2 } from 'lucide-react';
import { CommonReferenceProps } from 'types/review.ts';

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
      <div className="flex justify-between items-center p-4 bg-white rounded-t-lg border-b-2 border-secondary">
        <h3 className="font-bold">{fileName}</h3>
        <div className="flex gap-2">
          <button onClick={() => setIsEditing(!isEditing)}>
            <Pencil className="w-4 h-4" />
          </button>
          <button onClick={() => onDelete(id)}>
            <Trash2 className="w-4 h-4" />
          </button>
        </div>
      </div>
      <div className="p-4">
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
          <div className={type === 'CODE' ? 'font-mono bg-gray-50 p-2' : ''}>{content}</div>
        )}
      </div>
    </div>
  );
};

export default CommonReference;
