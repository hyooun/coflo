import { useState, useEffect, useRef } from 'react';
import { BellIcon } from '@components/Sidebar/Icons/Bell';
import { Toast } from '@components/Common/Toast';
import { Notification } from '@apis/Notification';
import { NotificationResponse } from 'types/notification';
import { useAtomValue } from 'jotai';
import { projectIdAtom } from '@store/auth';

export const AlarmButton = () => {
  const [isOpen, setIsOpen] = useState(false);
  const projectId = useAtomValue(projectIdAtom);
  const [alarms, setAlarms] = useState<NotificationResponse[]>([]);
  const [count, setCount] = useState<number>(0);

  const [toastMessage, setToastMessage] = useState('');
  const popupRef = useRef<HTMLDivElement>(null);
  const buttonRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const fetchNotifications = async () => {
      if (!projectId) return;
      const response = await Notification.getNotification(projectId);
      console.log(response.data);
      if (response.data) {
        setAlarms(response.data);
      }
    };

    fetchNotifications();
  }, [projectId]);

  useEffect(() => {
    const fetchUnreadCount = async () => {
      if (!projectId) return;
      const response = await Notification.getUnreadNotificationCount(projectId);
      console.log(response);
      if (response.data) {
        setCount(response.data.unreadCount);
        setNotificationCount(response.data.unreadCount);
      }
    };

    fetchUnreadCount();
  }, [projectId]);

  const handleAlarmClick = async (id: string) => {
    setAlarms((prevAlarms) =>
      prevAlarms.map((alarm) => {
        if (alarm.id === id && !alarm.isRead) {
          setCount((prevCount) => prevCount - 1);
          setNotificationCount((prevCount) => prevCount - 1);
          return { ...alarm, isRead: true };
        }
        return alarm;
      }),
    );
    await Notification.patchNotificationReadStatus(id);
    setToastMessage('알림이 읽음 상태로 변경되었습니다.');
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

  return (
    <div className="relative w-full">
      <div
        ref={buttonRef}
        onClick={() => setIsOpen(!isOpen)}
        className="font-pretentard flex flex-row items-center rounded-[3px] w-full h-10 bg-[#F5F7FA] text-primary-500 px-3 py-2 select-none hover:bg-[#ebecf0] hover:text-secondary cursor-pointer"
      >
        <BellIcon className="text-primary-500 hover:text-secondary" />
        <div className="pl-3 flex items-center">
          Alarm
          {count > 0 && (
            <span className="ml-2 text-white rounded-full px-2 py-0.5 text-xs bg-primary-500">
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
            {alarms.map((alarm) => {
              return (
                <div
                  key={alarm.id}
                  className="flex items-center my-4 cursor-pointer px-2"
                  onClick={() => handleAlarmClick(alarm.id)}
                >
                  <span
                    className={`flex-1 truncate text-lg ${alarm.isRead ? 'text-gray-600' : 'text-primary-500 font-bold'}`}
                  >
                    {alarm.content}
                  </span>
                  <span
                    className={`text-xs ml-2 ${alarm.isRead ? 'text-gray-600' : 'text-gray-800'}`}
                  >
                    {alarm.createdDate}
                  </span>
                </div>
              );
            })}
          </div>
        </div>
      )}

      {toastMessage && <Toast message={toastMessage} onClose={() => setToastMessage('')} />}
    </div>
  );
};
