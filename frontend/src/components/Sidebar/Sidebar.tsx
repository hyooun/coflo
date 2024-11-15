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
import { useAtomValue } from 'jotai';
import { projectIdAtom } from '@store/auth';
import { AlarmButton } from '@components/Button/AlarmButton';

export const Sidebar = () => {
  const location = useLocation();
  const projectId = useAtomValue(projectIdAtom);

  return (
    <div className="h-screen min-w-[250px] max-w-[250px] border-r border-r-solid border-r-[#dfe1e6] bg-[#F5F7FA] flex flex-col items-center p-4 font-pretendard text-sm leading-[1.2] ">
      <div className="pb-5">
        <Title />
      </div>
      <AlarmButton count={41} active={location.pathname === `/${projectId}/main/alarm`} />
      <div className="h-10 w-full flex flex-col justify-center">
        <div className="bg-gray-300 h-[1.5px] w-full"></div>
      </div>
      <Link to={`/${projectId}/main`} className="w-full">
        <NavButton active={location.pathname === `/${projectId}/main`} icon={<HomeIcon />}>
          Home
        </NavButton>
      </Link>
      <Link to={`/${projectId}/main/merge-request`} className="w-full">
        <NavButton
          active={location.pathname === `/${projectId}/main/merge-request`}
          icon={<MergeListIcon />}
        >
          Merge Request
        </NavButton>
      </Link>
      <Link to={`/${projectId}/main/custom-prompt`} className="w-full">
        <NavButton
          active={location.pathname === `/${projectId}/main/custom-prompt`}
          icon={<CustomPromptIcon />}
        >
          Custom Prompt
        </NavButton>
      </Link>
      <div className="h-10 w-full flex flex-col justify-center">
        <div className="bg-gray-300 h-[1.5px] w-full"></div>
      </div>
      <Link to={`/${projectId}/main/badge`} className="w-full">
        <NavButton active={location.pathname === `/${projectId}/main/badge`} icon={<BadgeIcon />}>
          My Badge
        </NavButton>
      </Link>
      <Link to={`/${projectId}/main/settings`} className="w-full">
        <NavButton
          active={location.pathname === `/${projectId}/main/settings`}
          icon={<SettingIcon />}
        >
          Setting
        </NavButton>
      </Link>
    </div>
  );
};
