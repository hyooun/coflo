import { useEffect } from 'react';
import { useAtomValue, useSetAtom } from 'jotai';
import { notificationAtom } from '@store/auth';

interface ToastProps {
  message: string;
  onClose: () => void;
}

const Toast = ({ message, onClose }: ToastProps) => {
  useEffect(() => {
    const timer = setTimeout(onClose, 5000);
    return () => clearTimeout(timer);
  }, [onClose]);

  return (
    <div className="fixed bottom-4 right-4 w-[300px] h-[200px] bg-primary-500 text-white p-4 rounded shadow-lg z-50 flex flex-col">
      <div className="text-left font-semibold">알림</div>
      <div className="flex-grow flex items-center justify-center text-center">{message}</div>
    </div>
  );
};

const Notification = () => {
  const notification = useAtomValue(notificationAtom);
  const setNotification = useSetAtom(notificationAtom);

  if (!notification) return null;

  return <Toast message={notification} onClose={() => setNotification(null)} />;
};

export default Notification;
