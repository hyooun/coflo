export interface Project {
  id: string;
  name: string;
  isCurrent?: boolean;
}

export interface ProjectSelectorProps {
  onClose: () => void;
  titleRef: React.RefObject<HTMLDivElement>;
}
