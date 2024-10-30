import { GitlabMergeRequest, Reviewer } from 'types/mergeRequest.ts';

export interface MergeRequestReview extends GitlabMergeRequest {
  reviews: Review[];
}

export interface Review {
  id: number;
  reviewer: Reviewer;
  createdAt: string;
  updatedAt: string;
  content: string; // 마크다운 형식의 리뷰 내용
  comments: Comment[];
}

interface Comment {
  id: number;
  reviewer: Reviewer;
  createdAt: string;
  updatedAt: string;
  content: string;
  resolved?: boolean;
  resolvable: boolean;
  replies: CommentReply[];
}

interface CommentReply {
  id: number;
  author: Reviewer;
  content: string;
  createdAt: string;
  updatedAt: string;
  system?: boolean;
}

export type ReferenceType = 'code' | 'text';

export interface Reference {
  id: number;
  fileName: string;
  type: ReferenceType;
  content: string;
  relevance?: number;
}

export interface ReviewCommentProps {
  name?: string;
  title?: string;
  type?: ReferenceType;
  content: string;
  createdAt?: string;
  backgroundColor?: string;
  reviewer?: Reviewer;
}
