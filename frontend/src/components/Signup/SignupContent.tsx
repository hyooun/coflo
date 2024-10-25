import SignupForm from '@components/Signup/SignupForm.tsx';

const SignupContent = () => {
  return (
    <div className="w-full h-full flex items-center justify-center">
      <div className="absolute inset-0 pointer-events-none">
        <img
          src="/images/signup_bg_image.webp"
          alt="sign up"
          className="w-full h-full object-cover"
        />
      </div>
      <SignupForm />
    </div>
  );
};

export default SignupContent;
