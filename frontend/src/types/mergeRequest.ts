export interface Assignee {
  id: number;
  username: string;
  name: string;
  avatarUrl: string;
}

export interface Reviewer {
  id: number;
  username: string;
  name: string;
  avatarUrl: string;
}

export interface GitlabMergeRequest {
  id: number;
  iid: number;
  title: string;
  description: string;
  state: 'opened' | 'closed' | 'merged';
  mergedAt: string | null;
  createdAt: string;
  updatedAt: string;
  closedAt: string | null;
  sourceBranch: string;
  targetBranch: string;
  labels: string[];
  hasConflicts: boolean;
  assignee: Assignee;
  reviewer: Reviewer;
  isAiReviewCreated: boolean;
}

export interface SearchParameter {
  keyword?: string;
  /** @format int32 */
  page?: number;
  /** @format int32 */
  size?: number;
}

export interface GitlabMrListResponse {
  gitlabMrList: GitlabMergeRequest[];
  totalPages?: number;
  totalElements?: number;
  isLast?: boolean;
  currPage?: number;
}

export interface MrItemProps {
  mergeRequest: GitlabMergeRequest;
}
