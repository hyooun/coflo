import { GitlabMergeRequest, Reviewer } from 'types/mergeRequest.ts';

interface MergeRequestReview extends GitlabMergeRequest {
  reviews: Review[];
  references: Reference[];
}

interface Review {
  reviewer: Reviewer;
  createdAt: string;
  content: string;
  comments: (CodeComment | GeneralComment)[];
}

interface CodeComment {
  type: 'code';
  fileName: string;
  codeChanges: string;
  reviewer: Reviewer;
  createdAt: string;
  content: string;
}

interface GeneralComment {
  type: 'general';
  reviewer: Reviewer;
  createdAt: string;
  content: string;
}

interface Reference {
  id: number;
  fileName: string;
  type: 'code' | 'text';
  content: string;
}
