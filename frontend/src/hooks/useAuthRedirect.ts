import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAtom } from 'jotai';
import { isLoginAtom, isSignupAtom, isConnectAtom, projectIdAtom } from '@store/auth';

export const useAuthRedirect = () => {
  const navigate = useNavigate();
  const [isLogin] = useAtom(isLoginAtom);
  const [isSignup] = useAtom(isSignupAtom);
  const [isConnect] = useAtom(isConnectAtom);
  const [projectId] = useAtom(projectIdAtom);

  const [isInitialized, setIsInitialized] = useState(false);

  useEffect(() => {
    if (
      typeof isLogin !== 'undefined' &&
      typeof isSignup !== 'undefined' &&
      typeof isConnect !== 'undefined' &&
      typeof projectId !== 'undefined'
    ) {
      setIsInitialized(true);
    }
  }, [isLogin, isSignup, isConnect, projectId]);

  useEffect(() => {
    if (!isInitialized) return;

    console.log('리다이렉트 상태:', {
      isLogin,
      isSignup,
      isConnect,
      projectId,
    });

    if (!isLogin) {
      navigate('/login');
      return;
    }

    if (!isSignup) {
      navigate('/signup');
      return;
    }

    if (isConnect && projectId) {
      navigate(`/${projectId}/main`);
    } else {
      navigate('/repository');
    }
  }, [isInitialized, isLogin, isSignup, isConnect, projectId, navigate]);

  return {
    isLogin,
    isSignup,
    isConnect,
    projectId,
  };
};
