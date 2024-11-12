import { Radar, Line, Bar } from 'react-chartjs-2';
import { useEffect, useRef } from 'react';

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

const ChartBox = ({
  chartType,
  data,
  options,
  chartId,
  width = '100%',
  height = '100%',
  plugins,
}: ChartBoxProps) => {
  const containerRef = useRef<HTMLDivElement>(null);
  const chartRef = useRef<any>(null);
  const observerRef = useRef<ResizeObserver | null>(null);

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

  return (
    <div
      ref={containerRef}
      style={{ width, height, position: 'relative' }}
      className="w-full h-full"
    >
      {renderChart()}
    </div>
  );
};

export default ChartBox;
