interface ModalProps {
  repo: any;
  inputValue: string;
  setInputValue: (value: string) => void;
  onConfirm: () => void;
  onClose: () => void;
}

export interface GuideModalProps {
  repo?: any;
  inputValue?: string;
  setInputValue?: (value: string) => void;
  isOpen: boolean;
  width?: string;
  title: string;
  content?: React.ReactNode;
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
    placeholder?: string;
  };
  link?: {
    url: string;
    text: string;
  };
}
