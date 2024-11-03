interface RepresentativeBadgeProps {
  selectedBadge: string | null;
  onSave: () => void;
}

const RepresentativeBadge = ({ selectedBadge, onSave }: RepresentativeBadgeProps) => {
  return (
    <div className="my-8 flex flex-col items-center">
      <div className="w-40 h-40 border-2 rounded-lg flex items-center justify-center mb-4">
        {selectedBadge ? (
          <img src={selectedBadge} alt="대표 뱃지" className="w-32 h-32 object-contain" />
        ) : (
          <p className="text-gray-400">대표 뱃지를 선택해주세요</p>
        )}
      </div>
      <button onClick={onSave} className="px-4 py-2 bg-primary-500 text-white rounded-lg">
        저장
      </button>
    </div>
  );
};

export default RepresentativeBadge;
