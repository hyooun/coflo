import { useParams } from 'react-router-dom';
import MergeRequestHeader from '@components/MergeRequest/MergeRequestHeader';
import ReviewList from '@components/MergeRequest/Review/ReviewList.tsx';
import { GitlabMergeRequest } from 'types/mergeRequest.ts';
import { MergeRequestReview } from 'types/review.ts';
import { Reference } from 'types/reference';
import { useEffect, useState } from 'react';
import ReferencesList from '@components/MergeRequest/Review/ReviewReferenceList.tsx';
import { Review } from '@apis/Review';
import { projectIdAtom } from '@store/auth';
import { useAtom } from 'jotai';

const MergeRequestReviewPage = () => {
  const { id } = useParams<{ id: string }>();
  const [projectId] = useAtom(projectIdAtom);
  console.log(projectId);
  const [mergeRequest, setMergeRequest] = useState<GitlabMergeRequest | null>(null);
  const [reviews, setReviews] = useState<MergeRequestReview['reviews']>([]);
  const [references, setReferences] = useState<Reference[]>([]);
  const [selectedReviewId, setSelectedReviewId] = useState<string | null>(null);
  const [sendReviewId, setSendReviewId] = useState<string | null>(null);

  const fetchMergeRequest = async (projectId: string, mergeRequestIid: string) => {
    const response = await Review.getCodeReviewList(projectId, mergeRequestIid);
    const data = response.data;
    if (data) {
      setMergeRequest(data.mergeRequest);
      setReviews(data.reviews);
    }
  };

  useEffect(() => {
    if (!id || !projectId) return;

    fetchMergeRequest(projectId, id);
  }, [id, projectId]);

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
  }, [selectedReviewId]);

  useEffect(() => {
    if (reviews.length > 0 && !selectedReviewId) {
      setSelectedReviewId(String(Number(reviews[0].id)));
      setSendReviewId(String(Number(reviews[reviews.length - 1].id)));
    }
  }, [reviews, selectedReviewId]);

  if (!mergeRequest) return <div>Loading...</div>;

  const handleReviewClick = (reviewId: string) => {
    setSelectedReviewId(reviewId);
  };

  return (
    <div className="p-8 flex flex-col w-full overflow-auto items-center">
      <div className="w-full border-b-[1px] border-background-bnavy">
        <MergeRequestHeader mergeRequest={mergeRequest} />
        <div className="flex gap-12 w-full">
          <ReviewList
            reviews={reviews}
            mergeRequest={mergeRequest}
            onReviewClick={handleReviewClick}
          />
          <ReferencesList
            references={references}
            selectedReviewId={selectedReviewId}
            sendReviewId={sendReviewId}
          />
        </div>
      </div>
    </div>
  );
};

export default MergeRequestReviewPage;
