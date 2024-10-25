import App from 'App';
import { createBrowserRouter } from 'react-router-dom';
import HomePage from '@pages/Home/HomePage';
import MergeListPage from '@pages/MergeList/MergeListPage';
import CustomTemplatePage from '@pages/CustomTemplate/CustomTemplatePage';
import BadgePage from '@pages/Badge/BadgePage';
import SettingsPage from '@pages/Setting/SettingPage';
import LoginPage from '@pages/Login/LoginPage';

const customRouter = createBrowserRouter([
  {
    path: 'login',
    element: <LoginPage />,
  },
  {
    path: '/',
    element: <App />,
    children: [
      {
        path: '/',
        element: <HomePage />,
      },
      {
        path: 'merge-request',
        element: <MergeListPage />,
      },
      {
        path: 'custom-template',
        element: <CustomTemplatePage />,
      },
      {
        path: 'badge',
        element: <BadgePage />,
      },
      {
        path: 'settings',
        element: <SettingsPage />,
      },
    ],
  },
]);

export default customRouter;
