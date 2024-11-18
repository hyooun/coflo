// components/Modal/GuideModal.tsx
// import { useState } from 'react';
import { X } from 'lucide-react';
// import { Gitlab } from '@apis/Gitlab';
import { useState, useEffect } from 'react';
import { TokenInput } from '@components/Input/TokenInput';
import type { GuideModalProps } from 'types/modal.ts';
import { Gitlab } from '@apis/Gitlab';
import AlertModal from '@components/Modal/AlertModal';
import { CircleCheck, TriangleAlert } from 'lucide-react';

export default function GuideModal({
  gitlabProjectId,
  isOpen,
  width = 'w-[600px]',
  title,
  content,
  contentBottom,
  image,
  onClose,
  onConfirm,
  hasInput,
  inputProps,
  links,
}: GuideModalProps) {
  const [botToken, setBotToken] = useState<string>('');
  const [isTokenValid, setIsTokenValid] = useState(false);
  const [isValidating, setIsValidating] = useState(false);
  const [isAlertModalOpen, setIsAlertModalOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState<string[]>([]);

  useEffect(() => {
    if (isOpen && inputProps?.value) {
      setBotToken(inputProps.value);
    }
  }, [inputProps?.value, isOpen]);

  const handleValidateToken = async () => {
    console.log(botToken);
    if (!botToken) return;
    console.log('토큰 검증 시작');

    setIsValidating(true);

    if (!gitlabProjectId) {
      console.error('Gitlab Project ID가 정의되지 않았습니다.');
      setIsAlertModalOpen(true);
      setAlertMessage(['Gitlab Project ID가 정의되지 않았습니다.', 'GitLab ID를 확인해주세요.']);
      return;
    }

    try {
      const response = await Gitlab.validateBotToken({
        gitlabProjectId,
        botToken,
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

  if (!isOpen) return null;

  return (
    <div
      className="fixed inset-0 bg-black/50 flex items-center justify-center z-50"
      onClick={onClose}
    >
      <div
        className={`bg-white p-8 border-2 border-primary-500 rounded-xl ${width} max-h-[90vh] relative`}
        onClick={(e) => e.stopPropagation()}
      >
        {/* Close Button */}
        <button
          onClick={onClose}
          className="absolute top-4 right-4 p-1 hover:bg-primary-100 rounded-full transition-colors"
          aria-label="Close modal"
        >
          <X size={24} className="text-primary-500" />
        </button>

        {/* Title */}
        <h2 className="text-xl font-bold text-center mt-4 mb-6 text-primary-500">{title}</h2>

        {/* Content */}
        {content && <div className="mb-6 space-y-2">{content}</div>}

        {/* Image */}
        {image && (
          <div className="mb-6">
            <img src={image.src} alt={image.alt} className="w-full h-auto rounded-lg" />
          </div>
        )}

        {/* Input Field */}
        {hasInput && inputProps && (
          <div className="mb-6">
            <TokenInput
              value={inputProps.value}
              onChange={inputProps.onChange}
              onValidate={handleValidateToken}
              isValidating={isValidating}
              isValid={isTokenValid}
              labelText={inputProps.labelText || ''}
              placeholder={inputProps.placeholder || ''}
              warningMessage="검증하기 버튼을 눌러 토큰을 검증해주세요."
              showValidateButton={true}
            />
          </div>
        )}

        {isAlertModalOpen && (
          <AlertModal
            content={alertMessage}
            onConfirm={() => setIsAlertModalOpen(false)}
            icon={isTokenValid ? CircleCheck : TriangleAlert}
            iconClassName={isTokenValid ? 'text-state-success' : 'text-state-warning'}
          />
        )}

        {/* Link */}
        {links && (
          <div className="flex flex-col mb-6 items-center">
            {links.map((link) => (
              <div className="flex items-center justify-start gap-2">
                {link.icon && link.icon}
                <a
                  href={link.url}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="text-primary-500"
                >
                  {link.text}
                </a>
              </div>
            ))}
          </div>
        )}

        {contentBottom && <div className="mb-6 space-y-2">{contentBottom}</div>}

        {/* Confirm Button */}
        {onConfirm && (
          <div className="flex justify-end">
            <button
              type="button"
              onClick={onConfirm}
              className="bg-primary-500 text-white px-6 py-2 rounded-lg
                       hover:bg-primary-600 transition-colors"
            >
              확인
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
