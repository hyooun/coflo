export interface Language {
  name: string;
  share: number;
  color: string;
}

export interface ProjectRequestResponse {
  commitCount: number;
  branchCount: number;
  mergeRequestCount: number;
  languages: Language[];
  aiReviewCount: number;
}

export interface ProgrammingLanguagesData {
  labels: string[];
  datasets: {
    label: string;
    data: number[];
    backgroundColor: string;
    barThickness: number;
    borderRadius?: { topLeft: number; topRight: number; bottomLeft: number; bottomRight: number }[];
  }[];
}

export interface ProgrammingLanguage {
  name: string;
  share: number;
  color: string;
}

export interface ProjectDetailResponse {
  programmingLanguagesData: ProgrammingLanguagesData | null;
  commitCount: number;
  branchCount: number;
  mergeRequestCount: number;
  aiReviewCount: number;
}
