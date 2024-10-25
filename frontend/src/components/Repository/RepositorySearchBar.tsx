import { SearchIcon } from '../SearchBar/Icons/Search';

export function RepositorySearchBar() {
  return (
    <div className="relative mb-2">
      <input
        type="text"
        className="p-4 pl-12 border border-gray-300 rounded-md w-[500px] h-[30px]"
        placeholder="Search results..."
      />
      <div className="absolute inset-y-0 left-3 flex items-center pointer-events-none">
        <SearchIcon />
      </div>
    </div>
  );
}
