import { useState } from 'react';
import { CheckIcon, ArrowDownToLine, Undo2 } from 'lucide-react';

import CodeReference from './CodeReference';
import TextReference from './TextReference';
import { CommonReferenceProps } from 'types/reference.ts';
import { PencilIcon } from '@components/TextDiv/Icons/PencilIcon';
import { TrashIcon } from '@components/TextDiv/Icons/TrashIcon';
import AlertModal from '@components/Modal/AlertModal';

const CommonReference = ({
  id,
  fileName,
  content,
  type,
  language,
  // onEdit,
  onDelete,
  maxLength = 2000,
}: CommonReferenceProps) => {
  const [isEditing, setIsEditing] = useState(false);
  const [editedContent, setEditedContent] = useState(content);
  const [showSaveMessage, setShowSaveMessage] = useState(false);
  const [isAlertModalOpen, setIsAlertModalOpen] = useState(false);

  const handleEdit = (newContent: string) => {
    setEditedContent(newContent);
  };

  const handleSave = () => {
    if (editedContent.length > maxLength) {
      setIsAlertModalOpen(true);
      return;
    }

    // onEdit(id, editedContent);
    setShowSaveMessage(true);
    setTimeout(() => {
      setShowSaveMessage(false);
      setIsEditing(false);
    }, 1000);
  };

  const handleCancel = () => {
    setEditedContent(content);
    setIsEditing(false);
  };

  const handleAlertModalConfirm = () => {
    setIsAlertModalOpen(false);
  };
  return (
    <div className="rounded-lg border-2 border-primary-500 w-full ">
      <div className="flex flex-wrap justify-between items-center p-2 bg-white rounded-t-lg border-b-2 border-primary-500">
        <h3 className={`font-bold ${type === 'CODE' ? 'font-SFMono text-sm' : ''} overflow-hidden`}>
          {fileName}
        </h3>
        <div className="flex gap-2">
          {isEditing ? (
            <>
              <button
                onClick={handleSave}
                className="flex items-center gap-2 text-primary-500 hover:text-secondary"
                disabled={showSaveMessage}
              >
                {showSaveMessage ? (
                  <>
                    <CheckIcon className="w-4 h-4" strokeWidth={3} />
                    <span className="text-sm animate-fade-out">저장되었습니다</span>
                  </>
                ) : (
                  <ArrowDownToLine className="w-4 h-4" strokeWidth={3} />
                )}
              </button>
              <button
                onClick={handleCancel}
                className="flex items-center gap-1 text-primary-500 hover:text-secondary"
                disabled={showSaveMessage}
              >
                <Undo2 className="w-4 h-4" strokeWidth={3} />
              </button>
            </>
          ) : (
            <>
              <button onClick={() => setIsEditing(true)}>
                <PencilIcon className="w-4 h-4 text-primary-500 hover:text-secondary" />
              </button>
              <button onClick={() => onDelete(id)} className="">
                <TrashIcon className="w-4 h-4 text-primary-500 hover:text-secondary" />
              </button>
            </>
          )}
        </div>
      </div>
      <div className="w-full overflow-hidden">
        {isEditing ? (
          type === 'CODE' ? (
            <CodeReference
              id={id}
              content={editedContent}
              onEdit={handleEdit}
              onCancel={handleCancel}
              language={language}
              maxLength={maxLength}
            />
          ) : (
            <TextReference
              id={id}
              content={editedContent}
              onEdit={handleEdit}
              onCancel={handleCancel}
              maxLength={maxLength}
            />
          )
        ) : (
          <div
            className={`${
              type === 'CODE' ? 'font-SFMono bg-secondary/30 text-sm' : ''
            } p-4 whitespace-break-spaces max-h-[420px] overflow-y-auto w-full`}
          >
            {editedContent}
          </div>
        )}
      </div>

      {isAlertModalOpen && (
        <AlertModal
          content={['글자 수가 제한을 초과했습니다.', `최대 길이는 ${maxLength}자입니다.`]}
          onConfirm={handleAlertModalConfirm}
          className="w-84 h-60"
        />
      )}
    </div>
  );
};

export default CommonReference;
