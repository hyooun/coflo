import { useNavigate, useParams } from 'react-router-dom';
import { useEffect } from 'react';
import { useAtom } from 'jotai';
import { isConnectAtom, isLoginAtom, isSignupAtom, projectIdAtom } from '@store/auth';

const VITE_REDIRECT_URL = import.meta.env.VITE_REDIRECT_URL;
const VITE_API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

const getRedirectUrl = (provider: string) =>
  encodeURIComponent(`${VITE_REDIRECT_URL}/login/callback/${provider}`);

export function login(provider: 'kakao' | 'google') {
  const redirectUrl = getRedirectUrl(provider);
  window.location.replace(
    `${VITE_API_BASE_URL}/api/oauth2/authorization/${provider}?redirect_url=${redirectUrl}`,
  );
}

export function OAuthRedirectHandler() {
  const navigate = useNavigate();
  const { provider } = useParams<{ provider: string }>();
  const [, setIsLogin] = useAtom(isLoginAtom);
  const [, setIsSignup] = useAtom(isSignupAtom);
  const [, setIsConnect] = useAtom(isConnectAtom);
  const [, setProjectId] = useAtom(projectIdAtom);

  useEffect(() => {
    if (!provider) {
      console.error('Provider not specified');
      navigate('/login');
      return;
    }

    // const redirectUrl = getRedirectUrl(provider);

    // fetch(`${VITE_API_BASE_URL}/api/oauth2/authorization/${provider}?redirect_url=${redirectUrl}`, {
    //   mode: 'no-cors',
    // });

    const searchParams = new URLSearchParams(window.location.search);
    // console.log('파싱된 쿼리 파라미터:', {
    //   isSignup: searchParams.get('isSignup'),
    //   isConnect: searchParams.get('isConnect'),
    //   projectId: searchParams.get('projectId'),
    //   raw: Object.fromEntries(searchParams.entries()),
    // });

    const isSignup = searchParams.get('isSignup') === 'true';
    const isConnect = searchParams.get('isConnect') === 'true';
    const projectId = searchParams.get('projectId');

    if (!searchParams.has('isSignup')) {
      return;
    }

    setIsLogin(true);
    setIsSignup(isSignup);
    setIsConnect(isConnect);
    setProjectId(projectId);

    if (!isSignup) {
      navigate('/signup', { replace: true });
      return;
    }

    if (isConnect && projectId) {
      navigate(`/${projectId}/main`, { replace: true });
      return;
    }

    navigate('/repository', { replace: true });
  }, [provider, navigate, setIsLogin, setIsSignup, setIsConnect, setProjectId]);

  return null;
}
