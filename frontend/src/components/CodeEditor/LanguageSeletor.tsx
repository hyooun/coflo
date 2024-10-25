import { LANGUAGE_VERSIONS } from '@constants/codeEditor';

interface LanguageSelectorProps {
  language: string;
  onSelect: (lang: string) => void;
}

const LanguageSelector = ({ language, onSelect }: LanguageSelectorProps) => {
  const languages = Object.keys(LANGUAGE_VERSIONS);

  return (
    <select value={language} onChange={(e) => onSelect(e.target.value)}>
      {languages.map((lang) => (
        <option key={lang} value={lang}>
          {lang}
        </option>
      ))}
    </select>
  );
};

export default LanguageSelector;
