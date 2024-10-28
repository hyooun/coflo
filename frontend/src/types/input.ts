import { InputHTMLAttributes, ReactNode } from 'react';

export interface CommonInputProps extends InputHTMLAttributes<HTMLInputElement> {
  placeholder?: string;
  className?: string;
  type?: string;
  icon?: ReactNode;
  warningMessage?: string;
  isWarning?: boolean;
  labelText?: string;
}
