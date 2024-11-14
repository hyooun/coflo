import { ProjectRequest } from '@apis/Project';
import { ProjectTeamRequestResoponse } from 'types/project';
import { RadarData } from 'types/project';

const colors = ['#5795D1', '#B8E314', '#F2B870', '#BB92F6', '#B4D3F1', '#EBC3BB'];
const profileIcons: Record<number, string> = {};
const badgeIcons: Record<number, string> = {};

const defaultLabels = ['가독성', '유지보수성', '보안성', '신뢰성', '재사용성', '일관성'];

export interface TeamScoreResponse {
  radarData: RadarData;
  profileIcons: Record<number, string>;
  badgeIcons: Record<number, string>;
}

export const fetchProjectTeamScore = async (
  projectId: string,
): Promise<TeamScoreResponse | null> => {
  const response = await ProjectRequest.getProjectTeamScore(projectId);
  if (response.data) {
    const teamData: ProjectTeamRequestResoponse = response.data;

    teamData.userScores.slice(0, 6).forEach((user, index) => {
      if (user.profileImageUrl) {
        profileIcons[index] = user.profileImageUrl;
      }
      if (user.badgeImageUrl) {
        badgeIcons[index] = user.badgeImageUrl;
      }
    });

    const radarData = {
      labels: defaultLabels,
      datasets: teamData.userScores.slice(0, 6).map((user, index) => ({
        label: user.username,
        data: defaultLabels.map((label) => {
          const scoreObj = user.scores.find((score) => score.name === label);
          return scoreObj ? scoreObj.score : 0;
        }),
        borderColor: colors[index % colors.length],
        backgroundColor: 'transparent',
      })),
    };

    return { radarData, profileIcons, badgeIcons };
  }

  return null;
};
