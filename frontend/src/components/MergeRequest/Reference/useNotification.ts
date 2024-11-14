import { useEffect, useRef } from 'react';
import { useSetAtom } from 'jotai';
import { notificationAtom } from '@store/auth';

export const useNotification = (projectId: string, setLoading: (loading: boolean) => void) => {
  const setNotification = useSetAtom(notificationAtom);
  const eventSourceRef = useRef<EventSource | null>(null);

  const notify = () => {
    console.log('Notification triggered');
  };

  useEffect(() => {
    const eventSource = new EventSource(`https://www.coflo.co.kr/api/sse/subscribe`, {
      withCredentials: true,
    });

    console.log('EventSource created:', eventSource);

    eventSourceRef.current = eventSource;

    eventSource.addEventListener('notification', (event: MessageEvent) => {
      console.log('Received notification:', event);

      const message = event.data;
      if (message.includes('EventStream Created')) {
        console.log('Ignoring EventStream Created message:', message);
        return;
      }

      setNotification(message);
      setLoading(false);
    });

    return () => {
      eventSource.close();
    };
  }, [setNotification, setLoading]);

  return { notify };
};
