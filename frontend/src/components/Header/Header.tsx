export default function Header({ title }: HeaderProps) {
  return (
    <div className="flex flex-row font-pretendard items-center justify-between px-10  py-8">
      <div className="text-3xl font-bold text-[#172b4d]">{title}</div>
    </div>
  );
}
