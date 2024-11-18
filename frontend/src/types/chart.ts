interface ChartBoxProps {
  chartType: 'radar' | 'line' | 'bar';
  data: any;
  options: any;
  chartId: any;
  width?: string;
  height?: string;
  plugins?: any[];
}

interface BarChartProps {
  data: { label: string; value: number }[];
  width: number;
  height: number;
}
