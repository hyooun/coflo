import { useNavigate } from 'react-router-dom';
import { useAtom } from 'jotai';
import { CircleCheck, LogOut } from 'lucide-react';
import logo from 'assets/logo.png';
import { User } from '@apis/User';
import { isLoginAtom } from '@store/auth';
import { useState } from 'react';
import ConfirmModal from '@components/Modal/ConfirmModal';

export default function Navbar() {
  const navigate = useNavigate();
  const [, setIsLogin] = useAtom(isLoginAtom);
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState(false);
  const [confirmMessage, setConfirmMessage] = useState<string[]>([]);

  const handleLogout = () => {
    setConfirmMessage(['로그아웃 하시겠습니까?']);
    setIsConfirmModalOpen(true);
  };

  const onConfirmLogout = async () => {
    await User.logout();
    setIsLogin(false);
    setIsConfirmModalOpen(false);
    navigate('/login');
  };

  return (
    <div className="min-w-[64px] max-w-[64px] bg-gradient-to-b from-[#1A2036] to-secondary h-screen shadow-2xl text-[#c1d6f2] overflow-hidden flex flex-col justify-between">
      <div className="w-full flex justify-center items-center mt-5">
        <img src={logo} alt="Logo" className="w-7 h-7" />
      </div>
      <div className="flex justify-center items-center mb-5 w-full">
        <LogOut
          className="text-white bg-primary-500 w-10 h-10 rounded-full p-2 cursor-pointer"
          onClick={handleLogout}
        />
      </div>
      {isConfirmModalOpen && (
        <ConfirmModal
          content={confirmMessage}
          onConfirm={onConfirmLogout}
          onCancel={() => setIsConfirmModalOpen(false)}
          icon={CircleCheck}
          iconClassName="text-state-success"
        />
      )}
    </div>
  );
}
