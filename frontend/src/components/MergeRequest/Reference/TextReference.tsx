interface TextReferenceProps {
  id?: number;
  content: string;
  onEdit: (content: string) => void;
  onCancel: () => void;
}

const TextReference = ({ content, onEdit }: TextReferenceProps) => {
  return (
    <textarea
      value={content}
      onChange={(e) => onEdit(e.target.value)}
      className="w-full min-h-[200px] h-auto p-2 border rounded resize-none max-h-[420px] overflow-y-auto focus:outline-none"
    />
  );
};

export default TextReference;
