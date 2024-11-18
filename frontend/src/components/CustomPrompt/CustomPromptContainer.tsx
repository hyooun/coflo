import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAtom } from 'jotai';
import PrevPrompt from './PrevPrompt';
import AlertModal from '@components/Modal/AlertModal';
import { customPrompt } from '@apis/CustomPrompt';
import { projectIdAtom } from '@store/auth';
import SaveIcon from './icons/SaveIcon';
import { CircleCheck, TriangleAlert } from 'lucide-react';

const CustomPromptContainer = () => {
  const [content, setContent] = useState<string>('');
  const [isAlertModalOpen, setIsAlertModalOpen] = useState<boolean>(false);
  const [alertMessage, setAlertMessage] = useState<string[]>([]);
  const [isSuccess, setIsSuccess] = useState<boolean>(false);
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
      setIsSuccess(true);
      navigate(0);
      return;
    }

    setIsAlertModalOpen(true);
    setAlertMessage(['프롬프트 저장에 실패했습니다.']);
    setIsSuccess(false);
  };

  const isSaveDisabled = content.trim() === '';

  return (
    <>
      <div className="flex flex-col gap-6 min-w-[600px]">
        <div className="relative w-full border-1 border-primary-500 shadow-lg rounded-xl p-4 bg-[#EFF2FB] min-h-[300px]">
          <PrevPrompt />
        </div>
        <div className="relative w-full">
          <textarea
            className="w-full min-h-[400px] h-auto max-h-[420px] border-1 border-primary-500 shadow-lg  p-10 pt-13  rounded-2xl resize-none overflow-y-auto focus:outline-none bg-[#F5F7FA] placeholder:text-[#A5A5A5]"
            value={content}
            onChange={handleChange}
            placeholder="원하는 프롬프트를 입력해주세요."
          />
          {maxLength && (
            <div
              className={`absolute top-5 right-7 text-lg px-1 ${
                content.length >= maxLength
                  ? 'text-state-warning bg-white rounded-2xl'
                  : 'text-gray-600'
              }`}
            >
              {content.length} / {maxLength}
            </div>
          )}
          <button
            className="absolute bottom-5 right-5 p-2 rounded-xl"
            onClick={handleSavePrompt}
            disabled={isSaveDisabled}
          >
            <SaveIcon width={50} height={50} fill={isSaveDisabled ? '#e3e3e3' : '#2C365B'} />
          </button>
        </div>
      </div>

      {isAlertModalOpen && (
        <AlertModal
          content={alertMessage}
          onConfirm={() => setIsAlertModalOpen(false)}
          icon={isSuccess ? CircleCheck : TriangleAlert}
          iconClassName={isSuccess ? 'text-state-success' : 'text-state-warning'}
        />
      )}
    </>
  );
};

export default CustomPromptContainer;
