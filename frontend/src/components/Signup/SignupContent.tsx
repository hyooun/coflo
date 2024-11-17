import SignupForm from '@components/Signup/SignupForm.tsx';
import signupBoat from '@assets/images/signup_boat.gif';

const SignupContent = () => {
  return (
    <div className="flex flex-row justify-between items-center w-full mx-auto px-20">
      <div className="relative flex flex-row justify-between items-center w-full mx-auto px-10">
        <SignupForm />
      </div>
      <img src={signupBoat} alt="sign_up_boat" className="w-2/5 h-auto object-contain" />
    </div>
  );
};

export default SignupContent;
