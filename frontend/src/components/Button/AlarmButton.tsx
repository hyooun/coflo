import { useState, useEffect, useRef } from 'react';
import { BellIcon } from '@components/Sidebar/Icons/Bell';
import { Toast } from '@components/Common/Toast';

interface Alarm {
  id: number;
  message: string;
  timeAgo: string;
  read: boolean;
}

interface AlarmButtonProps {
  count: number;
  active: boolean;
}

export const AlarmButton = ({ active }: AlarmButtonProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const [alarms, setAlarms] = useState<Alarm[]>([]);
  const [count, setCount] = useState(0);
  const [toastMessage, setToastMessage] = useState('');
  const popupRef = useRef<HTMLDivElement>(null);
  const buttonRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    // 알람 목록을 불러오는 API 호출
    const mockData = [
      { id: 1, message: '정복자 뱃지를 얻었습니다', timeAgo: '1분 전', read: false },
      { id: 2, message: '리뷰가 재생성 되었어요!', timeAgo: '1분 전', read: false },
      { id: 3, message: '리뷰가 재생성 되었어요!', timeAgo: '1분 전', read: false },
      { id: 4, message: '리뷰가 재생성 되었어요!', timeAgo: '1분 전', read: false },
      { id: 5, message: '리뷰가 재생성 되었어요!', timeAgo: '1분 전', read: false },
      { id: 6, message: '리뷰가 재생성 되었어요!', timeAgo: '1분 전', read: false },
      { id: 7, message: '리뷰가 재생성 되었어요!', timeAgo: '1분 전', read: false },
      { id: 8, message: '리뷰가 재생성 되었어요!', timeAgo: '1분 전', read: false },
      { id: 9, message: '리뷰가 재생성 되었어요!', timeAgo: '1분 전', read: false },
      { id: 10, message: '리뷰가 재생성 되었어요!', timeAgo: '1분 전', read: false },
      // 추가 목업 데이터
    ];
    setAlarms(mockData);
    setCount(mockData.filter((alarm) => !alarm.read).length);
  }, []);

  const handleNewAlarm = (newAlarm: Alarm) => {
    setAlarms((prevAlarms) => [newAlarm, ...prevAlarms]);
    setCount((prevCount) => prevCount + 1);
    setToastMessage(newAlarm.message);
  };

  const handleAlarmClick = (id: number) => {
    setAlarms((prevAlarms) =>
      prevAlarms.map((alarm) => (alarm.id === id ? { ...alarm, read: true } : alarm)),
    );

    setCount((prevCount) => prevCount - 1);
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (buttonRef.current?.contains(event.target as Node)) {
        return;
      }

      if (popupRef.current && !popupRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const bgColor = isOpen || active ? 'bg-[#ebecf0]' : 'bg-[#F5F7FA]';
  const textColor = isOpen || active ? 'secondary' : 'primary-500';

  return (
    <div className="relative w-full">
      <div
        ref={buttonRef}
        onClick={() => setIsOpen(!isOpen)}
        className={`font-pretentard flex flex-row items-center rounded-[3px] w-full h-10 ${bgColor} text-${textColor} px-3 py-2 select-none hover:cursor-pointer`}
      >
        <BellIcon className={`text-${textColor}`} />
        <div className="pl-3 flex items-center">
          Alarm
          {count > 0 && (
            <span className={`ml-2 text-white rounded-full px-2 py-0.5 text-xs bg-${textColor}`}>
              {count}
            </span>
          )}
        </div>
      </div>

      {isOpen && (
        <div
          className="absolute top-0 left-60 w-[300px] bg-white shadow-lg rounded-md border border-gray-200 z-50 max-h-[250px] overflow-y-auto"
          ref={popupRef}
        >
          <div className="px-4">
            {alarms.map((alarm) => (
              <div
                key={alarm.id}
                className="flex items-center my-4 cursor-pointer px-2"
                onClick={() => handleAlarmClick(alarm.id)}
              >
                <span
                  className={`flex-1 truncate text-lg ${alarm.read ? 'text-gray-600' : 'text-primary-500 font-bold'}`}
                >
                  {alarm.message}
                </span>
                <span className={`text-xs ml-2 ${alarm.read ? 'text-gray-600' : 'text-gray-900'}`}>
                  {alarm.timeAgo}
                </span>
              </div>
            ))}
          </div>
        </div>
      )}

      {toastMessage && <Toast message={toastMessage} onClose={() => setToastMessage('')} />}
    </div>
  );
};
