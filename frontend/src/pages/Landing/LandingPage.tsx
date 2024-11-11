import { GitMerge, Zap, Search, GitBranch, ArrowRight, CheckCircle2 } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const ReviewDemo = () => (
  <div className="bg-white rounded-lg shadow-xl p-6 max-w-2xl mx-auto">
    <div className="flex items-center gap-2 mb-4 text-secondary border-b pb-4">
      <GitMerge className="w-5 h-5" />
      <span className="font-medium">user-auth-feature</span>
      <span className="text-gray-400">•</span>
      <span className="text-gray-400 text-sm">2 minutes ago</span>
    </div>
    <div className="space-y-6">
      <div className="flex gap-4">
        <div className="flex-shrink-0">
          <div className="w-8 h-8 rounded-full bg-primary-500/10 flex items-center justify-center">
            <CheckCircle2 className="w-5 h-5 text-primary-500" />
          </div>
        </div>
        <div>
          <div className="font-medium mb-2">Security Enhancement</div>
          <div className="text-secondary text-sm bg-gray-50 p-4 rounded-lg font-mono">
            Consider using bcrypt instead of SHA-256 for password hashing:
            <br />
            <span className="text-red-500">- const hash = SHA256(password);</span>
            <br />
            <span className="text-green-500">+ const hash = await bcrypt.hash(password, 10);</span>
          </div>
        </div>
      </div>
      <div className="flex gap-4">
        <div className="flex-shrink-0">
          <div className="w-8 h-8 rounded-full bg-blue-500/10 flex items-center justify-center">
            <Zap className="w-5 h-5 text-blue-500" />
          </div>
        </div>
        <div>
          <div className="font-medium mb-2">Performance Optimization</div>
          <div className="text-secondary text-sm bg-gray-50 p-4 rounded-lg">
            Adding an index on the `email` field will improve query performance by approximately 75%
            based on your current data volume.
          </div>
        </div>
      </div>
    </div>
  </div>
);

const LandingPage = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-white">
      {/* Navigation */}
      <nav className="border-b">
        <div className="container mx-auto px-6 py-4">
          <div className="flex justify-between items-center">
            <div className="flex items-center gap-2">
              <GitMerge className="w-8 h-8 text-primary-500" />
              <span className="text-xl font-bold">coFlo</span>
            </div>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="py-20">
        <div className="container mx-auto px-6">
          <div className="text-center max-w-3xl mx-auto mb-16">
            <h1 className="text-4xl md:text-5xl font-bold mb-6">
              더 나은 코드를 위한 AI 코드 리뷰
            </h1>
            <p className="text-xl text-secondary">
              프로젝트의 맥락을 이해하고 팀의 코딩 스타일을 반영하는
            </p>
            <p className="text-xl text-secondary mb-8">
              맞춤형 코드 리뷰로 개발 생산성을 높이세요.
            </p>
            <button
              className="bg-primary-500 text-white px-8 py-4 rounded-full text-lg 
              hover:bg-primary-600 transition-colors inline-flex items-center gap-2"
              onClick={() => navigate('/login')}
            >
              시작하기
              <ArrowRight className="w-5 h-5" />
            </button>
          </div>
          <ReviewDemo />
        </div>
      </section>

      {/* Features Section */}
      <section className="py-20 bg-gray-50">
        <div className="container mx-auto px-6">
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-12">
            <div>
              <div
                className="w-12 h-12 rounded-full bg-primary-500/10 flex items-center 
                justify-center mb-6"
              >
                <GitBranch className="w-6 h-6 text-primary-500" />
              </div>
              <h3 className="text-xl font-semibold mb-4">Git 자동 연동</h3>
              <p className="text-secondary">
                웹훅 설정만으로 Merge Request가 생성될 때마다 자동으로 리뷰가 시작됩니다.
              </p>
            </div>
            <div>
              <div
                className="w-12 h-12 rounded-full bg-primary-500/10 flex items-center 
                justify-center mb-6"
              >
                <Search className="w-6 h-6 text-primary-500" />
              </div>
              <h3 className="text-xl font-semibold mb-4">컨텍스트 기반 분석</h3>
              <p className="text-secondary">
                이슈와 MR 히스토리를 분석하여 프로젝트의 맥락을 이해하고 더 정확한 리뷰를
                제공합니다.
              </p>
            </div>
            <div>
              <div
                className="w-12 h-12 rounded-full bg-primary-500/10 flex items-center 
                justify-center mb-6"
              >
                <Zap className="w-6 h-6 text-primary-500" />
              </div>
              <h3 className="text-xl font-semibold mb-4">맞춤형 리뷰</h3>
              <p className="text-secondary">
                팀의 코딩 스타일과 컨벤션을 반영한 커스텀 템플릿으로 일관된 품질의 리뷰를
                받아보세요.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* How it Works */}
      <section className="py-20">
        <div className="container mx-auto px-6">
          <h2 className="text-3xl font-bold text-center mb-16">3단계로 시작하는 AI 코드 리뷰</h2>
          <div className="grid md:grid-cols-3 gap-8 max-w-4xl mx-auto">
            <div className="text-center">
              <div
                className="w-12 h-12 rounded-full bg-primary-500 text-white font-bold 
                flex items-center justify-center mx-auto mb-6"
              >
                1
              </div>
              <h3 className="font-semibold mb-2">Git 연동</h3>
              <p className="text-secondary">프로젝트에 웹훅을 설정하여 coFlo와 연결하세요</p>
            </div>
            <div className="text-center">
              <div
                className="w-12 h-12 rounded-full bg-primary-500 text-white font-bold 
                flex items-center justify-center mx-auto mb-6"
              >
                2
              </div>
              <h3 className="font-semibold mb-2">템플릿 설정</h3>
              <p className="text-secondary">팀의 코딩 스타일에 맞는 리뷰 템플릿을 설정하세요</p>
            </div>
            <div className="text-center">
              <div
                className="w-12 h-12 rounded-full bg-primary-500 text-white font-bold 
                flex items-center justify-center mx-auto mb-6"
              >
                3
              </div>
              <h3 className="font-semibold mb-2">자동 리뷰</h3>
              <p className="text-secondary">MR이 생성되면 자동으로 AI 코드 리뷰가 시작됩니다</p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="bg-gray-50 py-20">
        <div className="container mx-auto px-6 text-center">
          <h2 className="text-3xl font-bold mb-8">지금 바로 AI 코드 리뷰를 시작하세요</h2>
          <p className="text-xl text-secondary mb-8 max-w-2xl mx-auto">
            설치 없이 GitLab 연동만으로 바로 사용할 수 있습니다.
          </p>
          <button
            className="bg-primary-500 text-white px-8 py-4 rounded-full text-lg 
            hover:bg-primary-600 transition-colors inline-flex items-center gap-2"
            onClick={() => navigate('/login')}
          >
            시작하기
            <ArrowRight className="w-5 h-5" />
          </button>
        </div>
      </section>
    </div>
  );
};

export default LandingPage;
