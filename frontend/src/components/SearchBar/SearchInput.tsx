import { useEffect, useRef, useState } from 'react';

const SearchInput = ({ searchTerm, setSearchTerm }: SearchInputProps) => {
  const [height, setHeight] = useState<number>(40);
  const textareaRef = useRef<HTMLTextAreaElement>(null);
  const maxHeight = 1000;
  const maxLength = 1000;

  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setSearchTerm(e.target.value);
  };

  useEffect(() => {
    if (textareaRef.current) {
      const newHeight = textareaRef.current.scrollHeight;
      setHeight(Math.min(newHeight, maxHeight));
    }
  }, [searchTerm]);

  useEffect(() => {
    if (searchTerm === '') {
      setHeight(40);
    }
  }, [searchTerm]);

  return (
    <div className="flex flex-col">
      <div className="flex items-center relative">
        <textarea
          ref={textareaRef}
          value={searchTerm}
          onChange={handleChange}
          maxLength={maxLength}
          className="flex-grow border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400 placeholder-gray-500 bg-[#f4f5f7] resize-none overflow-y-auto pr-15"
          placeholder="커스텀 템플릿을 입력하세요"
          style={{
            height: `${height}px`,
            minHeight: '40px',
            maxHeight: `${maxHeight}px`,
            padding: '10px 30px 10px 10px',
            lineHeight: '20px',
            textAlign: 'left',
            overflowY: height >= maxHeight ? 'scroll' : 'hidden',
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
