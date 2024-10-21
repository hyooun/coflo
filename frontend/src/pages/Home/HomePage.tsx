import ChartBox from '@components/ChartBox/ChartBox';
import { useState } from 'react';

const HomePage = () => {
  const [selectedChart, setSelectedChart] = useState<'line' | 'cubic'>('line');

  const radarData = {
    labels: [
      '가독성',
      'Public Speaking',
      'Development',
      'Hard Skill',
      'Soft Skill',
      'Resources',
    ],
    datasets: [
      {
        label: '구승석',
        data: [65, 59, 90, 81, 56, 55],
        borderColor: '#BB92F6',
      },
      {
        label: '김형민',
        data: [20, 10, 50, 31, 40, 15],
        borderColor: '#B8E314',
      },
    ],
  };

  const radarOptions = {
    responsive: true,
    scales: {
      r: {
        angleLines: {
          color: 'gray',
        },
        grid: {
          color: 'gray',
        },
        pointLabels: {
          color: 'gray',
        },
        ticks: {
          color: 'gray',
          backdropColor: 'transparent',
        },
      },
    },
    plugins: {
      title: {
        display: false,
      },
    },
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

  const lineOptions = {
    responsive: true,
    plugins: {
      title: {
        display: false,
      },
    },
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

  const cubicLineOptions = {
    responsive: true,
    plugins: {
      title: {
        display: false,
      },
    },
    interaction: {
      intersect: false,
    },
    scales: {
      x: {
        display: true,
        title: {
          display: true,
        },
      },
      y: {
        display: true,
        title: {
          display: true,
          text: 'Value',
        },
        suggestedMin: 0,
        suggestedMax: 100,
      },
    },
  };

  return (
    <div className="flex flex-col flex-grow overflow-auto px-8 pt-6">
      <div className="flex flex-row ml-4">
        <ChartBox chartType="radar" data={radarData} options={radarOptions} />
        <div className="w-[350px] h-[300px] bg-[#F5F7FA] m-2"></div>
      </div>

      <div className="flex">
        <div className="flex flex-col">
          <div className="ml-6 mb-4">
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
          <div className="flex flex-row ml-4">
            {selectedChart === 'line' && (
              <ChartBox
                chartType="line"
                data={lineData}
                options={lineOptions}
              />
            )}
            {selectedChart === 'cubic' && (
              <ChartBox
                chartType="line"
                data={cubicLineData}
                options={cubicLineOptions}
              />
            )}
            <div className="w-[350px] h-[300px] bg-[#F5F7FA] m-2"></div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
