import SignupContent from '@components/Signup/SignupContent.tsx';

const SignupPage = () => {
  return (
    <div className="w-full h-screen flex justify-start items-center">
      <div className="w-full max-w-md p-5">
        <SignupContent />
      </div>
    </div>
  );
};

export default SignupPage;
