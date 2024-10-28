export function CommonButton({
  active,
  icon,
  onClick,
  children,
  className,
  bgColor = 'bg-primary-500',
}: BtnProps) {
  const currentBgColor = active ? bgColor : 'bg-primary-300';
  const currentHoverColor = active ? 'bg-primary-500' : 'hover:bg-primary-400';

  return (
    <div
      className={`${className} font-pretendard flex flex-row items-center rounded-3xl ${currentBgColor} ${currentHoverColor} text-white transition-colors duration-200`}
      onClick={onClick}
    >
      {icon && <div className="pr-2">{icon}</div>}
      <div className="flex-1 text-center">{children}</div>
    </div>
  );
}
