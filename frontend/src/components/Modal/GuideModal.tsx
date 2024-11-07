// components/Modal/GuideModal.tsx
import { Gitlab } from '@apis/Gitlab';
import { TokenInput } from '@components/Input/TokenInput';
import { X } from 'lucide-react';
import { useState } from 'react';
import type { GuideModalProps } from 'types/modal.ts';

export default function GuideModal({
  isOpen,
  width = 'w-[600px]',
  title,
  content,
  image,
  onClose,
  onConfirm,
  hasInput,
  inputProps,
  links,
}: GuideModalProps) {
  if (!isOpen) return null;
  // 프로젝트 아이디 추가 구현
  // const [botToken, setBotToken] = useState<string>('');
  // const [isTokenValid, setIsTokenValid] = useState(false);
  // const [isValidating, setIsValidating] = useState(false);
  //
  // const handleValidateToken = async () => {
  //   if (!botToken) return;
  //   console.log('토큰 검증 시작');
  //   console.log(botToken);
  //   setIsValidating(true);
  //   try {
  //     const response = await Gitlab.validateBotToken({
  //       botToken,
  //     });
  //     const isValid = response.data;
  //     if (isValid) {
  //       setIsTokenValid(isValid);
  //       console.log('유효한 토큰입니다.');
  //       // setIsAlertModalOpen(true);
  //       // setAlertMessage(['유효한 토큰입니다.', '회원가입을 진행해주세요.']);
  //     } else {
  //       console.log('유효하지 않은 토큰입니다.');
  //       // setIsAlertModalOpen(true);
  //       // setAlertMessage(['유효하지 않은 토큰입니다.', '다시 한 번 입력해주세요.']);
  //     }
  //   } catch (error) {
  //     setIsTokenValid(false);
  //   } finally {
  //     setIsValidating(false);
  //   }
  // };

  return (
    <div
      className="fixed inset-0 bg-black/50 flex items-center justify-center z-50"
      onClick={onClose}
    >
      <div
        className={`bg-white p-8 border-2 border-primary-500 rounded-xl ${width} max-h-[90vh] relative`}
        onClick={(e) => e.stopPropagation()} // 모달 내부 클릭 시 닫히지 않도록
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
        <h2 className="text-2xl font-bold text-center mt-4 mb-6 text-primary-500">{title}</h2>

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
