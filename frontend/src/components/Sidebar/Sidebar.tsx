import {
  HomeIcon,
  SettingIcon,
  MergeListIcon,
  BadgeIcon,
  CustomPromptIcon,
} from '@components/Sidebar/Icons/index';
import { NavButton } from '@components/Button/NavButton';
import { Title } from '@components/Sidebar/Title';
import { Link, useLocation } from 'react-router-dom';

export const Sidebar = () => {
  const location = useLocation();

  return (
    <div className="h-screen min-w-[250px] max-w-[250px] border-r border-r-solid border-r-[#dfe1e6] bg-[#f4f5f7] flex flex-col items-center p-4 font-pretendard text-sm leading-[1.2] ">
      <Title />
      <Link to="/main" className="w-full">
        <NavButton active={location.pathname === '/main'} icon={<HomeIcon />}>
          Home
        </NavButton>
      </Link>
      <Link to="/main/merge-request" className="w-full">
        <NavButton active={location.pathname === '/main/merge-request'} icon={<MergeListIcon />}>
          Merge Request
        </NavButton>
      </Link>
      <Link to="/main/custom-prompt" className="w-full">
        <NavButton active={location.pathname === '/main/custom-prompt'} icon={<CustomPromptIcon />}>
          Custom Prompt
        </NavButton>
      </Link>
      <div className="h-10 w-full flex flex-col justify-center">
        <div className="bg-[#c1c7d0] h-[1px] w-full"></div>
      </div>
      <Link to="/main/badge" className="w-full">
        <NavButton active={location.pathname === '/main/badge'} icon={<BadgeIcon />}>
          My Badge
        </NavButton>
      </Link>
      <Link to="/main/settings" className="w-full">
        <NavButton active={location.pathname === '/main/settings'} icon={<SettingIcon />}>
          Setting
        </NavButton>
      </Link>
    </div>
  );
};
