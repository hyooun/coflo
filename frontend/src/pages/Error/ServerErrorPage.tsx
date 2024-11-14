import { NavigationButtons } from '@components/Button/NavigationButtons';
import { TerminalWindow } from '@components/Common/TerminalWindow';
import { ServerCrash } from 'lucide-react';

export const ServerErrorPage = () => {
  return (
    <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4">
      <div className="max-w-2xl w-full">
        <TerminalWindow title="server.log">
          <div className="text-gray-400">$ systemctl status coFlo-server</div>
          <div className="text-red-400 mt-2">
            ● coFlo-server.service - coFlo API Server
            <br />
            Loaded: loaded (/etc/systemd/system/coFlow.service)
            <br />
            Active: failed (Result: exit-code)
          </div>
          <div className="text-yellow-400 mt-2">
            Error: Internal Server Error (500)
            <br />
            - Server is temporarily unavailable
            <br />- Our team has been notified
          </div>
          <div className="flex items-center gap-2 text-gray-400 mt-4">
            <span className="animate-pulse">▋</span>
          </div>
        </TerminalWindow>

        <div className="text-center">
          <ServerCrash className="w-16 h-16 text-primary-500 mx-auto mb-4" />
          <h1 className="text-4xl font-bold mb-4">500: Server Error</h1>
          <div className="text-gray-600 mb-8 max-w-md mx-auto">
            <p>서버에 일시적인 문제가 발생했습니다.</p>
            <p>잠시 후 다시 시도해주세요.</p>
          </div>

          <NavigationButtons />
        </div>
      </div>
    </div>
  );
};
