// 터미널 스타일 컴포넌트
export const TerminalWindow = ({
  children,
  title = 'terminal',
}: {
  children: React.ReactNode;
  title?: string;
}) => (
  <div className="bg-gray-900 rounded-lg overflow-hidden shadow-xl mb-8">
    <div className="flex items-center gap-2 bg-gray-800 px-4 py-2">
      <div className="w-3 h-3 rounded-full bg-red-500"></div>
      <div className="w-3 h-3 rounded-full bg-yellow-500"></div>
      <div className="w-3 h-3 rounded-full bg-green-500"></div>
      <span className="text-gray-400 text-sm ml-2">{title}</span>
    </div>
    <div className="p-6 font-mono">{children}</div>
  </div>
);
