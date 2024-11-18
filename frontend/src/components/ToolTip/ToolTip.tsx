export const Tooltip = ({ text, children }: TooltipProps) => {
  return (
    <div className="relative inline-block">
      {children}
      <span className="absolute bottom-full left-1/2 transform -translate-x-1/2 mb-2 p-2 min-w-[140px] h-8 flex items-center justify-center text-white bg-primary-500 rounded opacity-0 transition-opacity duration-200 ease-in-out tooltip">
        <span className="text-[10px]">{text}</span>
      </span>
    </div>
  );
};
