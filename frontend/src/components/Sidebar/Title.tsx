import { GitlabIcon } from './Icons/Gitlab';
import { SelectorIcon } from './Icons/Selector';

export function Title() {
  return (
    <div className="flex items-center w-full py-6">
      <div className="ml-3">
        <GitlabIcon />
      </div>

      <div className="pl-3">
        <div className="text-sm font-bold text-[#42526e]">aaaa aaaaaa</div>
        <div className="text-sm text-[#5e6c84]">프로젝트 변경</div>
      </div>
      <div className="ml-3">
        <SelectorIcon />
      </div>
    </div>
  );
}
