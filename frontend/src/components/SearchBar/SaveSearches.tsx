const SavedSearches = ({
  savedSearches,
  selectedSearches,
  handleToggleSelectSearch,
}: SavedSearchesProps) => {
  return (
    <div className="mt-4  p-2">
      <h4 className="font-bold">저장된 프롬프트:</h4>
      <ul>
        {savedSearches.map((search, index) => (
          <li key={index} className="flex justify-between items-center p-2 ">
            <span className="flex-grow break-words">{search}</span>

            <button
              onClick={() => handleToggleSelectSearch(search)}
              className={`ml-2 ${selectedSearches.includes(search) ? 'bg-blue-500' : 'bg-blue-100'} text-white px-4 py-2 rounded cursor-pointer`}
              style={{ whiteSpace: 'nowrap' }}
            >
              {selectedSearches.includes(search) ? '선택됨' : '선택'}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default SavedSearches;
