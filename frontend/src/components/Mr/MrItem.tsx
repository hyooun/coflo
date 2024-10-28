import { MrItemProps } from 'types/mr';
import LabelList from './LabelList';

const labels = ['CI', 'Backend', '🐛 Fix', '✨ Feature', '♻️ Refactor'];

export function MrItem({ mergeRequest }: MrItemProps) {
  const {
    title,
    state,
    assignee,
    reviewer,
    created_at,
    updated_at,
    source_branch,
    target_branch,
    description,
    labels,
  } = mergeRequest;

  return (
    <div className="p-2 w-[1000px] border-b border-gray-300">
      <div className="flex flex-row justify-between">
        <h2 className="text-xs font-semibold">{title}</h2>
      </div>
      <div className="flex flex-row justify-between mt-1">
        <div className="flex items-center">
          {assignee ? (
            <>
              <img
                src={assignee.avatar_url}
                alt={`${assignee.username}의 아바타`}
                className="w-5 h-5 rounded-full mr-1"
              />
              <span className="text-xs">{assignee.username}</span>
            </>
          ) : (
            <span className="text-xs text-gray-500">No Assignee</span>
          )}
        </div>
        <div className="flex items-center">
          {reviewer ? (
            <>
              <img
                src={reviewer.avatar_url}
                alt={`${reviewer.username}의 아바타`}
                className="w-5 h-5 rounded-full mr-1"
              />
            </>
          ) : (
            <span className="text-xs text-gray-500"></span>
          )}
        </div>
      </div>
      <div className="mt-2 text-xs text-gray-600 flex justify-between">
        <div>
          <p>
            <strong>created</strong> {new Date(created_at).toLocaleString()}
          </p>
          <p>
            <strong>Source Branch:</strong> {source_branch}
          </p>
          <p>
            <strong>Target Branch:</strong> {target_branch}
          </p>
          <LabelList labels={labels} />
        </div>
        <div className="text-right">
          <p>
            <strong>updated</strong> {new Date(updated_at).toLocaleString()}
          </p>
        </div>
      </div>
    </div>
  );
}
