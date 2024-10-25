import Navbar from '@components/Nav/Navbar/Navbar';
import { ReactNode } from 'react';

interface CommonLayoutProps {
  children: ReactNode;
}

const CommonLayout = ({ children }: CommonLayoutProps) => {
  return (
    <div className="flex flex-row h-screen w-full">
      <Navbar />
      {children}
    </div>
  );
};

export default CommonLayout;
