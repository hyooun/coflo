import { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Search } from 'lucide-react';
import { Project } from 'types/project';
import { ProjectSelectorProps } from 'types/project';

const ProjectSelector = ({ onClose, titleRef }: ProjectSelectorProps) => {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState('');
  const [projects, setProjects] = useState<Project[]>([]);
  const [selectedProject, setSelectedProject] = useState<Project>({ id: '', name: '' });
  const searchInputRef = useRef<HTMLInputElement>(null);
  const selectorRef = useRef<HTMLDivElement>(null);

  // 임시 현재 프로젝트 ID
  const currentProjectId = 'project1';

  const filterFunction = (project: Project, query: string) =>
    project.name.toLowerCase().includes(query.toLowerCase());

  const filteredItems = projects.filter((project) => filterFunction(project, searchQuery));

  useEffect(() => {
    // 임시 데이터
    const fetchedProjects = [
      { id: 'project1', name: '프로젝트 1' },
      { id: 'reviewping', name: 'ReviewPing' },
      { id: 'melting', name: 'Melting' },
      { id: 'project3', name: '프로젝트 3' },
      { id: 'project4', name: '프로젝트 4' },
      { id: 'project5', name: '프로젝트 5' },
    ];

    const projectsWithCurrent = fetchedProjects.map((project) => ({
      ...project,
      isCurrent: project.id === currentProjectId,
    }));

    setProjects(projectsWithCurrent);
    setSelectedProject(projectsWithCurrent.find((p) => p.isCurrent) || projectsWithCurrent[0]);
  }, []);

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
                key={project.id}
                onClick={() => {
                  setSelectedProject(project);
                  onClose();
                }}
                className="w-full text-left px-4 py-2 text-sm hover:bg-gray-100 transition-colors flex justify-between items-center"
              >
                <span>{project.name}</span>
                {project.isCurrent && (
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
