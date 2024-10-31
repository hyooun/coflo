import { useState } from 'react';
import CodeEditor from '@components/CodeEditor/CodeEditor';

interface CodeReferenceProps {
  id: number;
  content: string;
  language?: string;
  onEdit: (id: number, content: string) => void;
  onCancel: () => void;
}

const CodeReference = ({
  id,
  content,
  language = 'javascript',
  onEdit,
  onCancel,
}: CodeReferenceProps) => {
  const [editedContent, setEditedContent] = useState(content);

  const handleSubmit = () => {
    onEdit(id, editedContent);
    onCancel();
  };

  return (
    <div>
      <div className="mb-2">
        <CodeEditor
          defaultValue={editedContent}
          defaultLanguage={language}
          height="200px"
          width="100%"
          //   onChange={(value) => setEditedContent(value || '')}
        />
      </div>
      <div className="flex justify-end gap-2">
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

export default CodeReference;
