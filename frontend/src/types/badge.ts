export interface BadgeType {
  badgeCodeId: string;
  name: string;
  description: string;
  imageUrl: string;
  isAcquired: boolean;
}

export interface BadgeResponse {
  badgeDetails: BadgeType[];
  mainBadgeCodeId: string;
}

export interface BadgeProps {
  badge: BadgeType;
  onClick?: () => void;
  size?: 'sm' | 'md' | 'lg' | 'xl' | '2xl';
  tooltipOptions?: {
    show?: boolean;
    showName?: boolean;
    showDescription?: boolean;
  };
}

export interface RepresentativeBadgeProps {
  selectedBadge: string | null;
  onSave: () => void;
  onDefault: () => void;
}
