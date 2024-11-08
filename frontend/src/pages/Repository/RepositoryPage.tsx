import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAtom } from 'jotai';
import { FileQuestion } from 'lucide-react';

import { projectIdAtom } from '@store/auth.ts';
import Header from '@components/Header/Header';
import { RepositorySearchBar } from '@components/Repository/RepositorySearchBar';
import { RepositoryItem } from '@components/Repository/RepositoryItem';
import ToggleSwitch from '@components/Repository/ToggleSwitch';
import { CommonButton } from '@components/Button/CommonButton';
import GuideModal from '@components/Modal/GuideModal.tsx';
import tokenintro from '@assets/tokenintro.png';
import { GitlabProject, PageInfo } from 'types/gitLab';
import { UserProject } from '@apis/Link';
import { Gitlab } from '@apis/Gitlab';
import BranchSelector from '@components/Repository/BranchSelector';
import { BranchOption } from '@components/Repository/BranchSelector';
import CursorPagination from '@components/Pagination/CursorPagination';

export default function RepositoryPage() {
  const [repositories, setRepositories] = useState<GitlabProject[]>([]);
  const [selectedBranches, setSelectedBranches] = useState<BranchOption[]>([]);
  const [selectedRepo, setSelectedRepo] = useState<GitlabProject | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [inputValue, setInputValue] = useState('');
  const navigate = useNavigate();
  const [, setProjectId] = useAtom(projectIdAtom);
  const [pageInfo, setPageInfo] = useState<PageInfo>({
    startCursor: '',
    endCursor: '',
    hasNextPage: false,
    hasPreviousPage: false,
  });
  const [keyword, setKeyword] = useState('');

  const fetchProjects = async (pageInfo?: PageInfo) => {
    const response = await Gitlab.getGitlabProjects(
      keyword,
      pageInfo || {
        startCursor: '',
        endCursor: '',
        hasNextPage: false,
        hasPreviousPage: false,
      },
    );
    if (response && response.data) {
      setRepositories(response.data.gitlabProjectList);
      setPageInfo(response.data.pageInfo);
    }
  };

  useEffect(() => {
    fetchProjects(pageInfo);
  }, []);

  const handleNextPage = () => {
    if (pageInfo.hasNextPage) {
      fetchProjects({
        ...pageInfo,
        startCursor: pageInfo.startCursor,
      });
    }
  };

  const handlePreviousPage = () => {
    if (pageInfo.hasPreviousPage) {
      fetchProjects({
        ...pageInfo,
        endCursor: pageInfo.endCursor,
      });
    }
  };

  const handleInputChange = (value: string) => {
    setInputValue(value);
  };

  const handleToggleChange = async (index: number) => {
    const repo = repositories[index];

    if (repo.isLinkable) {
      if (repo.isLinked) {
        await UserProject.deleteRepository(repo.gitlabProjectId);
        setRepositories((prev) => {
          const updatedRepos = [...prev];
          updatedRepos[index] = { ...updatedRepos[index], isLinked: false };
          return updatedRepos;
        });
      } else {
        await UserProject.updateRepository(repo.gitlabProjectId, {});
        setRepositories((prev) => {
          const updatedRepos = [...prev];
          updatedRepos[index] = { ...updatedRepos[index], isLinked: true };
          return updatedRepos;
        });
      }
    } else {
      setSelectedRepo(repo);
      setIsModalOpen(true);
    }
  };

  const handleModalConfirm = async () => {
    if (selectedRepo) {
      const branchNames = selectedBranches.map((branch) => branch.value);

      await UserProject.updateRepository(selectedRepo.gitlabProjectId, {
        botToken: inputValue,
        branches: branchNames,
      });

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

  const handleButtonClick = async () => {
    const response = await UserProject.getLinkStatus();
    if (response?.data) {
      const { hasLinkedProject, projectId } = response.data;
      setProjectId(projectId);
      if (hasLinkedProject) {
        navigate(`/${projectId}/main`);
      } else {
        alert('연동되지 않았습니다. 먼저 연동을 완료해주세요.');
      }
    }
  };

  return (
    <div className="flex flex-col ml-[80px] p-6 w-full">
      <div className="flex flex-row justify-between items-center pr-3">
        <div>
          <Header
            title="Repository"
            description={['내 프로젝트에서 리뷰할 프로젝트를 선택합니다.']}
          />
        </div>
        <CommonButton
          className="px-4 min-w-[100px] h-[50px]"
          active={false}
          bgColor="bg-primary-500"
          onClick={handleButtonClick}
        >
          시작하기
        </CommonButton>
      </div>

      <RepositorySearchBar />

      <div className="bg-white w-full">
        {repositories.map((repo, index) => (
          <div key={repo.gitlabProjectId}>
            <div className="flex items-center justify-between p-4">
              <RepositoryItem
                name={repo.name}
                integrate={repo.isLinkable ? '' : '프로젝트 토큰을 설정해주세요'}
              />
              <ToggleSwitch checked={repo.isLinked} onChange={() => handleToggleChange(index)} />
            </div>
            {index < repositories.length - 1 && <div className="border-t border-gray-300" />}
          </div>
        ))}
      </div>

      {isModalOpen && selectedRepo && (
        <GuideModal
          isOpen={isModalOpen}
          title="프로젝트 토큰을 얻어오는 방법"
          width="w-[600px]"
          content={
            <div className="space-y-2">
              <p>1. 버튼을 클릭하여 프로젝트 검색을 시작하세요.</p>
              <p>2. 설정(Settings)으로 이동 후 Access Tokens 메뉴를 선택하세요.</p>
              <p>3. API 체크 후 Project Access Tokens를 생성하세요.</p>
            </div>
          }
          contentBottom={
            <div className="mt-4">
              <label className="block mb-2 text-2xl">참조할 브랜치</label>
              <BranchSelector
                value={selectedBranches}
                onChange={setSelectedBranches}
                gitlabProjectId={Number(selectedRepo?.gitlabProjectId)}
              />
            </div>
          }
          image={{
            src: tokenintro,
            alt: 'Project Token Instructions',
          }}
          hasInput
          inputProps={{
            value: inputValue,
            onChange: handleInputChange,
            placeholder: '프로젝트 토큰을 입력하세요',
            labelText: '프로젝트 토큰',
          }}
          links={[
            {
              url: 'https://docs.gitlab.com/ee/user/project/settings/project_access_tokens.html',
              text: '프로젝트 토큰 생성 가이드 보기',
              icon: <FileQuestion size={20} className="text-primary-500" />,
            },
          ]}
          onClose={() => {
            setIsModalOpen(false);
            setSelectedRepo(null);
            setInputValue('');
            setSelectedBranches([]);
          }}
          onConfirm={handleModalConfirm}
          gitlabProjectId={String(selectedRepo.gitlabProjectId)}
        ></GuideModal>
      )}

      {/* <MemoizedPagination /> */}
      <CursorPagination
        hasNextPage={pageInfo.hasNextPage}
        hasPreviousPage={pageInfo.hasPreviousPage}
        onNext={handleNextPage}
        onPrevious={handlePreviousPage}
      />
    </div>
  );
}
