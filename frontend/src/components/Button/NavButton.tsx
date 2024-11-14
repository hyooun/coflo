export function NavButton({ active, icon, children, className }: BtnProps) {
  const bgColor = active ? 'bg-[#ebecf0]' : 'bg-[#F5F7FA]';
  const textColor = active ? 'text-secondary' : 'text-primary-500';
  const fontWeight = active ? 'font-bold' : 'font-normal';

  return (
    <div
      className={`${className} font-pretentard flex flex-row items-center rounded-[3px] w-full h-10 ${bgColor} ${textColor} ${fontWeight} px-3 py-2 select-none hover:cursor-pointer`}
    >
      <div>{icon}</div>
      <div className="pl-3">{children}</div>
    </div>
  );
}
