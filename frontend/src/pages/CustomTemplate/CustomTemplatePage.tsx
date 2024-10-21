import Header from '@components/Header/Header';
import SearchComponent from '@components/SearchBar/SearchComponent';

const CustomTemplatePage = () => {
  return (
    <div className="flex flex-col flex-grow overflow-auto px-8 pt-6 ">
      <Header title="Custom Template"></Header>

      <SearchComponent />
    </div>
  );
};

export default CustomTemplatePage;
