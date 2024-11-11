export interface GitlabProject {
  gitlabProjectId: string;
  name: string;
  isLinked: boolean;
  isLinkable: boolean;
}

export interface GitlabProjectListResponse {
  gitlabProjectList: GitlabProject[];
  pageInfo: PageInfo;
}

export interface PageInfo {
  startCursor: string;
  hasNextPage: boolean;
  hasPreviousPage: boolean;
  endCursor: string;
}

export interface ValidateUserTokenRequest {
  domain: string;
  userToken: string;
}

export interface ValidateBotTokenRequest {
  gitlabProjectId: string;
  botToken: string;
}
