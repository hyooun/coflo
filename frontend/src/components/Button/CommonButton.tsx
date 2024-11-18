export function CommonButton({
  active,
  icon,
  onClick,
  children,
  className,
  bgColor = 'bg-primary-500',
  hoverColor = 'hover:bg-primary-600',
  disabled = false,
  style,
}: BtnProps) {
  const currentBgColor = disabled ? 'bg-gray-700' : active ? bgColor : 'bg-primary-500';

  const currentHoverColor = disabled ? '' : active ? hoverColor : 'hover:bg-primary-400';

  return (
    <div
      style={style}
      className={`${className} font-pretendard flex flex-row items-center rounded-3xl
        ${!style && currentBgColor} ${!style && currentHoverColor} transition-colors duration-200
        ${disabled ? 'cursor-not-allowed opacity-50' : 'cursor-pointer'}`}
      onClick={disabled ? undefined : onClick}
    >
      {icon && <div className="pr-2">{icon}</div>}
      <div className="flex-1 text-center">{children}</div>
    </div>
  );
}
