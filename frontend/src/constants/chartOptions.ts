const getMinMaxValues = (data: number[]) => {
  const minValue = Math.min(...data);
  const maxValue = Math.max(...data);
  return { min: minValue, max: maxValue };
};

export const createHorizontalBarOptions = (data: number[]) => {
  const { min, max } = getMinMaxValues(data);

  return {
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
      suggestedMin: min,
      suggestedMax: max,
    },
  };
};

export const createRadarOptions = () => {
  return {
    responsive: true,
    layout: {
      padding: {
        right: 50,
      },
    },
    scales: {
      r: {
        angleLines: {
          color: 'rgba(128, 128, 128, 0.2)',
        },
        grid: {
          color: 'rgba(128, 128, 128, 0.2)',
        },
        pointLabels: {
          color: 'gray',
        },
        ticks: {
          color: 'gray',
          backdropColor: 'transparent',
          stepSize: 2,
        },
        beginAtZero: true,
        min: 0,
        max: 10,
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
};

export const createLineOptions = (data: number[]) => {
  const { min, max } = getMinMaxValues(data);

  return {
    responsive: true,
    plugins: {
      title: {
        display: false,
      },
      legend: {
        display: false,
      },
    },
    scales: {
      x: {
        suggestedMin: min,
        suggestedMax: max,
      },
      y: {
        suggestedMin: min,
        suggestedMax: max,
      },
    },
  };
};

export const createCubicLineOptions = (data: number[]) => {
  const { min, max } = getMinMaxValues(data);

  return {
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
        suggestedMin: min,
        suggestedMax: max,
      },
    },
  };
};
