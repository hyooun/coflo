import { twMerge } from 'tailwind-merge';
import { CommonInputProps } from 'types/input.ts';

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
      {labelText && <label className="block mb-2 text-2xl">{labelText}</label>}
      <div className="relative">
        <input
          type={type}
          placeholder={placeholder}
          className={twMerge(
            'w-full px-4 py-3 border-2 rounded-xl text-primary-500 placeholder-primary-500/50 placeholder:text-lg',
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
      {isWarning && warningMessage && <p className="mt-1 text-state-warning">{warningMessage}</p>}
    </div>
  );
}

export default CommonInput;
