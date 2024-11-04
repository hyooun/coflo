import { useEffect, useState } from 'react';
import axios from 'axios';

import BadgeHeader from '@components/Badge/BadgeHeader';
import RepresentativeBadge from '@components/Badge/RepresentativeBadge';
import BadgeList from '@components/Badge/BadgeList';
import { BadgeType } from 'types/badge';
import AlertModal from '@components/Modal/AlertModal';

const BadgePage = () => {
  const [badges, setBadges] = useState<BadgeType[]>([]);
  const [selectedBadge, setSelectedBadge] = useState<BadgeType | null>(null);
  const [isAlertModalOpen, setIsAlertModalOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState<string[]>([]);

  useEffect(() => {
    const fetchBadges = async () => {
      try {
        const { data } = await axios.get('/api/badges');
        setBadges(data.data.badgeDetails);

        const mainBadgeData = data.data.badgeDetails.find(
          (badge: BadgeType) => badge.badgeCodeId === data.data.mainBadgeCodeId,
        );
        if (mainBadgeData) {
          setSelectedBadge(mainBadgeData);
        }
      } catch (error) {
        console.error('Error fetching badges:', error);
      }
    };
    fetchBadges();
  }, []);

  const handleBadgeClick = (badge: BadgeType) => {
    if (badge.isAcquired) {
      setSelectedBadge(badge);
    }
  };

  const handleSave = async () => {
    if (!selectedBadge) return;

    try {
      const { data } = await axios.patch('/api/badges', {
        badgeId: selectedBadge.badgeCodeId,
      });

      if (data.status === 'SUCCESS') {
        setAlertMessage(['대표 뱃지가 설정되었습니다.']);
        setIsAlertModalOpen(true);
      }
    } catch (error) {
      console.error('Error saving badge:', error);
      setAlertMessage(['대표 뱃지 설정에 실패했습니다.']);
      setIsAlertModalOpen(true);
    }
  };

  const handleDefault = async () => {
    try {
      const { data } = await axios.delete('/api/badges');

      if (data.status === 'SUCCESS') {
        setSelectedBadge(null);
        setAlertMessage(['대표 뱃지가 해제되었습니다.']);
        setIsAlertModalOpen(true);
      }
    } catch (error) {
      console.error('Error removing badge:', error);
      setAlertMessage(['대표 뱃지 해제에 실패했습니다.']);
      setIsAlertModalOpen(true);
    }
  };

  return (
    <div className="flex flex-col flex-grow overflow-auto px-8 pt-6">
      <BadgeHeader />
      <RepresentativeBadge
        selectedBadge={selectedBadge?.imageUrl || null}
        onSave={handleSave}
        onDefault={handleDefault}
      />
      <BadgeList badges={badges} onBadgeClick={handleBadgeClick} />
      {isAlertModalOpen && (
        <AlertModal content={alertMessage} onConfirm={() => setIsAlertModalOpen(false)} />
      )}
    </div>
  );
};

export default BadgePage;
