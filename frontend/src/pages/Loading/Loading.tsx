interface LoadingProps {
  isLoading: boolean;
}

const Loading = ({ isLoading }: LoadingProps) => {
  if (!isLoading) return null;

  return (
    <div className="fixed inset-0 flex items-center justify-center bg-white z-50 transition-opacity duration-300">
      <div className="relative">
        {/* 물결 효과를 위한 여러 원 */}
        <div className="absolute inset-0 flex items-center justify-center">
          <div className="wave-circle animate-wave1"></div>
          <div className="wave-circle animate-wave2"></div>
          <div className="wave-circle animate-wave3"></div>
        </div>
        {/* 중앙 로고 또는 아이콘 */}
        <div className="relative z-10 w-12 h-12 bg-primary-500 rounded-full"></div>
      </div>

      <style>
        {`
          .wave-circle {
            position: absolute;
            border: 2px solid #6366f1;
            border-radius: 50%;
            opacity: 0;
            transition: opacity 0.3s ease-in-out;
          }

          @keyframes wave {
            0% {
              width: 48px;
              height: 48px;
              opacity: 0.8;
            }
            100% {
              width: 120px;
              height: 120px;
              opacity: 0;
            }
          }

          .animate-wave1 {
            animation: wave 2s infinite cubic-bezier(0.4, 0, 0.2, 1);
          }

          .animate-wave2 {
            animation: wave 2s infinite cubic-bezier(0.4, 0, 0.2, 1);
            animation-delay: 0.6s;
          }

          .animate-wave3 {
            animation: wave 2s infinite cubic-bezier(0.4, 0, 0.2, 1);
            animation-delay: 1.2s;
          }
        `}
      </style>
    </div>
  );
};

export default Loading;
