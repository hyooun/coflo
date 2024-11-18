import { useEffect, useState } from 'react';
import { ChevronDown, ChevronUp } from 'lucide-react';

const ScrollNavigationButton = () => {
  const [isScrolled, setIsScrolled] = useState(false);

  useEffect(() => {
    const container = document.querySelector('.scroll-container');
    const handleScroll = (e: Event) => {
      const target = e.target as HTMLElement;
      // 컨테이너의 현재 스크롤 위치 확인
      const scrollTop = target.scrollTop;
      setIsScrolled(scrollTop > 0);
    };

    // 초기 스크롤 위치 확인
    if (container) {
      handleScroll({ target: container } as unknown as Event);
    }

    container?.addEventListener('scroll', handleScroll);
    return () => container?.removeEventListener('scroll', handleScroll);
  }, []);

  const scrollToPosition = (position: 'top' | 'bottom') => {
    const container = document.querySelector('.scroll-container');
    if (!container) return;

    if (position === 'top') {
      container.scrollTo({
        top: 0,
        behavior: 'smooth',
      });
    } else {
      container.scrollTo({
        top: container.scrollHeight,
        behavior: 'smooth',
      });
    }
  };

  return (
    <div className="fixed right-8 bottom-8 z-50">
      <button
        onClick={() => scrollToPosition(isScrolled ? 'top' : 'bottom')}
        className="border-2 border-primary-500 bg-white text-primary-500 hover:bg-primary-500 hover:text-white rounded-full p-3 shadow-lg"
      >
        {isScrolled ? <ChevronUp size={32} /> : <ChevronDown size={32} />}
      </button>
    </div>
  );
};

export default ScrollNavigationButton;
