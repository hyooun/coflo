import { Home } from 'lucide-react';

import { TerminalWindow } from '@components/Common/TerminalWindow';
import { RefreshCcw } from 'lucide-react';
import { ShieldAlert } from 'lucide-react';

export const UnauthorizedPage = () => {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4">
      <div className="max-w-2xl w-full">
        <TerminalWindow title="auth.log">
          <div className="text-gray-400">$ coFlo auth --check-token</div>
          <div className="text-red-400 mt-2">
            Error 401: Unauthorized
            <br />
            Authentication failed: Invalid or expired token
          </div>
          <div className="text-yellow-400 mt-2">
            Please ensure you:
            <br />
            1. Have a valid account
            <br />
            2. Have granted necessary permissions
            <br />
            3. Are using a valid access token
          </div>
          <div className="flex items-center gap-2 text-gray-400 mt-4">
            <span className="animate-pulse">▋</span>
          </div>
        </TerminalWindow>

        <div className="text-center">
          <ShieldAlert className="w-16 h-16 text-primary-500 mx-auto mb-4" />
          <h1 className="text-4xl font-bold mb-4">401: Unauthorized</h1>
          <p className="text-gray-600 mb-8 max-w-md mx-auto">
            인증에 실패했습니다. 다시 로그인해주세요.
          </p>

          <div className="flex gap-4 justify-center">
            <button
              onClick={() => (window.location.href = '/login')}
              className="inline-flex items-center gap-2 px-6 py-3 rounded-full 
                  bg-primary-500 text-white hover:bg-primary-600 transition-colors"
            >
              <RefreshCcw className="w-5 h-5" />
              다시 로그인
            </button>
            <button
              onClick={() => (window.location.href = '/')}
              className="inline-flex items-center gap-2 px-6 py-3 rounded-full 
                  border border-gray-300 text-gray-700 hover:bg-gray-100 transition-colors"
            >
              <Home className="w-5 h-5" />
              홈으로 가기
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
