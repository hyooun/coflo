import { useState } from 'react';
import { Eye, EyeOff } from 'lucide-react';
import CommonInput from './CommonInput';
import { CommonButton } from '../Button/CommonButton';

interface TokenInputProps {
  value: string;
  onChange: (value: string) => void;
  onValidate?: () => void;
  isValidating?: boolean;
  isValid?: boolean;
  labelText: string;
  placeholder: string;
  warningMessage?: string;
  showValidateButton?: boolean;
}

export const TokenInput = ({
  value,
  onChange,
  onValidate,
  isValidating = false,
  isValid = false,
  labelText,
  placeholder,
  warningMessage = '토큰을 검증해주세요.',
  showValidateButton = true,
}: TokenInputProps) => {
  const [showToken, setShowToken] = useState(true);

  return (
    <div className="relative h-28">
      <CommonInput
        type={showToken ? 'password' : 'text'}
        placeholder={placeholder}
        labelText={labelText}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        isWarning={value !== '' && !isValid}
        warningMessage={warningMessage}
        className="border-2"
        icon={
          <button
            type="button"
            onClick={() => setShowToken(!showToken)}
            className="focus:outline-none"
          >
            {showToken ? <EyeOff size={20} /> : <Eye size={20} />}
          </button>
        }
      />
      {showValidateButton && (
        <CommonButton
          onClick={onValidate}
          disabled={!value || isValidating}
          className="mt-8 w-24 absolute right-0 -top-7"
        >
          {isValidating ? '검증 중...' : '검증하기'}
        </CommonButton>
      )}
    </div>
  );
};
