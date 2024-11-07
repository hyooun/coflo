import { useState } from 'react';
import SearchInput from './SearchInput';
import { CommonButton } from '@components/Button/CommonButton';

const SearchComponent = () => {
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [isEditing, setIsEditing] = useState<boolean>(false);

  const defaultSearchTerms = ['기본 검색어 1', '기본 검색어 2', '기본 검색어 3'];

  const handleSave = () => {
    console.log('저장:', searchTerm);
    setIsEditing(false);
  };

  const handleReset = () => {
    const randomIndex = Math.floor(Math.random() * defaultSearchTerms.length);
    setSearchTerm(defaultSearchTerms[randomIndex]);
    setIsEditing(false);
  };

  const handleEdit = () => {
    setIsEditing(true);
  };

  return (
    <div className="bg-white p-4 w-[700px] flex flex-col">
      <SearchInput searchTerm={searchTerm} setSearchTerm={setSearchTerm} isEditing={isEditing} />

      <div className="flex justify-end mt-4">
        <CommonButton
          active={true}
          hoverColor=""
          bgColor="bg-gray-800"
          className="mr-2 w-[100px] h-[30px]"
          onClick={handleReset}
        >
          초기화
        </CommonButton>

        {isEditing ? (
          <CommonButton
            active={true}
            hoverColor=""
            bgColor="bg-primary-500"
            onClick={handleSave}
            className="mr-2 w-[100px] h-[30px]"
          >
            저장
          </CommonButton>
        ) : (
          <CommonButton
            active={true}
            hoverColor=""
            bgColor="bg-gray-800"
            onClick={handleEdit}
            className="mr-2 w-[100px] h-[30px]"
          >
            수정
          </CommonButton>
        )}
      </div>
    </div>
  );
};

export default SearchComponent;
