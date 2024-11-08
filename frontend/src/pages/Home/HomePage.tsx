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
  const [containerHeight, setContainerHeight] = useState<number>(0);

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
  const totalUsers = Object.keys(profileIcons).length;
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
        console.log(fetchedLineData);
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

  const fixedSpacing = 22;
  const offset = totalUsers < 1 ? 0 : (containerHeight - totalUsers * fixedSpacing) / 2 + 87;

  useEffect(() => {
    if (containerRef.current) {
      setContainerHeight(containerRef.current.clientHeight);
    }
  }, []);

  return (
    <div className="flex flex-col flex-grow overflow-auto px-8 pt-6">
      <div className="flex flex-row ml-4">
        <div className="flex flex-col mr-8">
          <Title title="Team Radar Charts" />
          <div
            ref={containerRef}
            className="w-[680px] h-[320px]  bg-gray-400 border-2 border-gray-500 rounded-lg m-2 flex items-center justify-center "
          >
            <ChartBox
              chartType="radar"
              data={radarData}
              options={createRadarOptions()}
              chartId="radarChart"
              width={500}
              height={300}
            />
            {Object.entries(profileIcons).map(([userId, profileUrl], index) => (
              <div
                key={`user-${userId}`}
                className="absolute"
                style={{
                  top: offset + index * fixedSpacing,
                  left: 910,
                }}
              >
                <img
                  src={profileUrl}
                  alt={`User ${userId} Profile`}
                  style={{ width: 20, height: 20 }}
                />

                {badgeIcons[parseInt(userId)] && (
                  <img
                    src={badgeIcons[parseInt(userId)]!}
                    alt={`User ${userId} Badge`}
                    style={{
                      width: 20,
                      height: 20,
                      marginLeft: 5,
                      position: 'absolute',
                      top: 0,
                      left: 25,
                    }}
                  />
                )}
              </div>
            ))}
          </div>
        </div>
        <div className="flex flex-col">
          <Title title="Project Total" />
          <div className="w-[420px] h-[320px] p-2 bg-gray-400 border-2 border-gray-500 rounded-lg m-2 flex  flex-col ">
            <Title title="About" textSize="text-lg" px="px-1" py="py-1" ml="ml-6" mt="mt-3" />
            <Title title="Language" px="px-1" py="py-1" ml="ml-8" />
            <div className="ml-16">
              {projectDetail.programmingLanguagesData &&
              projectDetail.programmingLanguagesData.labels ? (
                <ChartBox
                  chartType="bar"
                  data={projectDetail.programmingLanguagesData}
                  options={createHorizontalBarOptions([0, 100])}
                  chartId="horizontalBarChart"
                  width={300}
                  height={100}
                />
              ) : (
                <p>No Chart</p>
              )}
            </div>
            <div className="h-5 pr-4 pl-4 w-full flex flex-col justify-center">
              <div className="bg-gray-600 h-[1px] w-full"></div>
            </div>
            <Title title="Info" px="px-1" py="py-1" ml="ml-8" />
            <div className="flex flex-row m-4 ml-8 gap-4">
              <IconWithText svg={<CommitIcon />} text="commits" count={projectDetail.commitCount} />
              <IconWithText
                svg={<BranchIcon className="w-4 h-4" />}
                text="branches"
                count={projectDetail.branchCount}
              />
            </div>
            <div className="flex flex-row m-2 ml-8 gap-4">
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

      <div className="flex">
        <div className="flex flex-row ml-4">
          <div className="flex flex-col mr-8">
            <div className="flex flex-row justify-between">
              <Title title="Personal Growth Graph" />

              <ChartDropdown
                onSelect={(option) => {
                  handleSelect(option);
                  setSelectedOption(option);
                }}
              />
            </div>
            <div className="w-[680px] h-[320px]  bg-gray-400 border-2 border-gray-500 rounded-lg m-2 flex items-center justify-center ">
              {selectedChart === '획득 통합 스코어' && (
                <ChartBox
                  chartType="line"
                  data={cubicLineData}
                  options={createLineOptions([0, 70])}
                  chartId="lineChart"
                />
              )}
              {selectedChart === '획득 개별 스코어' && (
                <ChartBox
                  chartType="line"
                  data={lineData}
                  options={createLineOptions([0, 11])}
                  chartId="cubicLineChart"
                />
              )}
              {selectedChart === '누적 통합 스코어' && (
                <ChartBox
                  chartType="line"
                  data={cubicLineData}
                  options={createCubicLineOptions([minValue, maxValue])}
                  chartId="cumulativeLineChart"
                />
              )}
              {selectedChart === '누적 개별 스코어' && (
                <ChartBox
                  chartType="line"
                  data={lineData}
                  options={createCubicLineOptions([minValue, maxValue])}
                  chartId="cumulativeCubicLineChart"
                />
              )}
            </div>
          </div>
          <div className="w-[420px] h-[320px] mt-6 ml-2 ">
            <BestMergeRequestList />
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
