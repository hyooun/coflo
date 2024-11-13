import { createBrowserRouter } from 'react-router-dom';
import AuthGuard from './AuthGuard';
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
import App from 'App';
import PageNotFound from '@pages/Error/PageNotFound';
// import ErrorBoundary from './ErrorBoundary';
import { ServerErrorPage } from '@pages/Error/ServerErrorPage';
import { BadRequestPage } from '@pages/Error/BadRequestPage';
import { UnauthorizedPage } from '@pages/Error/UnauthorizedPage';

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
    element: (
      // <ErrorBoundary>
      <AuthGuard>
        <RepositoryLayout />
      </AuthGuard>
      // </ErrorBoundary>
    ),
  },
  {
    path: '/:projectId/main',
    element: (
      // <ErrorBoundary>
      <AuthGuard>
        <MainLayout />
      </AuthGuard>
      // </ErrorBoundary>
    ),
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
  {
    path: '/error',
    children: [
      {
        path: 'not-found',
        element: <PageNotFound />,
      },
      {
        path: 'server-error',
        element: <ServerErrorPage />,
      },
      {
        path: 'bad-request',
        element: <BadRequestPage />,
      },
      {
        path: 'unauthorized',
        element: <UnauthorizedPage />,
      },
    ],
  },
  {
    path: '*',
    element: <PageNotFound />,
  },
]);

export default customRouter;
