import Header from '@components/Header/Header';
import CustomPromptContainer from '@components/CustomPrompt/CustomPromptContainer';

const CustomPromptPage = () => {
  return (
    <div className="flex flex-col flex-grow overflow-auto px-8 pt-6 ">
      <Header
        title="Custom prompt"
        description={['프로젝트마다 원하는 리뷰 형식으로 요청할 수 있습니다.']}
      />

      <CustomPromptContainer />
    </div>
  );
};

export default CustomPromptPage;
