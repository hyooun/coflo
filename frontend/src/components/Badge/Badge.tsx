import { BadgeProps } from 'types/badge';
import badge_none from '@assets/images/badges/badge_none.png';

const Badge = ({
  badge,
  onClick,
  size = 'md',
  tooltipOptions = {
    show: true,
    showName: true,
    showDescription: true,
  },
}: BadgeProps) => {
  const sizeClasses = {
    sm: 'w-16 h-16',
    md: 'w-24 h-24',
    lg: 'w-32 h-32',
    xl: 'w-40 h-40',
    '2xl': 'w-48 h-48',
  };

  return (
    <div className="relative group">
      <div
        onClick={onClick}
        className={`aspect-square flex items-center justify-center rounded-xl 
          ${badge.isAcquired ? 'hover:bg-secondary cursor-pointer' : ''}`}
      >
        <img
          src={badge.isAcquired ? badge.imageUrl : badge_none}
          alt={`뱃지 ${badge.badgeCodeId}`}
          className={`${sizeClasses[size]} object-contain`}
        />
      </div>

      {/* 툴팁 */}
      {tooltipOptions.show && (
        <div
          className="absolute z-10 invisible group-hover:visible bg-secondary text-white text-sm rounded-lg py-2 px-3 
          -bottom-16 left-1/2 transform -translate-x-1/2 min-w-max"
        >
          <div className="flex flex-col gap-1">
            {tooltipOptions.showName && <span className="font-bold">{badge.name}</span>}
            {tooltipOptions.showDescription && (
              <span className="text-gray-300">{badge.description}</span>
            )}
          </div>
          {/* 툴팁 화살표 */}
          <div
            className="absolute -top-2 left-1/2 transform -translate-x-1/2 
            border-solid border-4 border-transparent border-b-gray-900"
          />
        </div>
      )}
    </div>
  );
};

export default Badge;
