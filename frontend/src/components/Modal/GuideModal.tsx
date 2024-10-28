// components/Modal/GuideModal.tsx
import { X } from 'lucide-react';
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
  link,
}: GuideModalProps) {
  if (!isOpen) return null;

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
            <input
              type="text"
              value={inputProps.value}
              onChange={(e) => inputProps.onChange(e.target.value)}
              placeholder={inputProps.placeholder}
              className="w-full p-3 border-2 border-primary-500 rounded-lg
                       focus:outline-none"
            />
          </div>
        )}

        {/* Link */}
        {link && (
          <div className="mb-6 text-center">
            <a
              href={link.url}
              target="_blank"
              rel="noopener noreferrer"
              className="text-primary-500"
            >
              {link.text}
            </a>
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
