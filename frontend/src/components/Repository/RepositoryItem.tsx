export function RepositoryItem({ name, integrate }: { name: string; integrate: string }) {
  return (
    <div className="flex flex-row justify-between items-center w-full mr-10">
      <h2 className="text-xl font-semibold">{name}</h2>
      <h2 className="text-xs font-semibold">{integrate}</h2>
    </div>
  );
}
