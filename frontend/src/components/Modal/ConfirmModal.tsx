import { ConfirmModalProps } from 'types/modal.ts';

const ConfirmModal = ({ content, onConfirm, onCancel, className = '' }: ConfirmModalProps) => {
  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div className="fixed inset-0 bg-black opacity-50"></div>
      <div
        className={`flex flex-col items-center justify-center bg-white rounded-lg p-6 max-w-sm mx-4 relative z-10 border-2 border-primary-500 ${className}`}
      >
        {content.map((text) => (
          <p className="text-center mb-6 text-xl text-primary-500">{text}</p>
        ))}
        <div className="flex justify-center gap-4">
          <button
            className="px-6 py-2 rounded-full border-2 border-primary-500 text-primary-500 bg-white"
            onClick={() => onCancel()}
          >
            취소
          </button>
          <button
            className="px-6 py-2 rounded-full border-2 border-primary-500 bg-primary-500 text-white"
            onClick={() => onConfirm()}
          >
            확인
          </button>
        </div>
      </div>
    </div>
  );
};

export default ConfirmModal;
