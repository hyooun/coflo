interface ChartBoxProps {
  chartType: 'radar' | 'line' | 'bar';
  data: any;
  options: any;
  chartId: any;
  width?: number;
  height?: number;
}

interface BarChartProps {
  data: { label: string; value: number }[];
  width: number;
  height: number;
}
