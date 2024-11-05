import { useEffect, useState } from 'react';

interface CustomPrompt {
  customPromptId: number;
  content: string;
}

const PrevPrompt = () => {
  const [prevPrompt, setPrevPrompt] = useState<CustomPrompt | null>(null);

  useEffect(() => {
    const fetchPrevPrompt = async () => {
      const response = await fetch(`/api/custom-prompts/1`);
      const result = await response.json();
      setPrevPrompt(result.data);
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
