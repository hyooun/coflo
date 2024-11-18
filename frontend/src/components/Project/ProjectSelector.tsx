import { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Search } from 'lucide-react';
import { Project } from 'types/project';
import { ProjectSelectorProps } from 'types/project';
import { projectFullPathAtom, projectIdAtom } from '@store/auth';
import { useAtom } from 'jotai';
import { UserProject } from '@apis/Link';
import { User } from '@apis/User';

const ProjectSelector = ({ onClose, titleRef }: ProjectSelectorProps) => {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState('');
  const [projects, setProjects] = useState<Project[]>([]);
  const searchInputRef = useRef<HTMLInputElement>(null);
  const selectorRef = useRef<HTMLDivElement>(null);

  const [currentProjectId, setCurrentProjectId] = useAtom(projectIdAtom);
  const [projectFullPath, setProjectFullPath] = useAtom(projectFullPathAtom);
  const [, setSelectedProject] = useState<Project>({
    projectId: currentProjectId || '',
    name: projectFullPath || '',
  });

  const filterFunction = (project: Project, query: string) =>
    project.name.toLowerCase().includes(query.toLowerCase());

  const filteredItems = projects.filter((project) => filterFunction(project, searchQuery));

  useEffect(() => {
    if (!currentProjectId) return;
    const fetchedProjects = async () => {
      try {
        const response = await UserProject.getUserProjects({ currentProjectId });
        setProjects(response.data || []);
      } catch (error) {
        console.error('Error fetching projects:', error);
      }
    };

    fetchedProjects();

    const projectsWithCurrent = projects.map((project) => ({
      ...project,
      isCurrent: project.projectId === currentProjectId,
    }));

    setProjects(projectsWithCurrent);
    setSelectedProject(projectsWithCurrent.find((p) => p.isCurrent) || projectsWithCurrent[0]);
  }, [currentProjectId]);

  useEffect(() => {
    searchInputRef.current?.focus();
  }, []);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (titleRef.current?.contains(event.target as Node)) {
        event.stopPropagation();
        return;
      }

      if (selectorRef.current?.contains(event.target as Node)) {
        return;
      }

      onClose();
    };

    document.addEventListener('mousedown', handleClickOutside, true);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside, true);
    };
  }, [onClose, titleRef]);

  const handleProjectClick = async (project: Project) => {
    const response = await User.updateRecentProject(Number(project.projectId));
    if (response.status === 'SUCCESS') {
      setCurrentProjectId(project.projectId);
      setProjectFullPath(project.name);
      setSelectedProject(project);
      onClose();
      navigate(`/${project.projectId}/main`);
    }
  };

  return (
    <div ref={selectorRef} className="w-[240px] bg-white rounded-lg shadow-lg border-2 ">
      <div className="flex justify-between items-center py-2 px-4 border-b-2 ">
        <h2 className="font-semibold text-lg tracking-tighter">Sync Projects</h2>
        <button
          onClick={() => navigate('/repository')}
          className="text-sm px-3 py-0.5 bg-primary-500 text-white rounded-full hover:bg-secondary"
        >
          추가 연동
        </button>
      </div>

      <div className="p-2">
        <div className="relative">
          <input
            ref={searchInputRef}
            type="text"
            className="w-full pl-8 pr-3 py-1.5 text-sm bg-gray-200 border border-gray-200 rounded-full focus:outline-none placeholder:text-gray-600"
            placeholder="프로젝트 검색..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-primary-500" />
        </div>

        <div className="mt-2 max-h-64 overflow-y-auto">
          {filteredItems.length === 0 ? (
            <div className="text-center text-primary-500 p-2"> 검색된 결과가 없습니다. </div>
          ) : (
            filteredItems.map((project) => (
              <button
                key={project.projectId}
                onClick={() => handleProjectClick(project)}
                className="w-full text-left px-4 py-2 text-sm hover:bg-gray-100 transition-colors flex justify-between items-center"
              >
                <span className="truncate">{project.name}</span>
                {Number(project.projectId) === Number(currentProjectId) && (
                  <span className="text-xs bg-primary-500 text-white px-2 py-0.5 rounded-full">
                    current
                  </span>
                )}
              </button>
            ))
          )}
        </div>
      </div>
    </div>
  );
};

export default ProjectSelector;
