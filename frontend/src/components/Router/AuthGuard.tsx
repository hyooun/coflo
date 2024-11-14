import { useAtom } from 'jotai';
import { useEffect, useState } from 'react';
import { isLoginAtom } from '@store/auth';
import { handleError } from '@config/apiConfig';

interface AuthGuardProps {
  children: React.ReactNode;
}

const AuthGuard = ({ children }: AuthGuardProps) => {
  const [isLogin] = useAtom(isLoginAtom);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setIsLoading(false);
  }, [isLogin]);

  if (isLoading) {
    return null;
  }

  if (!isLogin) {
    const customError = {
      response: {
        status: 401,
        data: { message: 'Login Required' },
      },
    };
    handleError(customError);
    return null;
    8;
  }

  return <>{children}</>;
};

export default AuthGuard;
