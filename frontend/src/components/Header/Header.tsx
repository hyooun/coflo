export default function Header({ title, description }: HeaderProps) {
  return (
    <div className="flex flex-col space-y-2 mb-6 w-full max-w-[1000px] min-w-[350px] items-start font-pretendard">
      <div className="text-3xl font-bold">{title}</div>

      {description && (
        <div className="flex flex-col">
          {description.map((desc, index) => (
            <div key={index} className="font-bold">
              {desc}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
