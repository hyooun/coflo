import { Outlet } from 'react-router-dom';
import { Sidebar } from '@components/Sidebar/Sidebar';
import CommonLayout from './CommonLayout';

const MainLayout = () => {
  return (
    <CommonLayout>
      <Sidebar />
      <Outlet />
    </CommonLayout>
  );
};

export default MainLayout;
