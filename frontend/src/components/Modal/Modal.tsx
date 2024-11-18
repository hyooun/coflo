import tokenintro from '@assets/tokenintro.png';
import { ModalProps } from 'types/modal.ts';

export const Modal = ({ inputValue, setInputValue, onConfirm, onClose }: ModalProps) => {
  return (
    <div className="fixed inset-0 border bg-black bg-opacity-50 flex items-center justify-center">
      <div className="bg-white p-8 border-2 border-[#2C365B] rounded-xl w-[600px] h-[650px] relative">
        <button
          onClick={onClose}
          className="absolute top-3 right-3 w-6 h-6 flex items-center justify-center text-3xl text-gray-600 font-pretendard"
        >
          &times;
        </button>

        <h1 className="text-center text-3xl mt-8 mb-8 font-pretendard">
          프로젝트 토큰을 얻어오는 방법
        </h1>

        <p className="mb-2 font-pretendard">1. 버튼을 클릭하여 프로젝트 검색을 시작하세요.</p>
        <p className="mb-2 font-pretendard">2. 설정(Settings)으로 이동하세요.</p>
        <p className="mb-2 font-pretendard">3. Access Tokens 메뉴를 선택하세요.</p>
        <p className="mb-2 font-pretendard">4. Project Access Tokens를 생성하세요.</p>
        <p className="mb-2 font-pretendard">5. API 체크를 확인하세요.</p>
        <p className="mb-5 font-pretendard">6. Create Access Tokens를 생성하세요.</p>
        <img src={tokenintro} alt="Project Token Instructions" className="mb-4" />

        <div className="flex items-center mb-4">
          <input
            id="token-input"
            type="text"
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            placeholder="프로젝트 토큰을 입력하세요"
            className="border border-gray-300 p-2 rounded w-5/6 font-pretendard"
          />
        </div>

        <div className="mb-8 text-center">
          <a
            href="https://docs.gitlab.com/ee/user/project/settings/project_access_tokens.html"
            target="_blank"
            rel="noopener noreferrer"
            className="text-[#2C365B] font-pretendard"
          >
            프로젝트 토큰 생성 가이드 보기
          </a>
        </div>

        <div className="flex justify-end">
          <button
            onClick={onConfirm}
            className="bg-primary-500 items-center w-[100px] text-white rounded p-2 rounded font-pretendard hover:cursor-pointer"
          >
            확인
          </button>
        </div>
      </div>
    </div>
  );
};
