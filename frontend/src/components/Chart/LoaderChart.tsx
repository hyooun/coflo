import { ProjectRequest } from '@apis/Project';
import { ProjectTotalScoreData, ProjectIndividualScoreData } from 'types/project';

const colors = ['#5795D1', '#B8E314', '#F2B870', '#BB92F6', '#B4D3F1', '#EBC3BB'];

export const loadChartData = async (
  projectId: string,
  chartType: string,
  queryParams?: Record<string, string>,
): Promise<{
  lineData: any;
  cubicLineData: any;
  minScore: number;
  maxScore: number;
  startDate: string | null;
  endDate: string | null;
}> => {
  let cumulativeScoreData: ProjectTotalScoreData | null = null;
  let individualScoreData: ProjectIndividualScoreData | null = null;

  const fetchData = async (): Promise<any> => {
    switch (chartType) {
      case '누적 통합 스코어':
        return await ProjectRequest.getProjectCumulativeTotalScore(projectId, queryParams);
      case '누적 개별 스코어':
        return await ProjectRequest.getProjectCumulativeIndividualScore(projectId, queryParams);
      case '획득 통합 스코어':
        return await ProjectRequest.getProjectNonCumulativeTotalScore(projectId, queryParams);
      case '획득 개별 스코어':
        return await ProjectRequest.getProjectNonCumulativeIndividualScore(projectId, queryParams);
      default:
        throw new Error('Invalid chart type');
    }
  };

  let startDate: string | null = null;
  let endDate: string | null = null;

  try {
    const response = await fetchData();

    if (response.data) {
      const responseData = response.data as any;

      startDate = responseData.startDate || null;
      endDate = responseData.endDate || null;

      if ('scoreOfWeek' in responseData) {
        cumulativeScoreData = {
          startDate: responseData.startDate,
          endDate: responseData.endDate,
          scoreOfWeek: responseData.scoreOfWeek || [],
        };
      } else if ('codeQualityScores' in responseData) {
        individualScoreData = {
          startDate: responseData.startDate,
          endDate: responseData.endDate,
          codeQualityScores: responseData.codeQualityScores || [],
        };
      } else {
        throw new Error('Invalid response structure');
      }
    } else {
      console.warn('No data found in response');
    }
  } catch (error) {
    console.error('Failed to load chart data:', error);
  }

  const minScore = cumulativeScoreData?.scoreOfWeek[0]?.score ?? 0;

  const maxScore = Math.max(
    cumulativeScoreData?.scoreOfWeek?.length
      ? cumulativeScoreData.scoreOfWeek[cumulativeScoreData.scoreOfWeek.length - 1]?.score
      : 0,
    ...(individualScoreData?.codeQualityScores.flatMap((codeQuality) =>
      codeQuality.scoreOfWeek.map((weekScore) => weekScore.score),
    ) || [0]),
  );

  const createLineData = () => {
    const labelsSet = new Set();

    individualScoreData?.codeQualityScores.forEach((codeQuality) => {
      codeQuality.scoreOfWeek.forEach((weekScore) => {
        labelsSet.add(weekScore.week);
      });
    });

    const labels = Array.from(labelsSet as Set<number>)
      .sort((a, b) => a - b)
      .map((week) => `${week}주차`);

    return {
      labels,
      datasets:
        individualScoreData?.codeQualityScores.map((codeQuality, index) => ({
          label: codeQuality.codeQualityName,
          data: labels.map((label) => {
            const weekNumber = parseInt(label);
            const weekData = codeQuality.scoreOfWeek.find((ws) => ws.week === weekNumber);
            return weekData ? weekData.score : 0;
          }),
          borderColor: colors[index % colors.length],
          backgroundColor: 'rgba(167, 191, 232, 0.2)',
        })) || [],
    };
  };

  const createCubicLineData = () => ({
    labels: cumulativeScoreData?.scoreOfWeek.map((weekScore) => `${weekScore.week}주차`) || [],
    datasets: [
      {
        label: '누적 점수',
        data: cumulativeScoreData?.scoreOfWeek.map((weekScore) => weekScore.score) || [],
        borderColor: '#9EE8E6',
        backgroundColor: 'rgba(158, 232, 230, 0.2)',
        fill: true,
      },
    ],
  });

  const lineData = createLineData();
  const cubicLineData = createCubicLineData();

  return {
    lineData,
    cubicLineData,
    minScore,
    maxScore,
    startDate,
    endDate,
  };
};
