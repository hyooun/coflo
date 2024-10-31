import CodeEditor from '@components/CodeEditor/CodeEditor';

interface CodeReferenceProps {
  id?: number;
  content: string;
  language?: string;
  onEdit: (content: string) => void;
  onCancel: () => void;
}

const CodeReference = ({ content, language = 'javascript', onEdit }: CodeReferenceProps) => {
  return (
    <CodeEditor
      defaultValue={content}
      defaultLanguage={language}
      height="400px"
      width="100%"
      onChange={(value) => onEdit(value || '')}
    />
  );
};

export default CodeReference;
