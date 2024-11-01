import { useEffect } from 'react';
import Editor, { useMonaco } from '@monaco-editor/react';
import { CODE_SNIPPETS, LANGUAGE_VERSIONS } from '@constants/codeEditor';
import { useState } from 'react';
import { Code2, Copy, Check } from 'lucide-react';
import CommonSelector from '@components/Common/CommonSelector';

interface CodeQueryEditorProps {
  defaultValue?: string;
  defaultLanguage: string;
  language?: string;
  height?: string;
  width?: string;
  onChange?: (value: string | undefined) => void;
  onLanguageChange?: (language: string) => void;
  isLanguageSelectable?: boolean;
}

const CodeEditor = ({
  defaultValue = '',
  defaultLanguage = 'javascript',
  language,
  height = '400px',
  width = '100%',
  onChange,
  onLanguageChange,
  isLanguageSelectable = true,
}: CodeQueryEditorProps) => {
  const monacoInstance = useMonaco();
  const [value, setValue] = useState(defaultValue || CODE_SNIPPETS[defaultLanguage]);
  const [selectedLanguage, setSelectedLanguage] = useState(language || defaultLanguage);
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
      setTimeout(() => setIsCopied(false), 1000);
    } catch (err) {
      console.error('Failed to copy code:', err);
    }
  };

  const handleSelectLanguage = (lang: string) => {
    setValue(CODE_SNIPPETS[lang] || '');
    onChange?.(CODE_SNIPPETS[lang] || '');
    setSelectedLanguage(lang);
    onLanguageChange?.(lang);
  };

  return (
    <div className="group relative flex flex-col w-full bg-black overflow-hidden max-h-[420px]">
      <div className="flex justify-between items-center px-4">
        <div className="flex items-center gap-2">
          <Code2 className="w-4 h-4 text-gray-500" />
          {isLanguageSelectable ? (
            <>
              <CommonSelector<string>
                selectedItem={selectedLanguage}
                items={Object.keys(LANGUAGE_VERSIONS)}
                onSelect={handleSelectLanguage}
                displayValue={(lang) => lang}
                searchPlaceholder="언어 검색..."
                isEqual={(item1, item2) => item1 === item2}
              />
            </>
          ) : (
            <span className="text-sm font-medium text-gray-600">{language || defaultLanguage}</span>
          )}
        </div>
        <button
          onClick={handleCopyCode}
          className="flex items-center gap-1.5 text-sm text-gray-500 hover:text-white transition-colors px-2 rounded-lg hover:bg-secondary"
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
      <div className="w-full overflow-hidden">
        <Editor
          height={height}
          width={width}
          defaultLanguage={defaultLanguage}
          language={language || defaultLanguage}
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
            fixedOverflowWidgets: true,
          }}
          className="max-w-full"
        />
      </div>
    </div>
  );
};

export default CodeEditor;
