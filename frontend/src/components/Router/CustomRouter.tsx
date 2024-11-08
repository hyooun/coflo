import App from 'App';
import { createBrowserRouter } from 'react-router-dom';
import HomePage from '@pages/Home/HomePage';
import MergeListPage from '@pages/MergeList/MergeListPage';
import CustomPromptPage from '@pages/CustomPrompt/CustomPromptPage';
import BadgePage from '@pages/Badge/BadgePage';
import SettingsPage from '@pages/Setting/SettingPage';
import MainLayout from '@components/Layout/MainLayout';
import RepositoryLayout from '@components/Layout/RepositoryLayout';
import LoginPage from '@pages/Login/LoginPage';
import SignupPage from '@pages/Signup/SignupPage.tsx';
import MergeRequestReviewPage from '@pages/MergeRequestReview/MergeRequestReviewPage.tsx';
import ReferencesPage from '@pages/MergeRequestReview/ReferencesPage';
import { OAuthRedirectHandler } from '@apis/Auth';

const customRouter = createBrowserRouter([
  {
    path: 'signup',
    element: <SignupPage />,
  },
  {
    path: 'login',
    element: <LoginPage />,
  },
  {
    path: 'login/callback/:provider',
    element: <OAuthRedirectHandler />,
  },
  {
    path: '/',
    element: <App />,
  },
  {
    path: '/repository',
    element: <RepositoryLayout />,
  },
  {
    path: '/:projectId/main',
    element: <MainLayout />,
    children: [
      {
        path: '',
        element: <HomePage />,
      },
      {
        path: 'merge-request',
        element: <MergeListPage />,
      },
      {
        path: 'merge-request/reviews/:id',
        element: <MergeRequestReviewPage />,
      },
      {
        path: 'merge-request/reviews/:id/references/:selectedReviewId',
        element: <ReferencesPage />,
      },
      {
        path: 'custom-prompt',
        element: <CustomPromptPage />,
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
