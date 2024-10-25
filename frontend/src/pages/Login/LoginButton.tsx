interface LoginButtonProps {
  imageName: string;
  buttonText: string;
  bgColor: string;
  onClick: () => void; // 로그인 요청을 처리할 함수
}

export default function LoginButton({
  imageName,
  buttonText,
  bgColor,
  onClick,
}: LoginButtonProps) {
  return (
    <button
      type="button"
      className={`w-full py-3 flex items-center justify-center relative font-bold rounded-3xl transition-colors h-14 ${bgColor} shadow-md`}
      onClick={onClick}
    >
      <img
        src={`/images/logo/${imageName}`}
        alt={`${buttonText} 로고`}
        className="absolute left-8 w-4 h-4 sm:w-4 sm:h-4 sm:absolute sm:left-8"
      />
      <p className="ml-6 block sm:hidden">{buttonText}</p>
    </button>
  );
}
