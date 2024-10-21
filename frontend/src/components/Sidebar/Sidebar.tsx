import {
  HomeIcon,
  SettingIcon,
  MergeListIcon,
  BadgeIcon,
  CustomTemplateIcon,
} from '@components/Sidebar/Icons/index';
import { Button } from '@components/Button/Button';
import { Title } from '@components/Sidebar/Title';
import { Link, useLocation } from 'react-router-dom';

export const Sidebar = () => {
  const location = useLocation();

  return (
    <div className="h-screen w-[250px] border-r border-r-solid border-r-[#dfe1e6] bg-[#f4f5f7] flex flex-col items-center px-4 font-pretendard text-sm leading-[1.2]">
      <Title />
      <Link to="/" className="w-full">
        <Button active={location.pathname === '/'} icon={<HomeIcon />}>
          Home
        </Button>
      </Link>
      <Link to="/merge-request" className="w-full">
        <Button
          active={location.pathname === '/merge-request'}
          icon={<MergeListIcon />}
        >
          Merge Request
        </Button>
      </Link>
      <Link to="/custom-template" className="w-full">
        <Button
          active={location.pathname === '/custom-template'}
          icon={<CustomTemplateIcon />}
        >
          Custom template
        </Button>
      </Link>
      <div className="h-10 w-full flex flex-col justify-center">
        <div className="bg-[#c1c7d0] h-[1px] w-full"></div>
      </div>
      <Link to="/badge" className="w-full">
        <Button active={location.pathname === '/badge'} icon={<BadgeIcon />}>
          My Badge
        </Button>
      </Link>
      <Link to="/settings" className="w-full">
        <Button
          active={location.pathname === '/settings'}
          icon={<SettingIcon />}
        >
          Setting
        </Button>
      </Link>
    </div>
  );
};
