import { useNavigate } from 'react-router-dom';
import { ArrowLeft, Home } from 'lucide-react';

export const NavigationButtons = () => {
  const navigate = useNavigate();

  return (
    <div className="flex gap-4 justify-center">
      <button
        onClick={() => navigate(-2)}
        className="inline-flex items-center gap-2 px-6 py-3 rounded-full 
            border border-gray-300 text-gray-700 hover:bg-gray-100 transition-colors"
      >
        <ArrowLeft className="w-5 h-5" />
        이전 페이지
      </button>
      <button
        onClick={() => navigate('/')}
        className="inline-flex items-center gap-2 px-6 py-3 rounded-full 
            bg-primary-500 text-white hover:bg-primary-600 transition-colors"
      >
        <Home className="w-5 h-5" />
        홈으로 가기
      </button>
    </div>
  );
};
