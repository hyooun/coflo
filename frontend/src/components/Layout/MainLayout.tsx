import { useEffect } from 'react';
import { Sidebar } from '@components/Sidebar/Sidebar';
import CommonLayout from './CommonLayout';
import { Outlet } from 'react-router-dom';
import { useNotification } from '@components/Notification/useNotification';
import Notification from '@components/Notification/Notification';
import { projectIdAtom } from '@store/auth';
import { useAtomValue } from 'jotai';

const MainLayout = () => {
  const projectId = useAtomValue(projectIdAtom);
  const { notify } = useNotification(projectId || '');

  useEffect(() => {
    notify();
  }, [notify]);

  return (
    <CommonLayout>
      <Sidebar />
      <Notification />
      <Outlet />
    </CommonLayout>
  );
};

export default MainLayout;
