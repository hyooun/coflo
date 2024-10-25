import { useEffect } from 'react';
import Editor, { useMonaco } from '@monaco-editor/react';
import { CODE_SNIPPETS } from '@constants/codeEditor';
import { useState } from 'react';
import LanguageSelector from './LanguageSeletor';

interface CodeQueryEditorProps {
  defaultValue?: string;
  defaultLanguage?: string;
  height?: string;
  width?: string;
}

const CodeEditor = ({
  defaultLanguage = 'java',
  height = '400px',
  width = '100%',
}: CodeQueryEditorProps) => {
  const monacoInstance = useMonaco();
  const [value, setValue] = useState(CODE_SNIPPETS[defaultLanguage] || '');
  const [language, setLanguage] = useState('javascript');

  useEffect(() => {
    if (!monacoInstance) return;

    monacoInstance.editor.setTheme('vs-dark');
  }, [monacoInstance]);

  useEffect(() => {
    setValue(CODE_SNIPPETS[language] || '');
  }, [language]);

  const handleLanguageChange = (lang: string) => {
    setLanguage(lang);
  };
  return (
    <div className="flex flex-col w-full">
      <LanguageSelector language={language} onSelect={handleLanguageChange} />
      <Editor
        height={height}
        width={width}
        defaultLanguage={defaultLanguage}
        language={language}
        defaultValue={value}
        value={value}
        theme="vs-dark"
        options={{
          minimap: {
            enabled: false,
          },
        }}
      />
    </div>
  );
};

export default CodeEditor;
