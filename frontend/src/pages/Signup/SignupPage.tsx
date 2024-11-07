import SignupContent from '@components/Signup/SignupContent.tsx';
import { useAuthRedirect } from '@hooks/useAuthRedirect';

const SignupPage = () => {
  useAuthRedirect();

  return (
    <div className="w-full h-screen flex justify-start items-center">
      <div className="w-full max-w-2xl p-5">
        <SignupContent />
      </div>
    </div>
  );
};

export default SignupPage;
