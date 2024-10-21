import { useQuery } from '@tanstack/react-query';
import { Project } from 'types/project';
import { fetchProjects } from '@components/Project/Project';

export const useProjects = () => {
  return useQuery<Project[], Error>({
    queryKey: ['projects'],
    queryFn: fetchProjects,
  });
};
