import { useState } from 'react';
import { useAtom } from 'jotai';
import { Link, Info, FileQuestion, CircleCheck, TriangleAlert } from 'lucide-react';
import { CommonButton } from '@components/Button/CommonButton.tsx';
import GuideModal from '@components/Modal/GuideModal.tsx';
import AlertModal from '@components/Modal/AlertModal';
import GitUrlSelector from './GitUrlSelector';
import { Gitlab } from '@apis/Gitlab';
import { User } from '@apis/User';
import { isSignupAtom } from '@store/auth';
import { useAuthRedirect } from '@hooks/useAuthRedirect';
import { TokenInput } from '@components/Input/TokenInput';

const SignupForm = () => {
  const [domain, setDomain] = useState('lab.ssafy.com');
  const [userToken, setUserToken] = useState('');
  const [isTokenValid, setIsTokenValid] = useState(false);
  const [isValidating, setIsValidating] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isAlertModalOpen, setIsAlertModalOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState<string[]>([]);
  const isSignupEnabled = isTokenValid && domain;
  const [, setIsSignup] = useAtom(isSignupAtom);

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
        setIsSignup(true);
        useAuthRedirect();
      }
    } catch (error) {
      console.error('회원가입 실패', error);
      setIsAlertModalOpen(true);
      setAlertMessage(['회원가입에 실패했습니다.', '다시 시도해주세요.']);
    }
  };

  return (
    <div className="relative min-w-[520px]">
      {/* 블러 효과 배경 */}
      <div className="absolute inset-0 backdrop-blur-md rounded-3xl"></div>

      {/* SignupForm 콘텐츠 */}
      <div className="bg-white/50 rounded-3xl p-10 py-20 relative z-10 font-pretendard border-2 border-primary-500">
        <div className="text-5xl font-bold mb-10 mt-20">회원 가입</div>
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
                  className="flex items-center text-sm text-secondary mt-1 cursor-pointer"
                  onClick={() => setIsModalOpen(true)}
                >
                  <Info size={16} />
                  <span className="ml-1 text-xl">사용자 인증 토큰 안내</span>
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
              아바타 프로필 클릭 &gt; Edit Profile &gt; User settings &gt; Access Tokens &gt; Add
              new token
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
          />
        )}

        <div className="flex justify-center mt-20 cursor-pointer">
          <CommonButton
            className="w-36 h-14 text-xl"
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
