import { useState, useEffect, useRef } from 'react';
import {
  GitMerge,
  Zap,
  Search,
  GitBranch,
  ArrowRight,
  ChevronDown,
  ChevronLeft,
  ChevronRight,
} from 'lucide-react';

import LogoImage from '@/assets/logo.png';
import Flowping from '@/assets/images/flowfing.png';
import LandingImage0 from '@/assets/images/landing/landing_image00.png';
import LandingImage1 from '@/assets/images/landing/landing_image01.png';
import LandingImage2 from '@/assets/images/landing/landing_image02.png';
import ReviewComment from '@components/MergeRequest/Review/ReviewComment';

const ReviewDemo = () => (
  <div className="bg-white rounded-lg shadow-xl p-6 w-full max-w-2xl">
    <div className="flex items-center gap-2 mb-4 text-gray-600 border-b pb-4 ">
      <GitMerge className="w-5 h-5" />
      <span className="text-xs font-SFMono">src/example/demo/controller/UserController.java</span>
      <span className="text-gray-600 text-sm">•</span>
      <span className="text-gray-600 text-xs">2 minutes ago</span>
    </div>
    <ReviewComment
      title="보안 개선 제안"
      type="CODE"
      content={`비밀번호 해싱에 SHA-256 대신 bcrypt 사용을 고려해보세요:
bcrypt는 더 안전한 해싱 알고리즘이며 자동으로 salt를 생성하고 적용합니다.`}
      reviewer={{
        id: 0,
        name: 'coFlo AI',
        username: 'coflo-ai',
        avatarUrl: Flowping,
      }}
      createdAt={new Date().toISOString()}
    />
  </div>
);

const FullPageSection = ({
  children,
  className = '',
}: {
  children: React.ReactNode;
  className?: string;
}) => (
  <div className={`h-screen w-full flex items-center justify-center ${className}`}>
    <div className="container mx-auto px-6">{children}</div>
  </div>
);

