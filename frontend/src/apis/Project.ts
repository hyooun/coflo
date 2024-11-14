import { AxiosResponse } from 'axios';
import { ApiResponse } from 'types/api';
import { instance } from '@config/apiConfig';
import { ProjectRequestResponse } from 'types/language';
import {
  ProjectTeamRequestResoponse,
  ProjectIndividualScoreData,
  ProjectTotalScoreData,
} from 'types/project';

const responseBody = <T>(response: AxiosResponse<ApiResponse<T>>) => response.data;
const apiRequests = {
  get: <T>(url: string, params?: object) =>
    instance.get<ApiResponse<T>>(url, { params }).then(responseBody),
};

export const ProjectRequest = {
  getProjectDetail: (projectId: string): Promise<ApiResponse<ProjectRequestResponse>> =>
    apiRequests.get<ProjectRequestResponse>(`projects/${projectId}`),

  getProjectTeamScore: (projectId: string): Promise<ApiResponse<ProjectTeamRequestResoponse>> =>
    apiRequests.get<ProjectTeamRequestResoponse>(`projects/${projectId}/scores`),

  getProjectCumulativeTotalScore: async (
    projectId: string,
    queryParams?: { calculationType?: string; scoreDisplayType?: string },
  ): Promise<ApiResponse<ProjectTotalScoreData>> => {
    const calculationType = queryParams?.calculationType || 'acquisition';
    const scoreDisplayType = queryParams?.scoreDisplayType || 'total';

    const url = `projects/${projectId}/statistics?calculationType=${calculationType}&scoreDisplayType=${scoreDisplayType}`;

    return await apiRequests.get<ProjectTotalScoreData>(url);
  },

  getProjectCumulativeIndividualScore: async (
    projectId: string,
    queryParams?: { calculationType?: string; scoreDisplayType?: string },
  ): Promise<ApiResponse<ProjectIndividualScoreData>> => {
    const calculationType = queryParams?.calculationType || 'acquisition';
    const scoreDisplayType = queryParams?.scoreDisplayType || 'total';

    const url = `projects/${projectId}/statistics?calculationType=${calculationType}&scoreDisplayType=${scoreDisplayType}`;

    return await apiRequests.get<ProjectIndividualScoreData>(url);
  },

  getProjectNonCumulativeTotalScore: async (
    projectId: string,
    queryParams: { calculationType?: string; scoreDisplayType?: string } = {},
  ): Promise<ApiResponse<ProjectTotalScoreData>> => {
    const calculationType = queryParams.calculationType || 'acquisition';
    const scoreDisplayType = queryParams.scoreDisplayType || 'total';

    const url = `projects/${projectId}/statistics?calculationType=${calculationType}&scoreDisplayType=${scoreDisplayType}`;

    return await apiRequests.get<ProjectTotalScoreData>(url);
  },

  getProjectNonCumulativeIndividualScore: async (
    projectId: string,
    queryParams?: { calculationType?: string; scoreDisplayType?: string },
  ): Promise<ApiResponse<ProjectIndividualScoreData>> => {
    const calculationType = queryParams?.calculationType || 'acquisition';
    const scoreDisplayType = queryParams?.scoreDisplayType || 'total';

    const url = `projects/${projectId}/statistics?calculationType=${calculationType}&scoreDisplayType=${scoreDisplayType}`;

    return await apiRequests.get<ProjectIndividualScoreData>(url);
  },
};
