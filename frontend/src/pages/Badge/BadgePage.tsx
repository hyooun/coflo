import { useState } from 'react';
import badge1blur from '@assets/badge1blur.jpg';
import badge1 from '@assets/badge1.jpg';
import BadgeHeader from '@components/Badge/BadgeHeader';
import RepresentativeBadge from '@components/Badge/RepresentativeBadge';
import BadgeList from '@components/Badge/BadgeList';

const BadgePage = () => {
  const [selectedBadge, setSelectedBadge] = useState<string | null>(null);

  const badges = [
    { id: 1, image: badge1, isAcquired: true },
    { id: 2, image: badge1blur, isAcquired: false },
    // ... 나머지 뱃지들 (총 12개)
  ];

  const handleBadgeClick = (badge: { id: number; image: string; isAcquired: boolean }) => {
    if (badge.isAcquired) {
      setSelectedBadge(badge.image);
    }
  };

  const handleSave = () => {
    console.log('대표 뱃지 저장:', selectedBadge);
  };

  return (
    <div className="flex flex-col flex-grow overflow-auto px-8 pt-6">
      <BadgeHeader />
      <RepresentativeBadge selectedBadge={selectedBadge} onSave={handleSave} />
      <BadgeList badges={badges} onBadgeClick={handleBadgeClick} />
    </div>
  );
};

export default BadgePage;
