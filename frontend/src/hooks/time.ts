import { useMemo } from 'react';

function useTimeAgo(date: string) {
  const timeAgo = useMemo(() => {
    if (!date) return '';
    const parsedDate = new Date(date);
    return getTimeAgoText(parsedDate);
  }, [date]);

  return timeAgo;
}

function getTimeAgoText(date: Date) {
  const now = new Date();
  const diffInSeconds = Math.floor((now.getTime() - date.getTime()) / 1000);

  if (diffInSeconds < 300) return 'just now';
  const diffInMinutes = Math.floor(diffInSeconds / 60);
  if (diffInMinutes < 60) return `${diffInMinutes} minutes ago`;

  const diffInHours = Math.floor(diffInMinutes / 60);
  if (diffInHours < 24) return `${diffInHours} hours ago`;

  const diffInDays = Math.floor(diffInHours / 24);
  if (diffInDays < 7) return `${diffInDays} days ago`;
  if (diffInDays < 30) return `${Math.floor(diffInDays / 7)} weeks ago`;

  const diffInMonths = Math.floor(diffInDays / 30);
  if (diffInMonths < 12) return `${diffInMonths} months ago`;

  const diffInYears = Math.floor(diffInMonths / 12);
  return `${diffInYears} years ago`;
}

export default useTimeAgo;
