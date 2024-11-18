import { BadgeType } from 'types/badge';
import Badge from './Badge';

interface BadgeListProps {
  badges: BadgeType[];
  onBadgeClick: (badge: BadgeType) => void;
}

const BadgeList = ({ badges, onBadgeClick }: BadgeListProps) => {
  return (
    <div className="flex flex-col w-full min-w-[400px] max-w-[1000px] mx-auto">
      {/* <div className="flex flex-row justify-start w-full mb-4">
        <p className="text-2xl font-bold">All Badges</p>
      </div> */}
      <div className="grid grid-cols-3 grid-rows-3 gap-8 rounded-3xl bg-background-blue border-2 border-[#F0F2F4] p-6">
        {badges.map((badge) =>
          badge.isAcquired ? (
            <Badge badge={badge} onClick={() => onBadgeClick(badge)} size="2xl" key={badge.name} />
          ) : (
            <Badge
              badge={badge}
              onClick={() => onBadgeClick(badge)}
              size="2xl"
              tooltipOptions={{ show: true, showName: true, showDescription: false }}
              key={badge.name}
            />
          ),
        )}
      </div>
    </div>
  );
};

export default BadgeList;
