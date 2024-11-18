import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Link, Info, FileQuestion, CircleCheck, TriangleAlert } from 'lucide-react';
import { CommonButton } from '@components/Button/CommonButton.tsx';
import GuideModal from '@components/Modal/GuideModal.tsx';
import AlertModal from '@components/Modal/AlertModal';
import { TokenInput } from '@components/Input/TokenInput';
import GitUrlSelector from './GitUrlSelector';
import { Gitlab } from '@apis/Gitlab';
import { User } from '@apis/User';
import LogoImage from '@assets/logo.png';

const SignupForm = () => {
  const navigate = useNavigate();
  const [domain, setDomain] = useState('lab.ssafy.com');
  const [userToken, setUserToken] = useState('');
  const [isTokenValid, setIsTokenValid] = useState(false);
  const [isValidating, setIsValidating] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isAlertModalOpen, setIsAlertModalOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState<string[]>([]);
  const isSignupEnabled = isTokenValid && domain;

  const handleValidateToken = async () => {
    if (!userToken) return;

    setIsValidating(true);
    try {
      const response = await Gitlab.validateUserToken({
        domain,
        userToken,
      });
      const isValid = response.data;
      if (isValid) {
        setIsTokenValid(isValid);
        setIsAlertModalOpen(true);
        setAlertMessage(['유효한 토큰입니다.', '회원가입을 진행해주세요.']);
      } else {
        setIsTokenValid(false);
        setIsAlertModalOpen(true);
        setAlertMessage(['유효하지 않은 토큰입니다.', '다시 한 번 입력해주세요.']);
      }
    } catch (error) {
      setIsTokenValid(false);
    } finally {
      setIsValidating(false);
    }
  };

  const handleSignup = async () => {
    if (!isSignupEnabled) return;

    try {
      const response = await User.addGitlabAccount({
        domain,
        userToken,
      });

      if (response.status === 'SUCCESS') {
        navigate('/repository');
      }
    } catch (error) {
      console.error('회원가입 실패', error);
      setIsAlertModalOpen(true);
      setAlertMessage(['회원가입에 실패했습니다.', '다시 시도해주세요.']);
    }
  };

  return (
    <div className="relative min-w-[500px]">
      {/* 블러 효과 배경 */}
      <div className="absolute inset-0 backdrop-blur-md rounded-3xl"></div>

      {/* SignupForm 콘텐츠 */}
      <div className="bg-white/50 rounded-3xl p-10 py-20 relative z-10 font-pretendard">
        <img src={LogoImage} alt="logo" className="w-24 h-auto mx-auto mb-4" />
        <div className="text-4xl font-bold text-secondary text-center mb-20">coFlo</div>
        <form>
          <div className="space-y-14">
            <div className="flex flex-col space-y-10">
              <div className="h-28">
                <GitUrlSelector
                  value={domain}
                  onChange={setDomain}
                  labelText="가져올 Git 서비스 URL을 선택해주세요."
                />
              </div>
              <div className="relative h-28">
                <TokenInput
                  value={userToken}
                  onChange={(value) => setUserToken(value)}
                  onValidate={handleValidateToken}
                  isValidating={isValidating}
                  isValid={isTokenValid}
                  labelText="사용자 인증 토큰을 입력해주세요."
                  placeholder="Enter your user access token"
                  warningMessage="검증하기 버튼을 눌러 토큰을 검증해주세요."
                />
                <div
                  className="flex items-center text-sm text-primary-500 mt-1 cursor-pointer"
                  onClick={() => setIsModalOpen(true)}
                >
                  <Info size={16} className="text-white" fill="primary-500" />
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
            <div className="text-center text-xl font-bold text-primary-500">
              아바타 프로필 클릭 &gt; Edit Profile &gt; Access Tokens &gt; Add new token
            </div>
          }
          onClose={() => setIsModalOpen(false)}
          image={{
            src: '/images/guide/guide_personal_access_token.png',
            alt: 'personal_access_token',
          }}
          links={[
            {
              url: `https://${domain}`,
              text: '토큰 가지러 가기',
              icon: <Link size={20} className="text-primary-500" />,
            },
            {
              url: 'https://docs.gitlab.com/ee/user/profile/personal_access_tokens.html',
              text: '인증 토큰 발급 관련 도움말',
              icon: <FileQuestion size={20} className="text-primary-500" />,
            },
          ]}
        />

        {isAlertModalOpen && (
          <AlertModal
            content={alertMessage}
            onConfirm={() => setIsAlertModalOpen(false)}
            icon={isTokenValid ? CircleCheck : TriangleAlert}
            iconClassName={isTokenValid ? 'text-state-success' : 'text-state-warning'}
          />
        )}

        <div className="flex justify-center mt-20 cursor-pointer">
          <CommonButton
            className="w-full h-14 text-xl text-white rounded-lg"
            disabled={!isSignupEnabled}
            onClick={handleSignup}
          >
            가입하기
          </CommonButton>
        </div>
      </div>
    </div>
  );
};

export default SignupForm;
