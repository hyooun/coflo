import { useEffect, useState } from 'react';

import RepresentativeBadge from '@components/Badge/RepresentativeBadge';
import BadgeList from '@components/Badge/BadgeList';
import { BadgeType } from 'types/badge';
import AlertModal from '@components/Modal/AlertModal';
import Header from '@components/Header/Header.tsx';
import { Badge } from '@apis/Badge';

const BadgePage = () => {
  const [badges, setBadges] = useState<BadgeType[]>([]);
  const [selectedBadge, setSelectedBadge] = useState<BadgeType | null>(null);
  const [mainBadge, setMainBadge] = useState<BadgeType | null>(null);
  const [isAlertModalOpen, setIsAlertModalOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState<string[]>([]);
  const [mainBadgeCodeId, setMainBadgeCodeId] = useState<string>('');

  useEffect(() => {
    const fetchBadges = async () => {
      const response = await Badge.getBadge();
      if (response.data) {
        setBadges(response.data.badgeDetails);
        setMainBadgeCodeId(response.data.mainBadgeCodeId || '');

        const mainBadgeData = response.data.badgeDetails.find(
          (badge: BadgeType) => badge.badgeCodeId === mainBadgeCodeId,
        );
        setMainBadge(mainBadgeData || null);
        setSelectedBadge(mainBadgeData || null);
      }
    };
    fetchBadges();
  }, []);

  const handleBadgeClick = (badge: BadgeType) => {
    if (badge.isAcquired) {
      selectedBadge?.badgeCodeId === badge.badgeCodeId
        ? setSelectedBadge(null)
        : setSelectedBadge(badge);
    }
  };

  const handleSave = async () => {
    if (mainBadge?.badgeCodeId === selectedBadge?.badgeCodeId) {
      return;
    }

    try {
      const { status } = await Badge.updateMainBadge(selectedBadge?.badgeCodeId || '');

      if (status === 'SUCCESS') {
        setMainBadge(selectedBadge);
        setAlertMessage([
          selectedBadge ? '대표 뱃지가 설정되었습니다.' : '대표 뱃지가 해제되었습니다.',
        ]);
        setIsAlertModalOpen(true);
      }
    } catch (error) {
      console.error('Error saving badge:', error);
      setAlertMessage([
        selectedBadge ? '대표 뱃지 설정에 실패했습니다.' : '대표 뱃지 해제에 실패했습니다.',
      ]);
      setIsAlertModalOpen(true);
    }
  };

  return (
    <div className="flex flex-col flex-grow overflow-auto p-10 min-w-[1000px] items-center">
      <Header
        title={'My Badge'}
        description={['다른 사람에게 보이는 뱃지를 설정 할 수 있습니다.']}
      />
      <RepresentativeBadge selectedBadge={selectedBadge?.imageUrl || null} onSave={handleSave} />
      <BadgeList badges={badges} onBadgeClick={handleBadgeClick} />
      {isAlertModalOpen && (
        <AlertModal content={alertMessage} onConfirm={() => setIsAlertModalOpen(false)} />
      )}
    </div>
  );
};

export default BadgePage;
