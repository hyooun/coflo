interface Badge {
  id: number;
  image: string;
  isAcquired: boolean;
}

interface BadgeListProps {
  badges: Badge[];
  onBadgeClick: (badge: Badge) => void;
}

const BadgeList = ({ badges, onBadgeClick }: BadgeListProps) => {
  return (
    <div className="grid grid-cols-4 grid-rows-3 gap-4">
      {badges.map((badge) => (
        <div
          key={badge.id}
          onClick={() => onBadgeClick(badge)}
          className={`w-full aspect-square flex items-center justify-center border rounded-lg cursor-pointer
              ${badge.isAcquired ? 'hover:bg-gray-100' : 'bg-gray-200'}`}
        >
          <img src={badge.image} alt={`뱃지 ${badge.id}`} className="w-3/4 h-3/4 object-contain" />
        </div>
      ))}
    </div>
  );
};

export default BadgeList;
