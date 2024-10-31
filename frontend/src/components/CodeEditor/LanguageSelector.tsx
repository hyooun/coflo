import { useState, useRef, useEffect } from 'react';
import { LANGUAGE_VERSIONS } from '@constants/codeEditor';
import { ChevronDown, Search } from 'lucide-react';

interface LanguageSelectorProps {
  language: string;
  onSelect: (lang: string) => void;
}

const LanguageSelector = ({ language, onSelect }: LanguageSelectorProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const languages = Object.keys(LANGUAGE_VERSIONS);
  const dropdownRef = useRef<HTMLDivElement>(null);
  const searchInputRef = useRef<HTMLInputElement>(null);

  const filteredLanguages = languages.filter((lang) =>
    lang.toLowerCase().includes(searchQuery.toLowerCase()),
  );

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
    <div className="relative py-1.5 " ref={dropdownRef}>
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="flex items-center gap-2 px-3 text-sm text-gray-500 hover:bg-secondary rounded-md transition-colors"
      >
        <span>{language}</span>
        <ChevronDown className={`w-4 h-4 transition-transform ${isOpen ? 'rotate-180' : ''}`} />
      </button>

      {isOpen && (
        <div className="absolute z-20 mt-1 w-48 bg-white rounded-lg shadow-lg border border-gray-200 py-1 max-h-60">
          <div className="p-2">
            <div className="relative">
              <input
                ref={searchInputRef}
                type="text"
                className="w-full pl-8 pr-3 py-1.5 text-sm bg-gray-500 border border-gray-200 rounded-full focus:outline-none placeholder:text-primary-400"
                placeholder="언어 검색..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
              <Search className="absolute left-2 top-1/2 -translate-y-1/2 w-4 h-4 text-primary-500" />
            </div>
          </div>
          <div className="max-h-40 overflow-y-auto">
            {filteredLanguages.map((lang) => (
              <button
                key={lang}
                onClick={() => {
                  onSelect(lang);
                  setIsOpen(false);
                }}
                className={`w-full text-left px-4 py-2 text-sm hover:bg-gray-100 transition-colors
                ${language === lang ? 'text-primary-500 bg-secondary/30' : 'text-gray-700'}
              `}
              >
                {lang}
              </button>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default LanguageSelector;
