import { useState } from 'react';
import { Eye, EyeOff, Info } from 'lucide-react';
import CommonInput from '@components/Input/CommonInput.tsx';
import { CommonButton } from '@components/Button/CommonButton.tsx';
import GuideModal from '@components/Modal/GuideModal.tsx';

const SignupForm = () => {
  const [showPassword, setShowPassword] = useState(true);
  const [projectUrl, setProjectUrl] = useState('');
  const [userToken, setUserToken] = useState('');
  const [isUrlValid, setIsUrlValid] = useState(true);
  const [isTokenValid, setIsTokenValid] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const validateUrl = (url: string) => {
    return url.startsWith('lab.ssafy');
  };

  const validateUserToken = (token: string) => {
    const englishOnlyRegex = /^[A-Za-z]+$/;
    return englishOnlyRegex.test(token);
  };

  const handleUrlChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setProjectUrl(value);
    setIsUrlValid(validateUrl(value));
  };

  const handleTokenChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setUserToken(value);
    setIsTokenValid(validateUserToken(value));
  };

  return (
    <div className="bg-white/70 rounded-3xl p-10 relative z-10 font-pretendard">
      <div className="text-3xl font-bold mb-10 mt-20">회원 가입</div>
      <form>
        <div className="space-y-8">
          <div className="flex flex-col space-y-4">
            <div className="h-28">
              <CommonInput
                placeholder="Enter your user project URL"
                labelText="가져올 프로젝트의 URL을 입력해주세요."
                value={projectUrl}
                onChange={handleUrlChange}
                isWarning={!isUrlValid}
                warningMessage="유효하지 않은 URL 형식입니다. (ex: lab.ssafy. ...)"
              />
            </div>
            <div className="h-28">
              <CommonInput
                type={showPassword ? 'password' : 'text'}
                placeholder="Enter your user access token"
                labelText="사용자 인증 토큰을 입력해주세요."
                value={userToken}
                onChange={handleTokenChange}
                isWarning={!isTokenValid}
                warningMessage="유효하지 않은 토큰입니다. 다시 한 번 입력해주세요."
                icon={
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="focus:outline-none"
                  >
                    {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                  </button>
                }
              />
              <div
                className="flex items-center text-sm text-secondary mt-1 cursor-pointer"
                onClick={() => setIsModalOpen(true)}
              >
                <Info size={16} />
                <span className="ml-1">사용자 인증 토큰 안내</span>
              </div>
            </div>
          </div>
        </div>
      </form>

      <GuideModal
        isOpen={isModalOpen}
        width="w-[400px]"
        title="사용자 인증 토큰 안내"
        content={
          <div className="text-center font-bold text-primary-500">
            아바타 프로필 클릭 &gt; Edit Profile &gt; User settings &gt; Access Tokens
          </div>
        }
        onClose={() => setIsModalOpen(false)}
        image={{
          src: '/images/guide/guide_personal_access_token.png',
          alt: 'personal_access_token',
        }}
        link={{
          url: 'https://docs.gitlab.com/ee/user/profile/personal_access_tokens.html',
          text: '인증 토큰 발급 관련 안내',
        }}
      />

      <div className="flex justify-center mt-10">
        <CommonButton className="w-32 h-10">회원가입</CommonButton>
      </div>
    </div>
  );
};

export default SignupForm;
