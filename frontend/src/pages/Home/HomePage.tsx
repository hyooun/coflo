import ChartBox from '@components/ChartBox/ChartBox';
import { useEffect, useState, useRef } from 'react';
import 'chart.js/auto';
import IconWithText from '@components/TextDiv/IconWithText';
import { BranchIcon } from '@components/TextDiv/Icons/BranchIcon';
import { PullRequestIcon } from '@components/TextDiv/Icons/PullRequestIcon';
import { CommitIcon } from '@components/TextDiv/Icons/CommitIcon';
import { AiIcon } from '@components/TextDiv/Icons/AiIcon';
import { fetchProjectDetail } from '@components/Chart/ProgrammingLanguageData';
import { fetchProjectTeamScore } from '@components/Chart/ProjectTeamScoreData';
import { RadarData } from 'types/project';
import { ChartDropdown } from '@components/Home/ChartDropDown';
import { loadChartData } from '@components/Chart/LoaderChart';

import {
  createHorizontalBarOptions,
  createRadarOptions,
  createLineOptions,
  createCubicLineOptions,
} from '@constants/chartOptions';
import Title from '@components/Title/Title';
import BestMergeRequestList from '@components/Home/BestMergeRequest/BestMergeRequestList.tsx';
import { ProgrammingLanguagesData } from 'types/language';
import { projectIdAtom } from '@store/auth';
import { useAtom } from 'jotai';
import { ProjectIdError } from 'types/errors';
import { useParams } from 'react-router-dom';

