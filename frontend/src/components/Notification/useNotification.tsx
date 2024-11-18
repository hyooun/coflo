import { useEffect, useRef } from 'react';
import { useSetAtom } from 'jotai';
import { notificationAtom } from '@store/auth';

export const useNotification = (projectId: string) => {
  const setNotification = useSetAtom(notificationAtom);

  const eventSourceRef = useRef<EventSource | null>(null);

  const notify = () => {
    console.log('Notification triggered');
  };

  const openEvtSource = () => {
    const eventSource = new EventSource(`https://www.coflo.co.kr/api/sse/subscribe?${projectId}`, {
      withCredentials: true,
    });

    eventSourceRef.current = eventSource;

    eventSource.addEventListener('notification', (event: MessageEvent) => {
      console.log('Received notification:', event);

      const message = event.data;
      if (message.includes('EventStream Created')) {
        console.log('Ignoring EventStream Created message:', message);
        return;
      }

      setNotification(message);
      window.location.reload();
    });

    eventSource.onerror = async (err) => {
      console.error('EventSource failed:', err);

      closeEvtSource();

      console.log('Reconnecting...');
      openEvtSource();
    };
  };

  const closeEvtSource = () => {
    if (eventSourceRef.current) {
      eventSourceRef.current.close();
      eventSourceRef.current = null;
    }
  };

  useEffect(() => {
    openEvtSource();

    return () => {
      closeEvtSource();
    };
  }, [projectId, setNotification]);

  return { notify };
};
