import { useState } from 'react';
import { StatusButton } from './StatusButton';

export interface MrStatusFilterProps {
  onStatusChange: (status: string) => void;
}

export const MrStatusFilter = ({ onStatusChange }: MrStatusFilterProps) => {
  const [activeStatus, setActiveStatus] = useState('opened');

  const statuses = [
    { label: 'Opened', value: 'opened', count: 2 },
    { label: 'Merged', value: 'merged', count: 41 },
    { label: 'Closed', value: 'closed', count: 4 },
    { label: 'All', value: 'all', count: 4 },
  ];

  const handleStatusClick = (status: string) => {
    setActiveStatus(status);
    onStatusChange(status);
  };

  return (
    <div className="flex gap-4 mb-4">
      {statuses.map((status) => (
        <StatusButton
          key={status.value}
          label={status.label}
          count={status.count}
          isActive={activeStatus === status.value}
          onClick={() => handleStatusClick(status.value)}
        />
      ))}
    </div>
  );
};
