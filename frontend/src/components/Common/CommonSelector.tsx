import React, { useState, useRef, useEffect } from 'react';
import { ChevronDown, Search } from 'lucide-react';

interface CommonSelectorProps<T> {
  // 기본 스타일링 및 UI 관련
  className?: string;
  buttonClassName?: string;
  dropdownClassName?: string;
  width?: string;
  maxHeight?: string;

  // 선택된 아이템 관련
  selectedItem: T;
  displayValue: (item: T) => string;
  onSelect: (item: T) => void;
  isEqual?: (item1: T, item2: T) => boolean;

  // 검색 관련
  items: T[];
  searchPlaceholder?: string;
  searchInputClassName?: string;
  showSearch?: boolean;
  filterFunction?: (item: T, query: string) => boolean;

  // 아이템 렌더링
  renderItem?: (item: T, isSelected: boolean) => React.ReactNode;
  itemClassName?: string;
}

const CommonSelector = <T extends unknown>({
  className = 'relative py-1.5',
  buttonClassName = 'flex items-center gap-2 px-3 text-sm text-gray-500 hover:bg-secondary rounded-md transition-colors',
  dropdownClassName = 'absolute z-20 mt-1 bg-white rounded-lg shadow-lg border border-gray-200 py-1',
  width = 'w-48',
  maxHeight = 'max-h-60',
  selectedItem,
  displayValue,
  onSelect,
  isEqual = (item1, item2) => item1 === item2,
  items,
  searchPlaceholder = '검색...',
  searchInputClassName = 'w-full pl-8 pr-3 py-1.5 text-sm bg-gray-500 border border-gray-200 rounded-full focus:outline-none placeholder:text-primary-400',
  showSearch = true,
  filterFunction = (item: T, query: string) =>
    displayValue(item).toLowerCase().includes(query.toLowerCase()),
  renderItem,
  itemClassName = 'w-full text-left px-4 py-2 text-sm hover:bg-gray-100 transition-colors',
}: CommonSelectorProps<T>) => {
  const [isOpen, setIsOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const dropdownRef = useRef<HTMLDivElement>(null);
  const searchInputRef = useRef<HTMLInputElement>(null);

  const filteredItems = items.filter((item) => filterFunction(item, searchQuery));

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  useEffect(() => {
    if (isOpen && searchInputRef.current) {
      searchInputRef.current.focus();
    }
  }, [isOpen]);

  return (
    <div className={className} ref={dropdownRef}>
      <button onClick={() => setIsOpen(!isOpen)} className={buttonClassName}>
        <span>{displayValue(selectedItem)}</span>
        <ChevronDown className={`w-4 h-4 transition-transform ${isOpen ? 'rotate-180' : ''}`} />
      </button>

      {isOpen && (
        <div className={`${dropdownClassName} ${width} ${maxHeight}`}>
          {showSearch && (
            <div className="p-2">
              <div className="relative">
                <input
                  ref={searchInputRef}
                  type="text"
                  className={searchInputClassName}
                  placeholder={searchPlaceholder}
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                />
                <Search className="absolute left-2 top-1/2 -translate-y-1/2 w-4 h-4 text-primary-500" />
              </div>
            </div>
          )}
          <div className="max-h-40 overflow-y-auto">
            {filteredItems.map((item) =>
              renderItem ? (
                renderItem(item, isEqual(item, selectedItem))
              ) : (
                <button
                  key={displayValue(item)}
                  onClick={() => {
                    onSelect(item);
                    setIsOpen(false);
                  }}
                  className={`${itemClassName} ${
                    isEqual(item, selectedItem)
                      ? 'text-primary-500 bg-secondary/30'
                      : 'text-gray-700'
                  }`}
                >
                  {displayValue(item)}
                </button>
              ),
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default CommonSelector;
