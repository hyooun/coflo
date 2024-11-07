export type ReferenceType = 'CODE' | 'TEXT';

export interface Reference {
  id: number;
  fileName: string;
  language: string;
  content: string;
  relevance?: number;
}

export interface ReferenceProps extends Reference {
  createdAt?: string;
  backgroundColor?: string;
}

export interface CommonReferenceProps extends ReferenceProps {
  onEdit: (id: number, content: string) => void;
  onDelete: (id: number) => void;
  onLanguageChange?: (language: string) => void;
  maxLength?: number;
  type: ReferenceType;
}

export interface ReferencesListProps {
  references: Reference[];
}
