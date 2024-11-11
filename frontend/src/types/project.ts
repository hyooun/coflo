export interface Project {
  projectId: string;
  name: string;
}
export interface Score {
  name: string;
  score: number;
}

export interface UserScore {
  userId: number;
  username: string;
  profileImageUrl: string;
  badgeName: string | null;
  badgeImageUrl: string | null;
  scores: Score[];
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

// export interface UserProjectResponse {
//   /** @format int64 */
//   projectId: number;
//   name: string;
// }

export interface GetLinkedStatusData {
  hasLinkedProject: boolean;
  projectId: string;
  projectFullPath: string;
}

export interface ProjectTeamRequestResoponse {
  startDate: string;
  endDate: string;
  userScores: UserScore[];
}
export interface RadarData {
  labels: string[];
  datasets: {
    label: string;
    data: number[];
    borderColor: string;
    backgroundColor: string;
  }[];
}

export interface ProjectTotalScoreData {
  startDate: string;
  endDate: string;
  scoreOfWeek: {
    week: number;
    score: number;
  }[];
}

export interface ProjectIndividualScoreData {
  startDate: string;
  endDate: string;
  codeQualityScores: {
    codeQualityName: string;
    scoreOfWeek: {
      week: number;
      score: number;
    }[];
  }[];
}
