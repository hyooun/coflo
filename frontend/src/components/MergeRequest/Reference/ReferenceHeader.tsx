const ReferenceHeader = () => {
  return (
    <div className="flex flex-col space-y-2 mb-6 min-w-[350px]">
      <span className="font-bold text-3xl">References</span>
      <div className="flex flex-col">
        <span className="font-bold">내용을 추가하거나 수정하여 리뷰를 재생성 할 수 있습니다.</span>
        <span className="font-bold">모든 변경사항은 재생성시 저장됩니다</span>
      </div>
    </div>
  );
};

export default ReferenceHeader;
