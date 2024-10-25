export function CommonButton({ active, icon, onClick, children, className }: BtnProps) {
  const bgColor = active ? 'bg-primary-500' : 'bg-primary-500';
  const textColor = active ? 'text-white' : 'text-white';

  return (
    <div
      className={`${className} font-pretendard flex flex-row items-center rounded-3xl ${bgColor} ${textColor}  hover:cursor-pointer`}
      onClick={onClick}
    >
      {icon && <div className="pr-2">{icon}</div>}
      <div className="flex-1 text-center">{children}</div>
    </div>
  );
}
