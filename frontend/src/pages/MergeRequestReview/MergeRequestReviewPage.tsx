// src/pages/MergeRequestReview/MergeRequestReviewPage.tsx
import { useParams } from 'react-router-dom';
import MergeRequestHeader from '@components/MergeRequest/MergeRequestHeader .tsx';
import ReviewList from '@components/MergeRequest/ReviewList.tsx';
import { GitlabMergeRequest } from 'types/mergeRequest.ts';
import { MergeRequestReview, Reference } from 'types/review.ts';
import { useEffect, useState } from 'react';

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
    <div>
      <MergeRequestHeader mergeRequest={mergeRequest} />
      <ReviewList reviews={reviews} />
    </div>
  );
};

export default MergeRequestReviewPage;
