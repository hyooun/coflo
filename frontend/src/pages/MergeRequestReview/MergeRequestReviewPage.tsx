// src/pages/MergeRequestReview/MergeRequestReviewPage.tsx
import { useParams } from 'react-router-dom';
import MergeRequestHeader from '@components/MergeRequest/MergeRequestHeader .tsx';
import ReviewList from '@components/MergeRequest/Review/ReviewList.tsx';
import { GitlabMergeRequest } from 'types/mergeRequest.ts';
import { MergeRequestReview, Reference } from 'types/review.ts';
import { useEffect, useState } from 'react';
import ReferencesList from '@components/MergeRequest/Review/ReferenceList.tsx';

const MergeRequestReviewPage = () => {
  const { id } = useParams();
  const [mergeRequest, setMergeRequest] = useState<GitlabMergeRequest | null>(null);
  const [reviews, setReviews] = useState<MergeRequestReview['reviews']>([]);
  const [references, setReferences] = useState<Reference[]>([]);

  useEffect(() => {
    if (!id) return;

    const fetchMergeRequest = async () => {
      const response = await fetch(`/api/reviews/${id}`);
      const data = await response.json();
      setMergeRequest(data);
      setReviews(data.reviews);
    };

    const fetchReferences = async () => {
      const response = await fetch(`/api/reviews/${id}/retrivals`);
      const data = await response.json();
      setReferences(data);
    };

    fetchMergeRequest();
    fetchReferences();
  }, [id]);

  if (!mergeRequest) return <div>Loading...</div>;

  return (
    <div className="p-8 flex flex-col">
      <div className="w-full">
        <MergeRequestHeader mergeRequest={mergeRequest} />
      </div>
      <div className="flex gap-12 ml-4">
        <div className="flex-1">
          <ReviewList reviews={reviews} />
        </div>
        <div className="">
          <ReferencesList references={references} />
        </div>
      </div>
    </div>
  );
};

export default MergeRequestReviewPage;
