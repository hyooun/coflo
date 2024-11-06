import { useState } from 'react';
import PrevPrompt from './PrevPrompt';
import AlertModal from '@components/Modal/AlertModal';
import { customPrompt } from '@apis/CustomPrompt';
import { useNavigate } from 'react-router-dom';

const CustomPromptContainer = () => {
  const [content, setContent] = useState<string>('');
  const [isAlertModalOpen, setIsAlertModalOpen] = useState<boolean>(false);
  const [alertMessage, setAlertMessage] = useState<string[]>([]);
  const maxLength = 1000;
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const value = e.target.value;
    setContent(value);
  };

  const handleSavePrompt = async () => {
    if (maxLength && content.length > maxLength) {
      setIsAlertModalOpen(true);
      setAlertMessage(['최대 글자 수를 초과하였습니다.']);
      return;
    }

    const projectId = '1';

    const response = await customPrompt.updateCustomPrompt(projectId, { promptText: content });

    if (response.status === 'SUCCESS') {
      setIsAlertModalOpen(true);
      setAlertMessage(['프롬프트가 성공적으로 저장되었습니다.']);
      navigate(0);
    }
  };
  return (
    <>
      <div className="flex flex-col gap-6 min-w-[600px]">
        <div className="relative w-full border-2 border-primary-500 rounded-xl p-4">
          <PrevPrompt />
          <textarea
            className="w-full min-h-[400px] h-auto max-h-[420px] p-4 pb-8 rounded-2xl resize-none overflow-y-auto focus:outline-none bg-secondary text-white"
            value={content}
            onChange={handleChange}
            placeholder="원하는 프롬프트를 입력해주세요."
          />
          {maxLength && (
            <div
              className={`absolute bottom-6 right-5 text-sm px-1 ${content.length >= maxLength ? 'text-state-warning bg-white rounded-2xl' : 'text-gray-600'}`}
            >
              {content.length} / {maxLength}
            </div>
          )}
        </div>
        <div className="flex justify-end">
          <button
            className="bg-primary-500 text-white px-4 py-2 rounded-xl w-fit"
            onClick={handleSavePrompt}
          >
            저장
          </button>
        </div>
      </div>
      {isAlertModalOpen && (
        <AlertModal content={alertMessage} onConfirm={() => setIsAlertModalOpen(false)} />
      )}
    </>
  );
};

export default CustomPromptContainer;
