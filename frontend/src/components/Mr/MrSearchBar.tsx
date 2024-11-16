import { useState, useRef, useEffect } from 'react';
import arrowIcon from '@assets/icons/arrow-icon.svg';
import { SearchIcon } from '@components/SearchBar/Icons/Search';

interface CustomSearchBarProps {
  showOption?: boolean;
  width?: string;
  onSearch: (keyword: string, searchType: string) => void;
}

export const CustomSearchBar = ({
  showOption = true,
  width = 'max-w-[1500px] min-w-[1000px]',
  onSearch,
}: CustomSearchBarProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedOption, setSelectedOption] = useState('All');
  const [searchKeyword, setSearchKeyword] = useState('');
  const [warningMessage, setWarningMessage] = useState('');
  const dropdownRef = useRef<HTMLDivElement>(null);

  const toggleDropdown = () => setIsOpen((prev) => !prev);

  const handleOptionClick = (option: string) => {
    setSelectedOption(option);
    setIsOpen(false);
  };

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    }

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  const handleSearch = () => {
    if (searchKeyword.length < 3 && searchKeyword.length > 0) {
      setWarningMessage('검색어는 3글자 이상 입력해주세요.');
      return;
    }
    setWarningMessage('');
    onSearch(searchKeyword, selectedOption);
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      if (searchKeyword.length < 3 && searchKeyword.length > 0) {
        setWarningMessage('검색어는 3글자 이상 입력해주세요.');
        return;
      }
      setWarningMessage('');
      onSearch(searchKeyword, selectedOption);
    }
  };

  return (
    <div
      className={`relative flex flex-row gap-2 mb-2 font-pretendard items-center justify-between ${width}`}
    >
      {showOption && (
        <div className="relative w-[130px] mr-3 flex items-center" ref={dropdownRef}>
          <div
            className="flex items-center justify-center font-semibold rounded-md w-[100px] h-[30px] cursor-pointer text-center"
            onClick={toggleDropdown}
            style={{
              backgroundImage: `url(${arrowIcon})`,
              backgroundRepeat: 'no-repeat',
              backgroundPosition: 'right center',
              backgroundSize: '15px',
              textAlign: 'center',
              paddingRight: '15px',
            }}
          >
            <span className="flex-grow text-center">{selectedOption}</span>
          </div>
          {isOpen && (
            <ul className="absolute top-full left-0 z-10 w-full bg-white border border-gray-300 rounded-md shadow-md mt-1">
              {['All', 'Title', 'Assignee', 'Reviewer'].map((option) => (
                <li
                  key={option}
                  className="py-2 px-3 cursor-pointer hover:bg-gray-100 text-center"
                  onClick={() => handleOptionClick(option)}
                >
                  {option}
                </li>
              ))}
            </ul>
          )}
        </div>
      )}

      <div className="flex-1 flex flex-col">
        <div className="flex flex-row gap-2">
          <div className="relative flex-1">
            <input
              type="text"
              className={`bg-gray-200 p-2 pl-3 pr-10 rounded-md h-full w-full text-primary-500 focus:outline-none placeholder:text-gray-600 font-pretendard ${
                warningMessage ? '' : ''
              }`}
              placeholder="검색..."
              value={searchKeyword}
              onChange={(e) => {
                setSearchKeyword(e.target.value);
                if (e.target.value.length >= 3) {
                  setWarningMessage('');
                }
              }}
              onKeyDown={handleKeyDown}
            />
            <button
              onClick={handleSearch}
              className="absolute right-3 top-1/2 transform -translate-y-1/2 cursor-pointer"
            >
              <SearchIcon />
            </button>
          </div>
        </div>
        {warningMessage && (
          <span className="text-state-warning text-sm mt-1">{warningMessage}</span>
        )}
      </div>
    </div>
  );
};
