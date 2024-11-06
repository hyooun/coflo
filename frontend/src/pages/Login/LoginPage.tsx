import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAtom } from 'jotai';
import { isLoginAtom, isSignupAtom, isConnectAtom } from '@store/auth';
import LoginHeader from '@components/Login/LoginHeader.tsx';
import LoginContent from '@components/Login/LoginContent.tsx';

const LoginPage = () => {
  const navigate = useNavigate();
  const [isLogin] = useAtom(isLoginAtom);
  const [isSignup] = useAtom(isSignupAtom);
  const [isConnect] = useAtom(isConnectAtom);

  useEffect(() => {
    console.log('로그인 페이지', isLogin, isSignup, isConnect);
    if (!isLogin) return;
    if (!isSignup) {
      navigate('/signup');
      return;
    }

    navigate(isConnect ? '/main' : '/repository');
  }, [isLogin, isSignup, isConnect, navigate]);

  return (
    <div className="flex flex-row h-full w-full justify-center items-center min-h-screen">
      <div className="flex flex-col p-8">
        <LoginHeader />
        <div className="mt-10">
          <LoginContent />
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
