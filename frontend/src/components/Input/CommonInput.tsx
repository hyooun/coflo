// components/Input/CommonInput.tsx
import { InputHTMLAttributes, ReactNode } from 'react';
import { twMerge } from 'tailwind-merge';

interface CommonInputProps extends InputHTMLAttributes<HTMLInputElement> {
  placeholder?: string;
  className?: string;
  type?: string;
  icon?: ReactNode;
  warningMessage?: string;
  isWarning?: boolean;
  labelText?: string;
}

function CommonInput({
  placeholder,
  className,
  type = 'text',
  icon,
  warningMessage,
  isWarning = false,
  labelText,
  ...props
}: CommonInputProps) {
  return (
    <div className="w-full">
      {labelText && <label className="block mb-2">{labelText}</label>}
      <div className="relative">
        <input
          type={type}
          placeholder={placeholder}
          className={twMerge(
            'w-full px-4 py-3 border-2 rounded-xl text-primary-500 placeholder-primary-500/50',
            'border-primary-500 bg-transparent',
            'focus:outline-none',
            icon && 'pr-12', // 아이콘이 있을 경우 오른쪽 패딩 추가
            isWarning && 'border-state-warning',
            className,
          )}
          {...props}
        />
        {icon && (
          <div className="absolute right-4 top-1/2 -translate-y-1/2 text-primary-500 flex items-center">
            {icon}
          </div>
        )}
      </div>
      {isWarning && warningMessage && (
        <p className="mt-1 text-sm text-state-warning">{warningMessage}</p>
      )}
    </div>
  );
}

export default CommonInput;
