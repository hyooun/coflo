import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAtom } from 'jotai';
import { FileQuestion } from 'lucide-react';

import { currentPageAtom, totalPagesAtom } from '@store/pagination';
import { projectIdAtom } from '@store/auth.ts';
import Header from '@components/Header/Header';
import { RepositorySearchBar } from '@components/Repository/RepositorySearchBar';
import { RepositoryItem } from '@components/Repository/RepositoryItem';
import ToggleSwitch from '@components/Repository/ToggleSwitch';
import Pagination from '@components/Pagination/Pagination';
import { CommonButton } from '@components/Button/CommonButton';
import GuideModal from '@components/Modal/GuideModal.tsx';
import tokenintro from '@assets/tokenintro.png';
import { GitlabProject } from 'types/gitLab';
import { UserProject } from '@apis/Link';
import { Gitlab } from '@apis/Gitlab';

const MemoizedPagination = React.memo(Pagination);

export default function RepositoryPage() {
  const [currentPage] = useAtom(currentPageAtom);
  const [, setTotalPages] = useAtom(totalPagesAtom);
  const [repositories, setRepositories] = useState<GitlabProject[]>([]);
  const itemsPerPage = 10;
  const [selectedRepo, setSelectedRepo] = useState<GitlabProject | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [inputValue, setInputValue] = useState('');
  const navigate = useNavigate();
  const [, setProjectId] = useAtom(projectIdAtom);

  useEffect(() => {
    const fetchProjects = async () => {
      console.log('현재 페이지:', currentPage);
      const response = await Gitlab.getGitlabProjects(undefined, currentPage, itemsPerPage);
      console.log('API 응답:', response);
      console.log('API 응답:', response.data);
      if (response && response.data) {
        setRepositories(response.data.gitlabProjectList);
        setTotalPages(response.data.totalPages);
      }
    };

    fetchProjects();
  }, [currentPage, setTotalPages]);

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
      await UserProject.updateRepository(selectedRepo.gitlabProjectId, { botToken: inputValue });

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
          }}
          onConfirm={handleModalConfirm}
          gitlabProjectId={String(selectedRepo.gitlabProjectId)}
        />
      )}
      <MemoizedPagination />
    </div>
  );
}
