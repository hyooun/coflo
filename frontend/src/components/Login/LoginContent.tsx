import LoginButton from '@pages/Login/LoginButton.tsx';
import loginImage from '@assets/images/login_image.png';
import { login } from '@apis/Auth.ts';

const LoginContent = () => {
  const handleKakaoLogin = () => {
    login('kakao');
  };

  const handleGoogleLogin = () => {
    login('google');
  };
  return (
    <div className="flex mobile:flex-col flex-row gap-20 h-full items-center justify-center ">
      <div className="w-full lg:w-1/2">
        <img
          src={loginImage}
          alt="login illustration"
          className="w-full max-w-[500px] h-auto object-contain mx-auto"
        />
      </div>

      <div className="w-full lg:w-1/2 flex flex-col justify-center max-w-md mx-auto font-pretendard">
        <div className="flex flex-col items-start mb-12">
          <div className="flex flex-wrap text-2xl md:text-3xl font-bold">
            <span>오늘도</span>
            <span className="text-secondary mx-1">coFlo</span>
            <span>할까요?</span>
          </div>
          <p className="mt-2">팀원과 함께 내 프로젝트 코드를 리뷰하세요.</p>
        </div>
        <div className="w-full space-y-3 mb-6">
          <LoginButton
            imageName="kakao_logo.png"
            buttonText="카카오로 시작하기"
            bgColor="bg-[#FEE500] hover:bg-[#FDD800]"
            onClick={handleKakaoLogin}
          />
          <LoginButton
            imageName="google_logo.png"
            buttonText="구글로 시작하기"
            bgColor="bg-[#EEEEEE] hover:bg-[#E0E0E0]"
            onClick={handleGoogleLogin}
          />
        </div>
        <div className="flex flex-wrap text-gray-600 font-bold justify-center">
          <span>소셜 계정을 통해 바로 이용이 가능하며 </span>
          <span className="flex flex-wrap">
            첫 로그인시
            <div className="text-secondary">&nbsp;이용약관&nbsp;</div> 및
            <div className="text-secondary">&nbsp;개인정보처리방침&nbsp;</div>
            동의로 간주됩니다.
          </span>
        </div>
      </div>
    </div>
  );
};

export default LoginContent;
