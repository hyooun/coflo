import { RepresentativeBadgeProps } from 'types/badge';

const RepresentativeBadge = ({ selectedBadge, onSave, onDefault }: RepresentativeBadgeProps) => {
  return (
    <div className="my-8 flex flex-col items-center ">
      <div className="w-48 h-48 bg-primary-500 border-2 border-primary-500 rounded-lg flex items-center justify-center mb-4">
        {selectedBadge ? (
          <img src={selectedBadge} alt="대표 뱃지" className="w-40 h-40 object-contain" />
        ) : (
          <div className="flex flex-col items-center text-gray-700">
            <span>대표 뱃지를</span> <span> 선택해주세요</span>
          </div>
        )}
      </div>
      <div className="flex flex-row gap-4">
        <button onClick={onDefault} className="px-4 py-2 bg-primary-500 text-white rounded-lg">
          설정 안 함
        </button>
        <button onClick={onSave} className="px-4 py-2 bg-primary-500 text-white rounded-lg">
          저장
        </button>
      </div>
    </div>
  );
};

export default RepresentativeBadge;
