import { useNavigate, useParams } from 'react-router-dom';
import { useEffect } from 'react';
import { useAtom } from 'jotai';
import { isConnectAtom, isLoginAtom, isSignupAtom } from '@store/auth';

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
  const [isSignup] = useAtom(isSignupAtom);
  const [isConnect] = useAtom(isConnectAtom);

  useEffect(() => {
    if (!provider) {
      console.error('Provider not specified');
      navigate('/login');
      return;
    }

    const redirectUrl = getRedirectUrl(provider);

    fetch(`${VITE_API_BASE_URL}/api/oauth2/authorization/${provider}?redirect_url=${redirectUrl}`, {
      mode: 'no-cors',
    });

    setIsLogin(true);

    if (!isSignup) {
      navigate('/signup');
      return;
    }

    navigate(isConnect ? '/main' : '/repository');
  }, [provider, navigate, setIsLogin, isSignup, isConnect]);

  return null;
}
