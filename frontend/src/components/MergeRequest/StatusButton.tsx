interface StatusButtonProps {
  label: string;
  count: number;
  isActive: boolean;
  onClick: () => void;
}

export const StatusButton = ({ label, count, isActive, onClick }: StatusButtonProps) => (
  <button
    onClick={onClick}
    className={`flex items-center gap-2 px-4 py-2 bg-white text-primary-500 ${
      isActive ? 'border-b-2 border-primary-500 font-bold' : ''
    }`}
  >
    <span>{label}</span>
    <span className="bg-primary-500 text-white px-3 py-0.5 rounded-2xl text-xs">{count}</span>
  </button>
);
