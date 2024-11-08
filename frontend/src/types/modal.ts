export interface ModalProps {
  repo: any;
  inputValue: string;
  setInputValue: (value: string) => void;
  onConfirm: () => void;
  onClose: () => void;
}

export interface GuideModalProps {
  gitlabProjectId?: string;
  repo?: any;
  inputValue?: string;
  setInputValue?: (value: string) => void;
  isOpen: boolean;
  width?: string;
  title: string;
  content?: React.ReactNode;
  contentBottom?: React.ReactNode;
  image?: {
    src: string;
    alt: string;
  };
  onClose: () => void;
  onConfirm?: () => void;
  hasInput?: boolean;
  inputProps?: {
    value: string;
    onChange: (value: string) => void;
    onValidate?: () => void;
    isValidating?: boolean;
    isValid?: boolean;
    placeholder?: string;
    labelText?: string;
    warningMessage?: string;
  };
  links?: {
    url: string;
    text: string;
    icon?: React.ReactNode;
  }[];
  children?: React.ReactNode;
}

export interface AlertModalProps {
  content: string[];
  onConfirm: () => void;
  className?: string;
  icon?: React.ElementType;
  iconSize?: number;
}

export interface ConfirmModalProps extends AlertModalProps {
  onCancel: () => void;
}
