import Header from '@components/Header/Header';
import SearchComponent from '@components/SearchBar/SearchComponent';

const CustomPromptPage = () => {
  return (
    <div className="flex flex-col flex-grow overflow-auto px-8 pt-6 ">
      <Header title="Custom prompt"></Header>

      <SearchComponent />
    </div>
  );
};

export default CustomPromptPage;
