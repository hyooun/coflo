import { Project } from 'types/project';

export const fetchProjects = async (): Promise<Project[]> => {
  try {
    const response = await fetch('/api/projects');
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching projects:', error);
    return [];
  }
};
