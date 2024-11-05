import { BadgeType } from 'types/badge';
import Badge from './Badge';

interface BadgeListProps {
  badges: BadgeType[];
  onBadgeClick: (badge: BadgeType) => void;
}

const BadgeList = ({ badges, onBadgeClick }: BadgeListProps) => {
  return (
    <div className="flex flex-col items-center mb-10 w-full max-w-[1000px] mx-auto">
      <div className="flex flex-row justify-start w-full my-4">
        <p className="text-2xl font-bold">All Badges</p>
      </div>
      <div className="grid grid-cols-4 grid-rows-3 gap-16 p-6 rounded-xl bg-gray-900">
        {badges.map((badge) =>
          badge.isAcquired ? (
            <Badge badge={badge} onClick={() => onBadgeClick(badge)} size="2xl" />
          ) : (
            <Badge
              badge={badge}
              onClick={() => onBadgeClick(badge)}
              size="2xl"
              tooltipOptions={{ show: true, showName: true, showDescription: false }}
            />
          ),
        )}
      </div>
    </div>
  );
};

export default BadgeList;