const LandingPage = () => {
  const [currentSection, setCurrentSection] = useState(0);
  const [isScrolling, setIsScrolling] = useState(false);
  const sections = useRef(5); // 섹션 수
  const [currentImage, setCurrentImage] = useState(0);

  const carouselImages = [
    { src: LandingImage0, alt: '작동방식 1' },
    { src: LandingImage1, alt: '작동방식 2' },
    { src: LandingImage2, alt: '작동방식 3' },
  ];

  useEffect(() => {
    const handleWheel = (e: WheelEvent) => {
      if (isScrolling) return;

      setIsScrolling(true);
      const direction = e.deltaY > 0 ? 1 : -1;

      setCurrentSection((prev) => {
        const next = prev + direction;
        if (next < 0) return 0;
        if (next >= sections.current) return sections.current - 1;
        return next;
      });

      setTimeout(() => setIsScrolling(false), 1000);
    };

    window.addEventListener('wheel', handleWheel);
    return () => window.removeEventListener('wheel', handleWheel);
  }, [isScrolling]);

  const scrollIndicator = (
    <div className="fixed bottom-8 right-8 flex flex-col items-center gap-2">
      <div className="text-sm font-medium text-gray-300">
        {currentSection + 1} / {sections.current}
      </div>
      <div className="w-1 h-20 bg-gray-200 rounded-full relative">
        <div
          className="absolute top-0 w-full bg-primary-500 rounded-full transition-all duration-500"
          style={{
            height: `${((currentSection + 1) / sections.current) * 100}%`,
          }}
        />
      </div>
    </div>
  );

  const navigationDots = (
    <div className="fixed right-8 top-1/2 -translate-y-1/2 flex flex-col gap-3">
      {Array.from({ length: sections.current }).map((_, index) => (
        <button
          key={index}
          className={`w-3 h-3 rounded-full transition-all duration-300 
            ${currentSection === index ? 'bg-primary-500 scale-125' : 'bg-gray-300'}`}
          onClick={() => setCurrentSection(index)}
        />
      ))}
    </div>
  );

  return (
    <div className="h-screen overflow-hidden relative">
      {/* Fixed Navigation */}
      <nav className="fixed top-0 left-0 right-0 z-50 bg-white/80 backdrop-blur-sm border-b">
        <div className="container mx-auto px-6 py-4">
          <div className="flex justify-between items-center">
            <div className="flex items-center gap-2">
              <img src={LogoImage} alt="logo" className="w-8 h-8" />
              <span className="text-xl font-bold">coFlo</span>
            </div>
          </div>
        </div>
      </nav>

      {/* Scroll Container */}
      <div
        className="transition-transform duration-1000 ease-in-out"
        style={{ transform: `translateY(-${currentSection * 100}vh)` }}
      >
        {/* Hero Section */}
        <FullPageSection className="bg-gradient-to-b from-white to-gray-50">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            <div>
              <h1 className="flex flex-col gap-4 text-5xl font-bold mb-6">
                <p>AI 기반 맞춤형</p>
                <p>코드 리뷰 플랫폼</p>
              </h1>
              <p className="text-xl text-primary-500 mb-8">
                프로젝트의 맥락을 이해하고 팀의 코딩 스타일을 반영하는 맞춤형 코드 리뷰로 개발
                생산성을 높이세요.
              </p>
              <button
                className="bg-primary-500 text-white px-8 py-4 rounded-full 
                text-lg hover:bg-primary-600 transition-colors inline-flex items-center gap-2"
              >
                시작하기
                <ArrowRight className="w-5 h-5" />
              </button>
            </div>
            <div className="relative">
              <ReviewDemo />
              <ChevronDown
                className="absolute bottom-[-60px] left-1/2 transform -translate-x-1/2 
                w-8 h-8 text-gray-400 animate-bounce"
              />
            </div>
          </div>
        </FullPageSection>

        {/* Features Section */}
        <FullPageSection className="bg-gray-50">
          <div>
            <h2 className="text-3xl font-bold text-center mb-16">개발 생산성을 높이는 핵심 기능</h2>
            <div className="grid md:grid-cols-3 gap-8">
              <div className="bg-white p-6 rounded-lg shadow-lg">
                <GitBranch className="w-8 h-8 text-primary-500 mb-4" />
                <h3 className="text-xl font-semibold mb-3">GitLab 자동 연동</h3>
                <p className="text-primary-500">
                  웹훅 설정만으로 Merge Request가 생성될 때마다 자동으로 리뷰가 시작됩니다.
                </p>
              </div>
              <div className="bg-white p-6 rounded-lg shadow-lg">
                <Search className="w-8 h-8 text-primary-500 mb-4" />
                <h3 className="text-xl font-semibold mb-3">컨텍스트 기반 분석</h3>
                <p className="text-primary-500">
                  이슈와 MR 히스토리를 분석하여 프로젝트의 맥락을 이해하고 더 정확한 리뷰를
                  제공합니다.
                </p>
              </div>
              <div className="bg-white p-6 rounded-lg shadow-lg">
                <Zap className="w-8 h-8 text-primary-500 mb-4" />
                <h3 className="text-xl font-semibold mb-3">맞춤형 리뷰</h3>
                <p className="text-primary-500">
                  팀의 코딩 스타일과 컨벤션을 반영한 커스텀 템플릿으로 일관된 품질의 리뷰를
                  받아보세요.
                </p>
              </div>
            </div>
          </div>
        </FullPageSection>

        {/* How it Works */}
        <FullPageSection className="bg-white">
          <div>
            <h2 className="text-3xl font-bold text-center mb-16">3단계로 시작하는 AI 코드 리뷰</h2>
            <div className="grid md:grid-cols-3 gap-8 max-w-4xl mx-auto">
              <div className="text-center">
                <div
                  className="w-12 h-12 rounded-full bg-primary-500 text-white 
                  font-bold flex items-center justify-center mx-auto mb-6"
                >
                  1
                </div>
                <h3 className="font-semibold mb-2">GitLab 연동</h3>
                <p className="text-primary-500">프로젝트에 웹훅을 설정하여 coFlo와 연결하세요</p>
              </div>
              <div className="text-center">
                <div
                  className="w-12 h-12 rounded-full bg-primary-500 text-white 
                  font-bold flex items-center justify-center mx-auto mb-6"
                >
                  2
                </div>
                <h3 className="font-semibold mb-2">템플릿 설정</h3>
                <p className="text-primary-500">팀의 코딩 스타일에 맞는 리뷰 템플릿을 설정하세요</p>
              </div>
              <div className="text-center">
                <div
                  className="w-12 h-12 rounded-full bg-primary-500 text-white 
                  font-bold flex items-center justify-center mx-auto mb-6"
                >
                  3
                </div>
                <h3 className="font-semibold mb-2">자동 리뷰</h3>
                <p className="text-primary-500">MR이 생성되면 자동으로 AI 코드 리뷰가 시작됩니다</p>
              </div>
            </div>
          </div>
        </FullPageSection>

        {/* Interactive Demo */}
        <FullPageSection className="bg-gray-50">
          <div className="text-center max-w-4xl mx-auto">
            <h2 className="text-3xl font-bold mb-8">실제 작동 방식 살펴보기</h2>
            <div className="bg-white rounded-lg shadow-xl p-8 relative">
              <div className="relative overflow-hidden max-w-[600px] mx-auto">
                <div
                  className="flex transition-transform duration-300 ease-in-out"
                  style={{ transform: `translateX(-${currentImage * 100}%)` }}
                >
                  {carouselImages.map((image, index) => (
                    <div key={index} className="w-full flex-shrink-0">
                      <img
                        src={image.src}
                        alt={image.alt}
                        className="w-full h-auto object-contain"
                        style={{ aspectRatio: '16/9' }}
                      />
                    </div>
                  ))}
                </div>

                <button
                  className="absolute left-2 top-1/2 -translate-y-1/2 bg-white/80 p-2 rounded-full 
                  shadow-lg hover:bg-white transition-colors disabled:opacity-50"
                  onClick={() => setCurrentImage((prev) => prev - 1)}
                  disabled={currentImage === 0}
                >
                  <ChevronLeft className="w-5 h-5 text-gray-700" />
                </button>

                <button
                  className="absolute right-2 top-1/2 -translate-y-1/2 bg-white/80 p-2 rounded-full 
                  shadow-lg hover:bg-white transition-colors disabled:opacity-50"
                  onClick={() => setCurrentImage((prev) => prev + 1)}
                  disabled={currentImage === carouselImages.length - 1}
                >
                  <ChevronRight className="w-5 h-5 text-gray-700" />
                </button>
              </div>

              <div className="flex justify-center gap-2 mt-4">
                {carouselImages.map((_, index) => (
                  <button
                    key={index}
                    className={`w-2 h-2 rounded-full transition-all 
                    ${currentImage === index ? 'bg-primary-500 w-4' : 'bg-gray-300'}`}
                    onClick={() => setCurrentImage(index)}
                  />
                ))}
              </div>
            </div>
          </div>
        </FullPageSection>

        {/* CTA Section */}
        <FullPageSection className="bg-gradient-to-b from-gray-50 to-white">
          <div className="text-center">
            <h2 className="text-3xl font-bold mb-8">지금 바로 AI 코드 리뷰를 시작하세요</h2>
            <p className="text-xl text-primary-500 mb-8 max-w-2xl mx-auto">
              설치 없이 GitLab 연동만으로 바로 사용할 수 있습니다.
            </p>
            <button
              className="bg-primary-500 text-white px-8 py-4 rounded-full 
              text-lg hover:bg-primary-600 transition-colors inline-flex items-center gap-2"
            >
              시작하기
              <ArrowRight className="w-5 h-5" />
            </button>
          </div>
        </FullPageSection>
      </div>

      {navigationDots}
      {scrollIndicator}
    </div>
  );
};

export default LandingPage;
