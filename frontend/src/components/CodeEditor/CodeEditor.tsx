import { useEffect } from 'react';
import Editor, { useMonaco } from '@monaco-editor/react';
import { CODE_SNIPPETS } from '@constants/codeEditor';
import { useState } from 'react';
import LanguageSelector from './LanguageSelector';
import { Code2, Copy, Check } from 'lucide-react';

interface CodeQueryEditorProps {
  defaultValue?: string;
  defaultLanguage?: string;
  height?: string;
  width?: string;
  onChange?: (value: string | undefined) => void;
}

const CodeEditor = ({
  defaultValue = '',
  defaultLanguage = 'javascript',
  height = '400px',
  width = '100%',
  onChange,
}: CodeQueryEditorProps) => {
  const monacoInstance = useMonaco();
  const [value, setValue] = useState(defaultValue || CODE_SNIPPETS[defaultLanguage] || '');
  const [language, setLanguage] = useState(defaultLanguage);
  const [isCopied, setIsCopied] = useState(false);

  useEffect(() => {
    if (!monacoInstance) return;
    monacoInstance.editor.setTheme('vs-dark');
  }, [monacoInstance]);

  const handleChange = (newValue: string | undefined) => {
    setValue(newValue || '');
    onChange?.(newValue);
  };

  const handleCopyCode = async () => {
    try {
      await navigator.clipboard.writeText(value);
      setIsCopied(true);
      setTimeout(() => setIsCopied(false), 2000);
    } catch (err) {
      console.error('Failed to copy code:', err);
    }
  };

  return (
    <div className="group relative flex flex-col w-full bg-black">
      <div className="flex justify-between items-center px-4 py-2">
        <div className="flex items-center gap-2">
          <Code2 className="w-4 h-4 text-gray-500" />
          <LanguageSelector language={language} onSelect={setLanguage} />
        </div>
        <button
          onClick={handleCopyCode}
          className="flex items-center gap-1.5 text-sm text-gray-500 hover:text-white transition-colors px-2 py-1 rounded hover:bg-secondary"
        >
          {isCopied ? (
            <>
              <Check className="w-4 h-4" />
              <span>복사됨</span>
            </>
          ) : (
            <>
              <Copy className="w-4 h-4" />
              <span>복사</span>
            </>
          )}
        </button>
      </div>
      <Editor
        height={height}
        width={width}
        defaultLanguage={defaultLanguage}
        language={language}
        defaultValue={value}
        value={value}
        onChange={handleChange}
        theme="vs-dark"
        options={{
          minimap: { enabled: false },
          padding: { top: 16, bottom: 16 },
          fontSize: 14,
          lineHeight: 1.5,
          scrollBeyondLastLine: false,
          wordWrap: 'on',
          wrappingIndent: 'indent',
          automaticLayout: true,
          readOnly: false,
          fontFamily: 'Monaco, Menlo, "Ubuntu Mono", Consolas, source-code-pro, monospace',
          renderLineHighlight: 'none',
          scrollbar: {
            vertical: 'hidden',
            horizontal: 'hidden',
          },
        }}
      />
    </div>
  );
};

export default CodeEditor;
