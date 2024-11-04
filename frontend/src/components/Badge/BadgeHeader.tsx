const BadgeHeader = () => {
  return (
    <div className="flex flex-col space-y-2 mb-6 w-full max-w-[1000px] min-w-[350px] items-start">
      <span className="font-bold text-3xl">My Badge</span>
      <div className="flex flex-col">
        <span className="font-bold">다른 사람에게 보이는 뱃지를 설정 할 수 있습니다.</span>
      </div>
    </div>
  );
};

export default BadgeHeader;
