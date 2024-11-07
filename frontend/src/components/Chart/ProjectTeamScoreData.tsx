import { ProjectRequest } from '@apis/Project';
import { ProjectTeamRequestResoponse } from 'types/project';
import { RadarData } from 'types/project';

const colors = ['#5795D1', '#B8E314', '#F2B870', '#BB92F6', '#B4D3F1', '#EBC3BB'];
const profileIcons: Record<number, string> = {};
const badgeIcons: Record<number, string> = {};

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

    teamData.userScores.slice(0, 6).forEach((user) => {
      if (user.profileImageUrl) {
        profileIcons[user.userId] = user.profileImageUrl;
      }
      if (user.badgeImageUrl) {
        badgeIcons[user.userId] = user.badgeImageUrl;
      }
    });

    const radarData = {
      labels: teamData.userScores[0].scores.map((score) => score.name),
      datasets: teamData.userScores.slice(0, 6).map((user, index) => ({
        label: user.username,
        data: user.scores.map((score) => score.score),
        borderColor: colors[index % colors.length],
        backgroundColor: 'transparent',
      })),
    };

    return { radarData, profileIcons, badgeIcons };
  }

  return null;
};
