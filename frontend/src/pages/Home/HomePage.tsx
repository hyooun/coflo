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
  const [projectId] = useAtom(projectIdAtom);

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

    const fetchData = async () => {
      if (!projectId) return;
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
    };

    fetchData();
  }, [selectedChart, projectId]);

  useEffect(() => {
    if (!projectId) return;
    const loadProjectData = async () => {
      const langauageData = await fetchProjectDetail(projectId);

      const teamScoreData = await fetchProjectTeamScore(projectId);

      setProjectDetail(langauageData || initialProjectDetail);
      if (teamScoreData) {
        setRadarData(teamScoreData.radarData);
        setProfileIcons(teamScoreData.profileIcons);
        setBadgeIcons(teamScoreData.badgeIcons);
      }
    };

    loadProjectData();
  }, [projectId]);

  useEffect(() => {
    if (containerRef.current) {
      setContainerHeight(containerRef.current.clientHeight);
    }
  }, []);

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
