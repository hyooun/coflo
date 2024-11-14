export default function Header({ title, description }: HeaderProps) {
  return (
    <div className="flex flex-col space-y-2 mb-6 w-full min-w-[500px] max-w-[2000px] items-start font-pretendard">
      <div className="text-3xl font-bold">{title}</div>

      {description && (
        <div className="flex flex-col">
          {description.map((desc, index) => (
            <div key={index} className="text-xl">
              {desc}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
