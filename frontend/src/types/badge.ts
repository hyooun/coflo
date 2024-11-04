export interface BadgeType {
  badgeCodeId: number;
  name: string;
  description: string;
  imageUrl: string;
  isAcquired: boolean;
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
