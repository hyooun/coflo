interface SearchInputProps {
  searchTerm: string;
  setSearchTerm: (term: string) => void;
  handleSaveSearch: (term: string) => void;
}

interface CategoryButtonProps {
  category: string;
  onClick: (category: string) => void;
  className?: string;
}

interface SavedSearchesProps {
  savedSearches: string[];
  selectedSearches: string[];
  handleToggleSelectSearch: (search: string) => void;
}
