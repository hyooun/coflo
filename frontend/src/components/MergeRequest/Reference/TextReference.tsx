interface TextReferenceProps {
  id?: number;
  content: string;
  onEdit: (content: string) => void;
  onCancel: () => void;
  maxLength?: number;
}

const TextReference = ({ content, onEdit, maxLength }: TextReferenceProps) => {
  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const value = e.target.value;
    if (maxLength && value.length > maxLength) {
      return;
    }
    onEdit(value);
  };

  return (
    <div className="relative w-full">
      <textarea
        value={content}
        onChange={handleChange}
        className="w-full min-h-[200px] h-auto p-2 pb-8 rounded-lg resize-none max-h-[420px] overflow-y-auto focus:outline-none"
        maxLength={maxLength}
      />
      {maxLength && (
        <div
          className={`absolute bottom-2 right-2 text-sm px-1 ${content.length >= maxLength ? 'text-primary-500' : 'text-gray-600'}`}
        >
          {content.length} / {maxLength}
        </div>
      )}
    </div>
  );
};

export default TextReference;
