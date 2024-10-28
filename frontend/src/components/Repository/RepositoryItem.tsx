export function RepositoryItem({ name, integrate }: { name: string; integrate: string }) {
  return (
    <div className=" pl-3 w-[800px]">
      <div className="flex flex-row justify-between ">
        <h2 className="text-lx font-semibold">{name}</h2>
        <h2 className="text-xs font-semibold">{integrate}</h2>
      </div>
    </div>
  );
}
