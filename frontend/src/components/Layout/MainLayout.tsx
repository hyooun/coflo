import { useEffect } from 'react';
import { Sidebar } from '@components/Sidebar/Sidebar';
import CommonLayout from './CommonLayout';
import { Outlet } from 'react-router-dom';
import { useNotification } from '@components/Notification/useNotification';
import Notification from '@components/Notification/Notification';

const MainLayout = () => {
  const { notify } = useNotification();

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
