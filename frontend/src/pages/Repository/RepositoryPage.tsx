import { useState, useEffect } from 'react';
import { useAtom } from 'jotai';
import { currentPageAtom, totalPagesAtom } from '@store/pagination';
import { RepositorySearchBar } from '@components/Repository/RepositorySearchBar';
import { RepositoryItem } from '@components/Repository/RepositoryItem';
import ToggleSwitch from '@components/Repository/ToggleSwitch';
import Pagination from '@components/Pagination/Pagination';
import { Link } from '@apis/Link';
import { GitlabProject } from 'types/gitLab';
import { CommonButton } from '@components/Button/CommonButton';
import { useNavigate } from 'react-router-dom';
import GuideModal from '@components/Modal/GuideModal.tsx';
import tokenintro from '@assets/tokenintro.png';

export default function RepositoryPage() {
  const [currentPage, setCurrentPage] = useAtom(currentPageAtom);
  const [totalPages, setTotalPages] = useAtom(totalPagesAtom);
  const [repositories, setRepositories] = useState<GitlabProject[]>([]);
  const itemsPerPage = 10;
  const [selectedRepo, setSelectedRepo] = useState<GitlabProject | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [inputValue, setInputValue] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProjects = async () => {
      console.log('현재 페이지:', currentPage);
      const response = await Link.getLinkRepository('some-keyword', currentPage, itemsPerPage);
      console.log('API 응답:', response);
      if (response && response.data) {
        setRepositories(response.data.gitlabProjectList);
        setTotalPages(response.data.totalPages);
      }
    };

    fetchProjects();
  }, [currentPage, setTotalPages]);

  const handleToggleChange = (index: number) => {
    const repo = repositories[index];
    if (repo.isLinkable) {
      setRepositories((prev) => {
        const updatedRepos = [...prev];
        updatedRepos[index] = { ...updatedRepos[index], isLinked: !updatedRepos[index].isLinked };
        return updatedRepos;
      });
    } else {
      setSelectedRepo(repo);
      setIsModalOpen(true);
    }
  };

  const handleModalConfirm = async () => {
    if (selectedRepo) {
      await Link.updateRepository(selectedRepo.gitlabProjectId, { token: inputValue });

      setRepositories((prev) => {
        const updatedRepos = [...prev];
        const index = repositories.findIndex(
          (repo) => repo.gitlabProjectId === selectedRepo.gitlabProjectId,
        );
        updatedRepos[index] = { ...updatedRepos[index], isLinked: true, isLinkable: true };
        return updatedRepos;
      });

      setIsModalOpen(false);
      setSelectedRepo(null);
      setInputValue('');
    }
  };

  const handleButtonClick = () => {
    navigate('/main');
  };

  return (
    <div className="max-w-3xl ml-[80px] p-6">
      <h1 className="text-3xl ml-[20px] font-bold mb-3">Repository</h1>
      <div className="flex items-center justify-between w-[1000px]">
        <h2 className="text-xl mb-3">내 프로젝트에서 리뷰할 프로젝트를 선택합니다.</h2>
        <CommonButton
          className="px-4 w-[100px] h-[50px]"
          active={false}
          onClick={handleButtonClick}
        >
          시작하기
        </CommonButton>
      </div>

      <RepositorySearchBar />

      <div className="bg-white w-[1000px]">
        {repositories.map((repo, index) => (
          <div key={repo.gitlabProjectId}>
            <div className="flex items-center justify-between p-4">
              <RepositoryItem
                name={repo.name}
                integrate={repo.isLinkable ? '' : '프로젝트 토큰이 없습니다'}
              />
              <ToggleSwitch checked={repo.isLinked} onChange={() => handleToggleChange(index)} />
            </div>
            {index < repositories.length - 1 && <div className="border-t border-gray-300" />}
          </div>
        ))}
      </div>

      {isModalOpen && (
        <GuideModal
          isOpen={isModalOpen}
          title="프로젝트 토큰을 얻어오는 방법"
          width="w-[600px]"
          content={
            <div className="space-y-2">
              <p>1. 버튼을 클릭하여 프로젝트 검색을 시작하세요.</p>
              <p>2. 설정(Settings)으로 이동하세요.</p>
              <p>3. Access Tokens 메뉴를 선택하세요.</p>
              <p>4. Project Access Tokens를 생성하세요.</p>
              <p>5. API 체크를 확인하세요.</p>
              <p>6. Create Access Tokens를 생성하세요.</p>
            </div>
          }
          image={{
            src: tokenintro,
            alt: 'Project Token Instructions',
          }}
          hasInput
          inputProps={{
            value: inputValue,
            onChange: setInputValue,
            placeholder: '프로젝트 토큰을 입력하세요',
          }}
          link={{
            url: 'https://docs.gitlab.com/ee/user/project/settings/project_access_tokens.html',
            text: '프로젝트 토큰 생성 가이드 보기',
          }}
          onClose={() => {
            setIsModalOpen(false);
            setSelectedRepo(null);
            setInputValue('');
          }}
          onConfirm={handleModalConfirm}
        />
      )}
      <Pagination />
    </div>
  );
}
