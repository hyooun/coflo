import { Radar, Line, Bar } from 'react-chartjs-2';
import { useEffect, useRef, useState } from 'react';

import {
  Chart as ChartJS,
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  Legend,
  CategoryScale,
  LinearScale,
} from 'chart.js';

ChartJS.register(
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  Legend,
  CategoryScale,
  LinearScale,
);

interface ProfileBadgeProps {
  profileIcons?: Record<number, string>;
  badgeIcons?: Record<number, string>;
  containerHeight?: number;
}

const ChartBox = ({
  chartType,
  data,
  options,
  chartId,
  width = '100%',
  height = '100%',
  plugins,
  profileIcons,
  badgeIcons,
}: ChartBoxProps & ProfileBadgeProps) => {
  const containerRef = useRef<HTMLDivElement>(null);
  const chartRef = useRef<any>(null);
  const observerRef = useRef<ResizeObserver | null>(null);
  const [containerHeight, setContainerHeight] = useState<number>(0);

  useEffect(() => {
    const resizeChart = () => {
      if (chartRef.current) {
        chartRef.current.resize();
      }
    };

    // ResizeObserver 설정
    if (containerRef.current) {
      observerRef.current = new ResizeObserver(() => {
        requestAnimationFrame(resizeChart);
      });

      observerRef.current.observe(containerRef.current);
    }

    // window resize 이벤트도 함께 처리
    window.addEventListener('resize', resizeChart);

    return () => {
      // cleanup
      if (observerRef.current) {
        observerRef.current.disconnect();
      }
      window.removeEventListener('resize', resizeChart);
      if (ChartJS.getChart(chartId)) {
        ChartJS.getChart(chartId)?.destroy();
      }
    };
  }, [chartId]);

  const renderChart = () => {
    const chartProps = {
      ref: chartRef,
      data,
      options: {
        ...options,
        maintainAspectRatio: false,
        responsive: true,
      },
      id: chartId,
      plugins,
    };

    switch (chartType) {
      case 'radar':
        return <Radar {...chartProps} />;
      case 'line':
        return <Line {...chartProps} />;
      case 'bar':
        return <Bar {...chartProps} />;
      default:
        return null;
    }
  };

  const renderProfileBadges = () => {
    if (!profileIcons || !badgeIcons) return null;

    const totalUsers = Object.keys(profileIcons).length;
    const fixedSpacing = 22;
    const offset = totalUsers < 1 ? 0 : (containerHeight - totalUsers * fixedSpacing) / 2;

    return Object.entries(profileIcons).map(([userId, profileUrl], index) => (
      <div
        key={`user-${userId}`}
        className="absolute"
        style={{
          top: offset + index * fixedSpacing,
          right: '20px',
        }}
      >
        <img src={profileUrl} alt={`User ${userId} Profile`} className="w-5 h-5 rounded-full" />
        {badgeIcons[parseInt(userId)] && (
          <img
            src={badgeIcons[parseInt(userId)]!}
            alt={`User ${userId} Badge`}
            className="w-6 h-6 absolute -top-1 left-5"
          />
        )}
      </div>
    ));
  };

  useEffect(() => {
    if (containerRef.current) {
      setContainerHeight(containerRef.current.clientHeight);
    }
  }, []);

  return (
    <div
      ref={containerRef}
      style={{ width, height, position: 'relative' }}
      className="w-full h-full"
    >
      {renderChart()}
      {renderProfileBadges()}
    </div>
  );
};

export default ChartBox;
