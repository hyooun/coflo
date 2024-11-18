export function RepositoryItem({ name, integrate }: { name: string; integrate: string }) {
  return (
    <div className="flex flex-row justify-between items-center w-full mr-10 min-w-0">
      <span className="text-xl font-semibold truncate flex-1 mr-4">{name}</span>
      <span className="text-sm font-semibold whitespace-nowrap">{integrate}</span>
    </div>
  );
}
