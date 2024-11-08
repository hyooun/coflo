import LoginHeader from '@components/Login/LoginHeader.tsx';
import LoginContent from '@components/Login/LoginContent.tsx';

const LoginPage = () => {
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
