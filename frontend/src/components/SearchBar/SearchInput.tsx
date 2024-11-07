import { useState } from 'react';

interface SearchInputProps {
  searchTerm: string;
  setSearchTerm: (value: string) => void;
  isEditing: boolean;
}

const SearchInput = ({ searchTerm, setSearchTerm, isEditing }: SearchInputProps) => {
  const [height] = useState<number>(500);

  const maxLength = 1000;

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setSearchTerm(e.target.value);
  };

  return (
    <div className="flex flex-col">
      <div className="flex items-center relative">
        <textarea
          value={searchTerm}
          onChange={handleChange}
          maxLength={maxLength}
          disabled={!isEditing}
          className="flex-grow border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400 placeholder-gray-500 bg-[#f4f5f7]  overflow-y-hidden resize-none p-15"
          placeholder="커스텀 템플릿을 입력하세요"
          style={{
            height: `${height}px`,
            padding: '20px 20px 20px 20px',
            lineHeight: '20px',
            textAlign: 'left',
          }}
        />
      </div>

      <div className="mt-2 text-right text-gray-600">
        {searchTerm.length}/{maxLength} 글자
      </div>
    </div>
  );
};

export default SearchInput;
