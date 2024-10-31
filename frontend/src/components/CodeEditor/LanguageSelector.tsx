import { useState, useRef, useEffect } from 'react';
import { LANGUAGE_VERSIONS } from '@constants/codeEditor';
import { ChevronDown } from 'lucide-react';

interface LanguageSelectorProps {
  language: string;
  onSelect: (lang: string) => void;
}

const LanguageSelector = ({ language, onSelect }: LanguageSelectorProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const languages = Object.keys(LANGUAGE_VERSIONS);
  const dropdownRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  return (
    <div className="relative" ref={dropdownRef}>
      <button
        onClick={() => setIsOpen(!isOpen)}
        className="flex items-center gap-2 px-3 py-1.5 text-sm text-gray-500 hover:bg-secondary rounded-md transition-colors"
      >
        <span>{language}</span>
        <ChevronDown className={`w-4 h-4 transition-transform ${isOpen ? 'rotate-180' : ''}`} />
      </button>

      {isOpen && (
        <div className="absolute z-20 mt-1 w-48 bg-white rounded-lg shadow-lg border border-gray-200 py-1 max-h-60 overflow-y-auto">
          {languages.map((lang) => (
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
      )}
    </div>
  );
};

export default LanguageSelector;
