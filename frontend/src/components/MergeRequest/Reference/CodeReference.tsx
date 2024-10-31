import CodeEditor from '@components/CodeEditor/CodeEditor';

interface CodeReferenceProps {
  id?: number;
  content: string;
  language: string;
  onEdit: (content: string) => void;
  onCancel: () => void;
}

const CodeReference = ({ content, language, onEdit }: CodeReferenceProps) => {
  return (
    <CodeEditor
      defaultValue={content}
      defaultLanguage={language}
      language={language}
      height="400px"
      width="100%"
      onChange={(value) => onEdit(value || '')}
      isLanguageSelectable={false}
    />
  );
};

export default CodeReference;
