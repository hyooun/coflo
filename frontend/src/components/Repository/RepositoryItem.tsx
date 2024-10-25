export function RepositoryItem({ name, integrate }: { name: string; integrate: string }) {
  return (
    <div className="p-2 w-[800px]">
      <div className="flex flex-row justify-between ">
        <h2 className="text-xs font-semibold">{name}</h2>
        <h2 className="text-xs font-semibold">{integrate}</h2>
      </div>
    </div>
  );
}
