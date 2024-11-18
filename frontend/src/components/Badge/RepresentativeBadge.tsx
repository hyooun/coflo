import { RepresentativeBadgeProps } from 'types/badge';

const RepresentativeBadge = ({ selectedBadge, onSave }: RepresentativeBadgeProps) => {
  return (
    <div className="my-8 flex flex-col items-center ">
      <div className="text-xl font-extrabold mb-4">대표 뱃지</div>
      <div className="w-60 h-60 bg-[#EFF2FB] border-2 border-[#EAEEF8] rounded-full flex items-center justify-center mb-4">
        {selectedBadge ? (
          <img src={selectedBadge} alt="대표 뱃지" className="w-48 h-48 object-contain" />
        ) : (
          <div className="flex flex-col items-center text-white">
            <span>대표 뱃지를</span> <span> 선택해주세요</span>
          </div>
        )}
      </div>
      <div className="flex flex-row gap-4">
        <button
          onClick={onSave}
          className="px-4 py-2 bg-secondary text-white font-bold rounded-xl text-lg"
        >
          저장
        </button>
      </div>
    </div>
  );
};

export default RepresentativeBadge;
