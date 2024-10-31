import { useState } from 'react';

interface TextReferenceProps {
  id: number;
  content: string;
  onEdit: (id: number, content: string) => void;
  onCancel: () => void;
}

const TextReference = ({ id, content, onEdit, onCancel }: TextReferenceProps) => {
  const [editedContent, setEditedContent] = useState(content);

  const handleSubmit = () => {
    onEdit(id, editedContent);
    onCancel();
  };

  return (
    <div>
      <textarea
        value={editedContent}
        onChange={(e) => setEditedContent(e.target.value)}
        className="w-full min-h-[200px] p-2 border rounded"
      />
      <div className="flex justify-end gap-2 mt-2">
        <button onClick={onCancel} className="px-3 py-1 border rounded">
          취소
        </button>
        <button onClick={handleSubmit} className="px-3 py-1 bg-primary text-white rounded">
          저장
        </button>
      </div>
    </div>
  );
};

export default TextReference;
