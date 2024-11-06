import { useState, useRef, useEffect } from 'react';
import arrowIcon from '@assets/icons/arrow-icon.svg';

export function ChartDropdown({ onSelect }: { onSelect: (option: string) => void }) {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedOption, setSelectedOption] = useState<string | null>(null);
  const dropdownRef = useRef<HTMLDivElement>(null);

  const toggleDropdown = () => setIsOpen((prev) => !prev);

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

  const options = ['획득 통합 스코어', '획득 개별 스코어', '누적 통합 스코어', '누적 개별 스코어'];

  const handleOptionClick = (option: string) => {
    setSelectedOption(option);
    onSelect(option);
    setIsOpen(false);
  };

  return (
    <div className="relative w-[150px] h-[30px] mt-3 mr-4 z-10" ref={dropdownRef}>
      <div
        className="flex items-center bg-transparent justify-between bg-gray-200 p-1 rounded-md cursor-pointer "
        onClick={toggleDropdown}
      >
        <span className="text-sm mr-0">{selectedOption || '차트 선택'}</span>
        <img src={arrowIcon} alt="Arrow Icon" className="w-4 h-4 " />
      </div>

      {isOpen && (
        <ul className="absolute top-full left-0 w-full border  bg-white border-gray-500 rounded-md shadow-md mt-1 ">
          {options.map((option) => (
            <li
              key={option}
              className="py-1 px-3 cursor-pointer hover:bg-gray-500 text-center"
              onClick={() => handleOptionClick(option)}
            >
              {option}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