const HomePage = () => {
  const initialProjectDetail = {
    programmingLanguagesData: null,
    commitCount: 0,
    branchCount: 0,
    mergeRequestCount: 0,
    aiReviewCount: 0,
  };
  const initialRadarData: RadarData = {
    labels: [],
    datasets: [],
  };
  const [, setContainerHeight] = useState<number>(0);

  const [radarData, setRadarData] = useState<RadarData | null>(initialRadarData);
  const [profileIcons, setProfileIcons] = useState<Record<number, string>>({});
  const [badgeIcons, setBadgeIcons] = useState<Record<number, string>>({});
  const [selectedChart, setSelectedChart] = useState<string>('획득 통합 스코어');
  const [, setSelectedOption] = useState<string | null>(null);

  const [lineData, setLineData] = useState({
    labels: [],
    datasets: [],
  });

  const [cubicLineData, setCubicLineData] = useState({
    labels: [],
    datasets: [],
  });

  const [maxValue, setMaxValue] = useState<number>(0);
  const [minValue, setMinValue] = useState<number>(100);
  const containerRef = useRef<HTMLDivElement | null>(null);
  const [projectDetail, setProjectDetail] = useState<{
    programmingLanguagesData: ProgrammingLanguagesData | null;
    commitCount: number;
    branchCount: number;
    mergeRequestCount: number;
    aiReviewCount: number;
  }>(initialProjectDetail);
  const { projectId: urlProjectId } = useParams();
  const [projectId, setProjectId] = useAtom(projectIdAtom);
  const [error, setError] = useState<Error | null>(null);

  const handleSelect = (chart: string) => {
    setSelectedChart(chart);
  };

  useEffect(() => {
    let calculationType = 'acquisition';
    let scoreDisplayType = 'total';

    switch (selectedChart) {
      case '획득 통합 스코어':
        calculationType = 'acquisition';
        scoreDisplayType = 'total';
        break;
      case '획득 개별 스코어':
        calculationType = 'acquisition';
        scoreDisplayType = 'individual';
        break;
      case '누적 통합 스코어':
        calculationType = 'cumulative';
        scoreDisplayType = 'total';
        break;
      case '누적 개별 스코어':
        calculationType = 'cumulative';
        scoreDisplayType = 'individual';
        break;
      default:
        break;
    }

    const queryParams = {
      calculationType,
      scoreDisplayType,
    };

    // projectId 유효성 검사를 위한 useEffect 수정
    useEffect(() => {
      const validateProjectId = () => {
        // 로컬 스토리지 검사
        const storedProjectId = localStorage.getItem('projectId');

        if (!storedProjectId) {
          throw new ProjectIdError('프로젝트 접근 권한이 없습니다.');
        }

        // URL의 projectId와 저장된 projectId 비교
        if (urlProjectId && storedProjectId !== urlProjectId) {
          throw new ProjectIdError('잘못된 프로젝트에 접근하였습니다.');
        }

        // atom의 projectId 검사 및 업데이트
        if (!projectId) {
          setProjectId(storedProjectId);
        }
      };

      try {
        validateProjectId();
      } catch (err) {
        if (err instanceof Error) {
          setError(err);
        }
      }
    }, [projectId, urlProjectId, setProjectId]);

    // API 호출하는 useEffect들 수정
    useEffect(() => {
      if (!projectId) {
        // throw new ProjectIdError('프로젝트 접근 권한이 없습니다.');
        return;
      }

      const fetchData = async () => {
        try {
          const {
            lineData: fetchedLineData,
            cubicLineData: fetchedCubicLineData,
            maxScore,
            minScore,
          } = await loadChartData(projectId, selectedChart, queryParams);

          if (fetchedLineData) {
            setLineData(fetchedLineData);
          }

          if (fetchedCubicLineData) {
            setCubicLineData(fetchedCubicLineData);
          }

          setMaxValue(maxScore);
          setMinValue(minScore);
        } catch (error) {
          throw error;
        }
      };

      fetchData().catch((error) => {
        if (error instanceof Error) {
          setError(error);
        }
      });
    }, [selectedChart, projectId]);
  }, [selectedChart, projectId]);

  useEffect(() => {
    if (!projectId) {
      return;
    }

    const loadProjectData = async () => {
      try {
        const languageData = await fetchProjectDetail(projectId);
        const teamScoreData = await fetchProjectTeamScore(projectId);

        setProjectDetail(languageData || initialProjectDetail);
        if (teamScoreData) {
          setRadarData(teamScoreData.radarData);
          setProfileIcons(teamScoreData.profileIcons);
          setBadgeIcons(teamScoreData.badgeIcons);
        }
      } catch (error) {
        // 에러는 interceptor에서 처리됨
      }
    };

    loadProjectData();
  }, [projectId]);

  useEffect(() => {
    if (containerRef.current) {
      setContainerHeight(containerRef.current.clientHeight);
    }
  }, []);

  // 에러가 있으면 에러를 던져서 ErrorBoundary가 잡을 수 있게 함
  if (error) {
    throw error;
  }

  return (
    <div className="flex flex-col flex-grow p-4 gap-8 min-w-[1000px] max-w-[2000px] overflow-auto">
      {/* 첫 번째 행 */}
      <div className="flex flex-row min-h-[500px] gap-4">
        {/* Team Radar Charts - 3/5 너비 */}
        <div className="flex flex-col flex-[3] min-w-[570px]">
          <Title title="Team Radar Charts" textSize="text-lg" />
          <div
            ref={containerRef}
            className="flex-grow bg-gray-400 border-2 border-gray-300 rounded-lg flex items-center justify-center p-4"
          >
            <ChartBox
              chartType="radar"
              data={radarData}
              options={createRadarOptions()}
              chartId="radarChart"
              width="500px"
              height="300px"
              profileIcons={profileIcons}
              badgeIcons={badgeIcons}
            />
          </div>
        </div>
        {/* Project Total - 2/5 너비 */}
        <div className="flex flex-col flex-[2] min-w-[380px]">
          <Title title="Project Total" textSize="text-lg" />
          <div className="flex flex-col flex-grow justify-between p-4 bg-gray-400 border-2 border-gray-300 rounded-lg">
            <Title title="About" textSize="text-xl" />
            <Title title="Language" textSize="text-lg" py="py-1" />
            <div className="mx-4">
              {projectDetail.programmingLanguagesData &&
              projectDetail.programmingLanguagesData.labels ? (
                <ChartBox
                  chartType="bar"
                  data={projectDetail.programmingLanguagesData}
                  options={createHorizontalBarOptions([0, 100])}
                  chartId="horizontalBarChart"
                  width="100%"
                  height="60px"
                />
              ) : (
                <p className="text-center">No Chart</p>
              )}
            </div>
            <div className="h-5 px-2 w-full flex flex-col justify-center">
              <div className="bg-gray-600 h-[1px] w-full"></div>
            </div>
            <div className="flex flex-col space-y-5 py-5">
              <Title title="Info" textSize="text-lg" py="py-1" />
              <div className="flex flex-col space-y-5 items-center justify-center">
                <div className="flex flex-row justify-around gap-4">
                  <IconWithText
                    svg={<CommitIcon />}
                    text="commits"
                    count={projectDetail.commitCount}
                  />
                  <IconWithText
                    svg={<BranchIcon className="w-4 h-4" />}
                    text="branches"
                    count={projectDetail.branchCount}
                  />
                </div>
                <div className="flex flex-row justify-around gap-4">
                  <IconWithText
                    svg={<PullRequestIcon className="w-4 h-4" />}
                    text="MRs"
                    count={projectDetail.mergeRequestCount}
                  />
                  <IconWithText
                    svg={<AiIcon />}
                    text="Ai reviews"
                    count={projectDetail.aiReviewCount}
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* 두 번째 행 */}
      <div className="flex flex-row min-h-[450px] gap-4 mb-4">
        {/* Personal Growth Graph - 3/5 너비 */}
        <div className="flex flex-col flex-[3] min-w-[570px]">
          <div className="flex flex-row justify-between items-center">
            <Title title="Personal Growth Graph" textSize="text-lg" />
            <ChartDropdown
              onSelect={(option) => {
                handleSelect(option);
                setSelectedOption(option);
              }}
            />
          </div>
          <div className="flex-grow bg-gray-400 border-2 border-gray-300 rounded-lg items-center justify-center p-4">
            {selectedChart === '획득 통합 스코어' && (
              <ChartBox
                chartType="line"
                data={cubicLineData}
                options={createLineOptions([0, 70])}
                chartId="lineChart"
                height="350px"
              />
            )}
            {selectedChart === '획득 개별 스코어' && (
              <ChartBox
                chartType="line"
                data={lineData}
                options={createLineOptions([0, 11])}
                chartId="cubicLineChart"
                height="350px"
              />
            )}
            {selectedChart === '누적 통합 스코어' && (
              <ChartBox
                chartType="line"
                data={cubicLineData}
                options={createCubicLineOptions([minValue, maxValue])}
                chartId="cumulativeLineChart"
                height="350px"
              />
            )}
            {selectedChart === '누적 개별 스코어' && (
              <ChartBox
                chartType="line"
                data={lineData}
                options={createCubicLineOptions([minValue, maxValue])}
                chartId="cumulativeCubicLineChart"
                height="350px"
              />
            )}
          </div>
        </div>
        {/* Best Merge Request - 2/5 너비 */}
        <div className="flex flex-col flex-[2] min-w-[380px]">
          <BestMergeRequestList />
        </div>
      </div>
    </div>
  );
};

export default HomePage;
