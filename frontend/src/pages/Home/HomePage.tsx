import ChartBox from '@components/ChartBox/ChartBox';
import { useState } from 'react';
import 'chart.js/auto';
import IconWithText from '@components/TextDiv/IconWithText';
import { BranchIcon } from '@components/TextDiv/Icons/BranchIcon';
import { PullRequestIcon } from '@components/TextDiv/Icons/PullRequestIcon';
import { CommitIcon } from '@components/TextDiv/Icons/CommitIcon';
import { AiIcon } from '@components/TextDiv/Icons/AiIcon';

import {
  horizontalBarOptions,
  radarOptions,
  lineOptions,
  cubicLineOptions,
} from '@constants/chartOptions';
import Title from '@components/Title/Title';
import BestMergeRequestList from '@components/Home/BestMergeRequest/BestMergeRequestList.tsx';

const HomePage = () => {
  const [selectedChart, setSelectedChart] = useState<'line' | 'cubic'>('line');

  const radarData = {
    labels: ['가독성', 'Public Speaking', 'Development', 'Hard Skill', 'Soft Skill', 'Resources'],
    datasets: [
      {
        label: '구승석',
        data: [65, 59, 90, 81, 56, 55],
        borderColor: '#BB92F6',
        backgroundColor: 'transparent',
      },
      {
        label: '김형민',
        data: [20, 10, 50, 31, 40, 15],
        borderColor: '#B8E314',
        backgroundColor: 'transparent',
      },
    ],
  };

  const programmingLanguagesData = {
    labels: ['languages'],
    datasets: [
      {
        label: 'JavaScript',
        data: [20],
        backgroundColor: 'rgba(255, 99, 132, 0.2)',
        borderColor: 'transparent',
        barThickness: 10,
        borderSkipped: false,
        borderRadius: [{ topLeft: 20, topRight: 0, bottomLeft: 20, bottomRight: 0 }],
      },
      {
        label: 'TypeScript',
        data: [30],
        barThickness: 10,
        backgroundColor: 'rgba(54, 162, 235, 0.2)',
      },
      {
        label: 'Python',
        data: [10],
        barThickness: 10,
        backgroundColor: 'rgba(255, 206, 86, 0.2)',
      },
      {
        label: 'Java',
        data: [40],
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        barThickness: 10,
        borderRadius: [{ topLeft: 0, topRight: 20, bottomLeft: 0, bottomRight: 20 }],
      },
    ],
  };

  const lineData = {
    labels: ['1주차', '2주차', '3주차', '4주차', '5주차', '6주차', '7주차'],
    datasets: [
      {
        label: '김형민',
        data: [65, 59, 80, 81, 56, 55, 40],
        borderColor: 'rgba(255, 99, 132, 1)',
        backgroundColor: 'rgba(255, 99, 132, 0.2)',
      },
      // {
      //   label: '구승석',
      //   data: [28, 48, 40, 19, 86, 27, 90],
      //   borderColor: 'rgba(54, 162, 235, 1)',
      //   backgroundColor: 'rgba(54, 162, 235, 0.2)',
      // },
    ],
  };

  const cubicLineData = {
    labels: ['1주차', '2주차', '3주차', '4주차', '5주차', '6주차', '7주차'],
    datasets: [
      {
        label: '김형민',
        data: [45, 25, 60, 75, 95, 80, 50],
        borderColor: 'rgba(75, 192, 192, 1)',
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        tension: 0.4,
      },
    ],
  };

  return (
    <div className="flex flex-col flex-grow overflow-auto px-8 pt-6">
      <div className="flex flex-row ml-4">
        <div className="flex flex-col mr-4">
          <Title title="Team Radar Charts" />
          <div className="w-[600px] h-[320px] bg-[#F5F7FA] m-2 flex items-center justify-center ">
            <ChartBox
              chartType="radar"
              data={radarData}
              options={radarOptions}
              chartId="radarChart"
              width={600}
              height={300}
            />
          </div>
        </div>
        <div className="flex flex-col">
          <Title title="Project Total" />
          <div className="w-[350px] h-[320px] bg-[#F5F7FA] m-2 flex  flex-col ">
            <Title title="About" textSize="text-lg" px="px-1" py="py-1" ml="ml-6" mt="mt-3" />
            <Title title="Language" px="px-1" py="py-1" ml="ml-8" />
            <div className="ml-7">
              <ChartBox
                chartType="bar"
                data={programmingLanguagesData}
                options={horizontalBarOptions}
                chartId="horizontalBarChart"
                width={300}
                height={100}
              />
            </div>
            <div className="h-5 pr-4 pl-4 w-full flex flex-col justify-center">
              <div className="bg-[#CCCCCC] h-[1px] w-full"></div>
            </div>
            <Title title="Info" px="px-1" py="py-1" ml="ml-8" />
            <div className="flex flex-row m-4 ml-8 gap-4">
              <IconWithText svg={<CommitIcon />} text="commits" count={2512} />
              <IconWithText svg={<BranchIcon />} text="branches" count={7} />
            </div>
            <div className="flex flex-row m-2 ml-8 gap-4">
              <IconWithText svg={<PullRequestIcon className="w-4 h-4" />} text="MRs" count={224} />
              <IconWithText svg={<AiIcon />} text="Ai reviews" count={211} />
            </div>
          </div>
        </div>
      </div>

      <div className="flex">
        <div className="flex flex-row ml-4">
          <div className="flex flex-col mr-4">
            <div className="flex flex-row justify-between">
              <Title title="Personal Growth Graph" />

              <div className="ml-6 mb-4 flex flex-row space-x-2">
                <button
                  onClick={() => setSelectedChart('line')}
                  className={`px-4 py-2 ${selectedChart === 'line' ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
                >
                  주간 변화
                </button>
                <button
                  onClick={() => setSelectedChart('cubic')}
                  className={`px-4 py-2 ${selectedChart === 'cubic' ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
                >
                  주간 누적 점수
                </button>
              </div>
            </div>
            <div className="w-[600px] h-[320px] bg-[#F5F7FA] m-2 flex items-center justify-center ">
              {selectedChart === 'line' && (
                <ChartBox
                  chartType="line"
                  data={lineData}
                  options={lineOptions}
                  chartId="lineChart"
                />
              )}
              {selectedChart === 'cubic' && (
                <ChartBox
                  chartType="line"
                  data={cubicLineData}
                  options={cubicLineOptions}
                  chartId="cubicLineChart"
                />
              )}
            </div>
          </div>
          <div className="w-[350px] h-[350px]">
            <BestMergeRequestList />
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
