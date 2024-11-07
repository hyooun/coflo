export interface Project {
  id: string;
  name: string;
  isCurrent?: boolean;
}

export interface ProjectSelectorProps {
  onClose: () => void;
  titleRef: React.RefObject<HTMLDivElement>;
}

export interface ProjectLinkRequest {
  botToken: string;
  branches: string[];
}

export interface UserProjectData {
  projectId: number;
}

export interface UserProjectResponse {
  /** @format int64 */
  projectId: number;
  name: string;
}

export interface GetLinkedStatusData {
  hasLinkeProject: boolean;
}
