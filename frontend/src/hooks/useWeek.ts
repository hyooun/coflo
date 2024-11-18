const getPreviousWeekDates = () => {
  const today = new Date();
  const dayOfWeek = today.getDay();

  const diffToPreviousMonday = dayOfWeek === 0 ? -6 : 1 - dayOfWeek - 7;

  const previousMonday = new Date(today);
  previousMonday.setDate(today.getDate() + diffToPreviousMonday);

  const previousSunday = new Date(previousMonday);
  previousSunday.setDate(previousMonday.getDate() + 6);

  const formatDate = (date: Date) => date.toISOString().split('T')[0];

  return {
    monday: formatDate(previousMonday),
    sunday: formatDate(previousSunday),
  };
};

export const { monday, sunday } = getPreviousWeekDates();

const getStartOfWeekAndToday = () => {
  const today = new Date();
  const dayOfWeek = today.getDay();

  const diffToStartOfWeek = dayOfWeek === 0 ? -6 : 1 - dayOfWeek;

  const startOfWeek = new Date(today);
  startOfWeek.setDate(today.getDate() + diffToStartOfWeek);

  const formatDate = (date: Date) => date.toISOString().split('T')[0];

  return {
    startOfWeek: formatDate(startOfWeek),
    today: formatDate(today),
  };
};

export const { startOfWeek, today } = getStartOfWeekAndToday();
