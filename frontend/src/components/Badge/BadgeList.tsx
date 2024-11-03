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
    <div className="flex flex-col items-center">
      <div className="flex flex-row justify-start w-full my-4">
        <p className="text-2xl font-bold">All Badges</p>
      </div>
      <div className="grid grid-cols-4 grid-rows-3 gap-4 rounded-xl bg-gray-900">
        {badges.map((badge) => (
          <div
            key={badge.id}
            onClick={() => onBadgeClick(badge)}
            className={`w-full aspect-square flex items-center justify-center rounded-xl 
              ${badge.isAcquired ? 'hover:bg-secondary cursor-pointer' : ''}`}
          >
            <img
              src={badge.image}
              alt={`뱃지 ${badge.id}`}
              className="w-3/4 h-3/4 object-contain"
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default BadgeList;
