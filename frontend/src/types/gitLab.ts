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

export interface ValidateUserTokenRequest {
  domain: string;
  userToken: string;
}

export interface ValidateBotTokenRequest {
  gitlabProjectId: string;
  botToken: string;
}
