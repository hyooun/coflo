import { useState, useRef, useEffect } from 'react';
import arrowIcon from '@assets/icons/arrow-icon.svg';
import { CommonButton } from '@components/Button/CommonButton';

interface CustomSearchBarProps {
  onSearch: (keyword: string, searchType: string) => void;
}

export function CustomSearchBar({ onSearch }: CustomSearchBarProps) {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedOption, setSelectedOption] = useState('All');
  const [searchKeyword, setSearchKeyword] = useState('');
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
    onSearch(searchKeyword, selectedOption);
  };

  return (
    <div className="relative flex flex-row gap-2 mb-2 font-pretendard items-center justify-between max-w-[1500px] min-w-[1000px]">
      <div className="relative w-[130px] mr-3" ref={dropdownRef}>
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

      <input
        type="text"
        className="bg-gray-200 p-2 pl-3 rounded-md w-full h-full focus:outline-none placeholder:text-black font-pretendard"
        placeholder="Search results..."
        value={searchKeyword}
        onChange={(e) => setSearchKeyword(e.target.value)}
      />

      <CommonButton className="w-16 h-full px-2 rounded-md" onClick={handleSearch}>
        검색
      </CommonButton>
    </div>
  );
}
