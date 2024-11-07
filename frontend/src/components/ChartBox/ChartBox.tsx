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
  width = 500,
  height = 300,
  plugins,
}: ChartBoxProps) => {
  const chartRef = useRef<any>(null);

  useEffect(() => {
    return () => {
      if (ChartJS.getChart(chartId)) {
        ChartJS.getChart(chartId)?.destroy();
      }
    };
  }, [chartId]);

  const renderChart = () => {
    const chartProps = {
      ref: chartRef,
      data,
      options: { ...options, maintainAspectRatio: false },
      id: chartId,
      width,
      height,
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

  return <div style={{ width, height }}>{renderChart()}</div>;
};

export default ChartBox;
