export default function Title({
  title,
  textSize = 'text-sm',
  px = 'px-4',
  py = 'py-4',
  ml = '',
  mt = '',
}: TitleProps) {
  return (
    <div
      className={`flex flex-row font-pretendard items-center justify-between ${px} ${py} ${ml} ${mt}`}
    >
      <div className={`${textSize} font-bold text-[#172b4d]`}>{title}</div>
    </div>
  );
}
