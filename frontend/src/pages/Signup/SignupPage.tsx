import SignupContent from '@components/Signup/SignupContent.tsx';
import signupBgImage from '@assets/images/signup_bg_image.png';

const SignupPage = () => {
  return (
    <div className="w-full h-screen flex justify-around items-center">
      <div className="absolute inset-0 pointer-events-none -z-10">
        <img src={signupBgImage} alt="sign up" className="w-full h-full object-cover" />
      </div>
      <SignupContent />
    </div>
  );
};

export default SignupPage;
