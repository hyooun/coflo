import CodeEditor from '@components/CodeEditor/CodeEditor';

interface CodeReferenceProps {
  id?: number;
  content: string;
  language: string;
  onEdit: (content: string) => void;
  onLanguageChange?: (language: string) => void;
  onCancel: () => void;
  maxLength?: number;
}

const CodeReference = ({
  content,
  language,
  onEdit,
  onLanguageChange,
  maxLength,
}: CodeReferenceProps) => {
  return (
    <CodeEditor
      defaultValue={content}
      defaultLanguage={language}
      language={language}
      onChange={(value) => onEdit(value || '')}
      onLanguageChange={onLanguageChange}
      isLanguageSelectable={false}
      maxLength={maxLength}
    />
  );
};

export default CodeReference;
