interface ModalProps {
  repo: any;
  inputValue: string;
  setInputValue: (value: string) => void;
  onConfirm: () => void;
  onClose: () => void;
}
