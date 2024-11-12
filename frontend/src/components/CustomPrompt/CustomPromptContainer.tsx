import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAtom } from 'jotai';
import PrevPrompt from './PrevPrompt';
import AlertModal from '@components/Modal/AlertModal';
import { customPrompt } from '@apis/CustomPrompt';
import { projectIdAtom } from '@store/auth';

const CustomPromptContainer = () => {
  const [content, setContent] = useState<string>('');
  const [isAlertModalOpen, setIsAlertModalOpen] = useState<boolean>(false);
  const [alertMessage, setAlertMessage] = useState<string[]>([]);
  const [projectId] = useAtom(projectIdAtom);
  const maxLength = 1000;
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const value = e.target.value;
    const normalizedValue = value.replace(/\n+/g, '\n');

    if (normalizedValue.length <= maxLength * 3) {
      setContent(normalizedValue);
    }
  };

  const handleSavePrompt = async () => {
    let trimmedContent = content;
    if (maxLength && content.length > maxLength) {
      trimmedContent = content.slice(0, maxLength);
      setContent(trimmedContent);
      return;
    }

    if (!projectId) return;

    const response = await customPrompt.updateCustomPrompt(projectId, { content: trimmedContent });

    if (response.status === 'SUCCESS') {
      setIsAlertModalOpen(true);
      setAlertMessage(['프롬프트가 성공적으로 저장되었습니다.']);
      navigate(0);
    }
  };

  return (
    <>
      <div className="flex flex-col gap-6 min-w-[600px]">
        <div className="relative w-full border-1 border-primary-500 shadow-lg rounded-xl p-4 bg-[#EFF2FB] min-h-[300px]">
          <PrevPrompt />
        </div>
        <textarea
          className="w-full min-h-[400px] h-auto max-h-[420px] border-1 border-primary-500 shadow-lg p-4 pb-8 rounded-2xl resize-none overflow-y-auto focus:outline-none bg-[#F5F7FA] placeholder:text-[#A5A5A5]"
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
      <div className="flex mt-5 justify-end">
        <button
          className="bg-primary-500 text-white px-4 py-2 rounded-xl w-fit"
          onClick={handleSavePrompt}
        >
          저장
        </button>
      </div>

      {isAlertModalOpen && (
        <AlertModal content={alertMessage} onConfirm={() => setIsAlertModalOpen(false)} />
      )}
    </>
  );
};

export default CustomPromptContainer;
