export interface Assignee {
  username: string;
  avatar_url: string;
}

export interface Reviewer {
  username: string;
  avatar_url: string;
}

export interface GitlabMergeRequest {
  id: number;
  iid: number;
  title: string;
  description: string;
  state: 'opened' | 'closed' | 'merged';
  merged_at: string | null;
  created_at: string;
  updated_at: string;
  closed_at: string | null;
  source_branch: string;
  target_branch: string;
  labels: string[];
  has_conflicts: boolean;
  assignee: Assignee;
  reviewer: Reviewer;
  isAiReviewCreated: boolean;
}

export interface GitlabMrListResponse {
  gitlabMrList: GitlabMergeRequest[];
  totalPages: number;
  totalElements: number;
  isLast: boolean;
  currPage: number;
}

export interface MrItemProps {
  mergeRequest: GitlabMergeRequest;
}
