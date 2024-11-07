import { useEffect, useState } from 'react';
import { customPrompt } from '@apis/CustomPrompt';

interface CustomPrompt {
  customPromptId: string;
  content: string;
}

const PrevPrompt = () => {
  const projectId = '1';
  const [prevPrompt, setPrevPrompt] = useState<CustomPrompt | null>(null);

  useEffect(() => {
    const fetchPrevPrompt = async () => {
      const response = await customPrompt.getCustomPrompt(projectId);
      const data = response.data;
      if (data) {
        setPrevPrompt(data);
      }
    };

    fetchPrevPrompt();
  }, []);

  return (
    <div className="flex flex-col border-b-2 border-primary-500 mb-4">
      <div className="font-bold text-sm text-primary-500">현재 저장된 프롬프트</div>
      <div className="rounded-lg  text-primary-500 p-4 mt-2 mb-4 max-h-[200px] overflow-y-auto">
        <div className="whitespace-pre-wrap">{prevPrompt?.content}</div>
      </div>
    </div>
  );
};

export default PrevPrompt;
