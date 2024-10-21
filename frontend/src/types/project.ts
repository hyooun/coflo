export interface Project {
  id: string;
  date: string;
  language: string;
}

export interface ProjectCardItemProps {
  project: Project;
  cardWidth: string;
  cardHeight: string;
}

export interface ProjectCardProps {
  cardWidth?: string;
  cardHeight?: string;
}
