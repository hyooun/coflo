import SearchInput from './SearchInput';
import CategoryButton from './CategoryButton';
import { useState, useEffect } from 'react';
import SavedSearches from './SaveSearches';

const SearchComponent = () => {
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [savedSearches, setSavedSearches] = useState<string[]>([]);
  const [selectedSearches, setSelectedSearches] = useState<string[]>([]);

  const categoriesData: { [key: string]: string[] } = {
    카테고리1: ['항목1-1', '항목1-2', '항목1-3'],
    카테고리2: ['항목2-1', '항목2-2', '항목2-3'],
    카테고리3: ['항목3-1', '항목3-2', '항목3-3'],
    카테고리4: ['항목4-1', '항목4-2', '항목4-3'],
  };

  const handleCategoryClick = (category: string) => {
    setSelectedCategory(category);
    console.log(`Selected category: ${category}`);
  };

  const handleClickOutside = (event: MouseEvent) => {
    if (!(event.target instanceof HTMLElement) || !event.target.closest('.category-button')) {
      setSelectedCategory(null);
    }
  };

  const handleSaveSearch = () => {
    if (searchTerm) {
      setSavedSearches((prev) => [...prev, searchTerm]);
      setSearchTerm('');
    }
  };

  const handleToggleSelectSearch = (search: string) => {
    setSelectedSearches((prev) =>
      prev.includes(search) ? prev.filter((s) => s !== search) : [...prev, search],
    );
  };

  useEffect(() => {
    document.addEventListener('click', handleClickOutside);
    return () => {
      document.removeEventListener('click', handleClickOutside);
    };
  }, []);

  return (
    <>
      <div className="bg-white p-4 w-[700px] flex flex-col">
        <SearchInput
          searchTerm={searchTerm}
          setSearchTerm={setSearchTerm}
          handleSaveSearch={handleSaveSearch}
        />
        <div className="flex flex-wrap mt-4">
          {Object.keys(categoriesData).map((category) => (
            <CategoryButton
              key={category}
              category={category}
              onClick={handleCategoryClick}
              className="category-button"
            />
          ))}
        </div>
        {selectedCategory && (
          <div className="mt-4 text-gray-700">
            <p>선택한 카테고리: {selectedCategory}</p>
            <ul>
              {categoriesData[selectedCategory].map((item, index) => (
                <li key={index}>{item}</li>
              ))}
            </ul>
          </div>
        )}
      </div>
      <div className="bg-white p-4 w-[1100px] flex flex-col">
        <SavedSearches
          savedSearches={savedSearches}
          selectedSearches={selectedSearches}
          handleToggleSelectSearch={handleToggleSelectSearch}
        />
      </div>
    </>
  );
};

export default SearchComponent;
