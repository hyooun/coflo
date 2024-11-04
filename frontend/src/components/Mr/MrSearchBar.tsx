import { useState, useRef, useEffect } from 'react';
import arrowIcon from '@assets/icons/arrow-icon.svg';

export function CustomSearchBar() {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedOption, setSelectedOption] = useState('All');
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

  return (
    <div className="relative mb-2 flex w-[800px] font-pretendard items-center">
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
        className="bg-gray-200 p-2 pl-3 border border-gray-300 rounded-md w-full h-[30px] focus:outline-none focus:border-gray-500 placeholder:text-black font-pretendard"
        placeholder="Search results..."
      />
    </div>
  );
}
