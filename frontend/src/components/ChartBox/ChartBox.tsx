import { Radar, Line } from 'react-chartjs-2';
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

const ChartBox = ({ chartType, data, options }: ChartBoxProps) => {
  const renderChart = () => {
    switch (chartType) {
      case 'radar':
        return (
          <Radar
            data={data}
            options={{ ...options, maintainAspectRatio: false }}
          />
        );
      case 'line':
        return (
          <Line
            data={data}
            options={{ ...options, maintainAspectRatio: false }}
          />
        );
      default:
        return null;
    }
  };

  return (
    <div className="w-[600px] h-[300px] bg-[#F5F7FA] m-2 flex items-center justify-center">
      {renderChart()}
    </div>
  );
};

export default ChartBox;
