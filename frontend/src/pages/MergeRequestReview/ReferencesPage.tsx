import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import { Reference } from 'types/reference.ts';
import ReferencesList from '@components/MergeRequest/Reference/ReferencesList.tsx';
import ScrollNavigationButton from '@components/Button/ScrollNavigationButton';
import Header from '@components/Header/Header.tsx';
import { Review } from '@apis/Review';

const ReferencesPage = () => {
  const { id, selectedReviewId } = useParams<{ id: string; selectedReviewId: string }>();

  const [references, setReferences] = useState<Reference[]>([]);
  useEffect(() => {
    if (!selectedReviewId) return;

    const fetchReferences = async (reviewId: string) => {
      const response = await Review.getReviewRetrievals(reviewId);
      const data = response.data;
      if (data) {
        setReferences(data);
      }
    };

    fetchReferences(selectedReviewId);
  }, [selectedReviewId, id]);

  if (!references) return <div>Loading...</div>;

  return (
    <div className="flex flex-col p-10 w-full overflow-auto scroll-container">
      <Header
        title={'References'}
        description={[
          '내용을 추가하거나 수정하여 리뷰를 재생성 할 수 있습니다.',
          '모든 변경사항은 재생성시 저장됩니다.',
        ]}
      />
      <ReferencesList references={references} />
      <ScrollNavigationButton />
    </div>
  );
};

export default ReferencesPage;
