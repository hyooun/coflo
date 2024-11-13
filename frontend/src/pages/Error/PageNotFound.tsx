import { Terminal, Home, ArrowLeft } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const PageNotFound = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4">
      <div className="max-w-2xl w-full">
        {/* Terminal Style Error Display */}
        <div className="bg-gray-900 rounded-lg overflow-hidden shadow-xl mb-8">
          <div className="flex items-center gap-2 bg-gray-800 px-4 py-2">
            <div className="w-3 h-3 rounded-full bg-red-500"></div>
            <div className="w-3 h-3 rounded-full bg-yellow-500"></div>
            <div className="w-3 h-3 rounded-full bg-green-500"></div>
            <span className="text-gray-400 text-sm ml-2">terminal</span>
          </div>
          <div className="p-6 font-mono">
            <div className="text-gray-400">$ curl coflo.co.kr/page</div>
            <div className="text-red-400 mt-2">Error 404: Page Not Found</div>
            <div className="text-yellow-400 mt-2">{">>> Page you're looking for might be:"}</div>
            <div className="text-gray-400 mt-2">
              1. Moved to a different location
              <br />
              2. Deleted or never existed
              <br />
              3. Having a coffee break ☕
            </div>
            <div className="flex items-center gap-2 text-gray-400 mt-4">
              <span className="animate-pulse">▋</span>
            </div>
          </div>
        </div>

        {/* Error Message and Navigation */}
        <div className="text-center">
          <Terminal className="w-16 h-16 text-primary-500 mx-auto mb-4" />
          <h1 className="text-4xl font-bold mb-4">404: Route Not Found</h1>
          <div className="text-gray-600 mb-8 max-w-md mx-auto">
            <p>이 페이지는 존재하지 않거나 삭제되었을 수 있습니다.</p>
            <p>홈으로 돌아가서 다시 시도해보세요.</p>
          </div>

          <div className="flex gap-4 justify-center">
            <button
              onClick={() => navigate(-1)}
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
        </div>

        {/* Git Command Suggestion */}
        {/* <div className="mt-12 text-center">
          <div className="inline-block bg-gray-100 rounded-lg px-6 py-4 font-mono text-sm text-gray-700">
            <span className="text-gray-400"># Try this:</span>
            <br />
            git checkout main
            <br />
            git pull origin main
          </div>
        </div> */}
      </div>
    </div>
  );
};

export default PageNotFound;
