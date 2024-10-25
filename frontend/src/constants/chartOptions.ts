export const horizontalBarOptions = {
  responsive: true,
  indexAxis: 'y',
  plugins: {
    title: {
      display: false,
    },
    legend: {
      position: 'bottom',
    },
  },
  scales: {
    x: {
      stacked: true,
      display: false,
    },
    y: {
      stacked: true,
      display: false,
    },
    suggestedMin: 0,
    suggestedMax: 100,
  },
};

export const radarOptions = {
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
    legend: {
      position: 'right',
    },
  },
};

export const lineOptions = {
  responsive: true,
  plugins: {
    title: {
      display: false,
    },
    legend: {
      display: false,
    },
  },
};

export const cubicLineOptions = {
  responsive: true,
  plugins: {
    title: {
      display: false,
    },
    legend: {
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
