const IconWithText = ({ svg, text, count }: IconWithTextProps) => {
  return (
    <div className="flex items-center w-40 h-5">
      <div className="mr-2">{svg}</div>
      <div className="text-sm font-semibold text-[#172b4d]  mr-2">
        {count}
        <span className="ml-1">{text}</span>
      </div>
    </div>
  );
};

export default IconWithText;
