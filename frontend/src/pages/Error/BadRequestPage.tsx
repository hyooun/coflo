import { NavigationButtons } from '@components/Button/NavigationButtons';
import { TerminalWindow } from '@components/Common/TerminalWindow';
import { AlertCircle } from 'lucide-react';

export const BadRequestPage = () => {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4">
      <div className="max-w-2xl w-full">
        <TerminalWindow title="request.log">
          <div className="text-gray-400">$ curl -X POST api.coFlo.dev/review</div>
          <div className="text-red-400 mt-2">
            Error 400: Bad Request
            <br />
            {`{`}
            <br />
            &nbsp;&nbsp;"status": "error",
            <br />
            &nbsp;&nbsp;"message": "Invalid request parameters",
            <br />
            &nbsp;&nbsp;"details": "Required field 'repository_url' is missing"
            <br />
            {`}`}
          </div>
          <div className="flex items-center gap-2 text-gray-400 mt-4">
            <span className="animate-pulse">▋</span>
          </div>
        </TerminalWindow>

        <div className="text-center">
          <AlertCircle className="w-16 h-16 text-primary-500 mx-auto mb-4" />
          <h1 className="text-4xl font-bold mb-4">400: Bad Request</h1>
          <div className="text-gray-600 mb-8 max-w-md mx-auto">
            <p>요청에 필요한 정보가 올바르지 않습니다.</p>
            <p>입력 정보를 확인하고 다시 시도해주세요.</p>
          </div>

          <NavigationButtons />
        </div>
      </div>
    </div>
  );
};
