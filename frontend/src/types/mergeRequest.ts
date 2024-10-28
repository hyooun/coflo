export interface MergeRequest {
  id: number;
  branchName: string;
  title: string;
  assignee: string;
  reviewer: string;
  createdAt: string;
  labels: string[];
  author: string;
}
