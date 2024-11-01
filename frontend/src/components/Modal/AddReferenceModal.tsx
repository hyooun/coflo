import { useState } from 'react';
import { X } from 'lucide-react';
import CodeEditor from '@components/CodeEditor/CodeEditor';
import CommonInput from '@components/Input/CommonInput';

interface AddReferenceModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (language: string, content: string, fileName: string) => void;
}

const AddReferenceModal = ({ isOpen, onClose, onSubmit }: AddReferenceModalProps) => {
  const [selectedLanguage, setSelectedLanguage] = useState('JavaScript');
  const [content, setContent] = useState('');
  const [fileName, setFileName] = useState('');
  const [isWarning, setIsWarning] = useState(false);

  if (!isOpen) return null;

  const handleSubmit = () => {
    if (fileName === '' || fileName.length === 0) {
      setIsWarning(true);
      return;
    }
    if (!content.trim()) {
      // TODO: 코드 에디터에 입력된 내용이 없을 경우 경고 띄우기
      return;
    }

    onSubmit(selectedLanguage, content, fileName);
    setFileName('');
    setIsWarning(false);
    onClose();
  };

  const handleFileNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFileName(e.target.value);
    setIsWarning(false);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-70 flex items-center justify-center">
      <div className="bg-white rounded-lg p-6 w-[800px] border-2 border-primary-500">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-2xl font-bold text-primary-500">코드/텍스트 추가</h2>
          <button onClick={onClose}>
            <X className="w-6 h-6 text-primary-500" />
          </button>
        </div>

        <div className="mb-4">
          <CommonInput
            placeholder="제목을 입력해주세요 (ex. 참고자료 1)"
            value={fileName}
            onChange={handleFileNameChange}
            warningMessage="제목을 다시 입력해주세요."
            isWarning={isWarning}
            className="border-b-2 rounded-none "
          />
        </div>

        <div className="mb-4">
          <CodeEditor
            defaultLanguage={selectedLanguage}
            onChange={(value) => setContent(value || '')}
            onLanguageChange={(lang) => setSelectedLanguage(lang)}
          />
        </div>

        <div className="flex justify-end">
          <button
            className="px-4 py-2 bg-primary-500 text-white rounded-2xl"
            onClick={handleSubmit}
          >
            추가
          </button>
        </div>
      </div>
    </div>
  );
};

export default AddReferenceModal;
