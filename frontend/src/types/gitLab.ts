export interface GitlabProject {
  gitlabProjectId: number;
  name: string;
  isLinked: boolean;
  isLinkable: boolean;
}

export interface GitlabProjectListResponse {
  gitlabProjectList: GitlabProject[];
  totalPages: number;
  totalElements: number;
  isLast: boolean;
  currPage: number;
}

export interface ValidateTokenRequest {
  domain: string;
  userToken: string;
}
