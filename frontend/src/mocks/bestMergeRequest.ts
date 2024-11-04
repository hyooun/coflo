// src/mocks/handlers.ts
import { rest } from 'msw';
import { BadgeType } from 'types/badge';
import { GitlabMergeRequest } from 'types/mergeRequest';
import { MergeRequestReview } from 'types/review';

interface BadgeResponse {
  status: string;
  data: {
    mainBadgeCodeId: number;
    badgeDetails: BadgeType[];
  };
}

export const handlers = [
  rest.get('/api/best-merge-requests', (req, res, ctx) => {
    return res(
      ctx.json([
        {
          id: 1,
          branchName: 'feature/user',
          title: 'Feat: 회원가입 컴포넌트 구현',
          assignee: '/images/mocks/profile1.png',
          reviewer: '/images/mocks/profile2.png',
          createdAt: '1 week ago',
          labels: ['feat', 'style'],
          author: '이보연',
        },
        {
          id: 2,
          branchName: 'dev',
          title: 'Fix: 입력폼 수정',
          assignee: '/images/mocks/profile1.png',
          reviewer: '/images/mocks/profile2.png',
          createdAt: '1 week ago',
          labels: ['fix', 'error'],
          author: '구승석',
        },
        {
          id: 3,
          branchName: 'dev',
          title: 'Fix: 입력폼 수정',
          assignee: '/images/mocks/profile1.png',
          reviewer: '/images/mocks/profile2.png',
          createdAt: '1 week ago',
          labels: ['fix', 'error'],
          author: '구승석',
        },
      ]),
    );
  }),
  rest.get('/api/reviews/:id', (req, res, ctx) => {
    const { id } = req.params;

    const mockMergeRequest: GitlabMergeRequest = {
      id: Number(id),
      iid: 247,
      title: '[feat/#247] 앨범 상세 보기 API 연동',
      description: 'API 연동을 통해 앨범 상세 정보를 가져올 수 있도록 구현',
      state: 'merged',
      mergedAt: '2023-10-01T12:34:56Z',
      createdAt: '2023-09-15T08:00:00Z',
      updatedAt: '2023-10-01T12:34:56Z',
      closedAt: null,
      sourceBranch: 'feature/album-detail',
      targetBranch: 'dev',
      labels: ['feature', 'api'],
      hasConflicts: false,
      assignee: {
        id: 1,
        username: 'hatchu',
        name: '하츄핑',
        avatarUrl: '/images/mocks/profile1.png',
      },
      reviewer: {
        id: 2,
        username: 'chana',
        name: '차나핑',
        avatarUrl: '/images/mocks/profile2.png',
      },
      isAiReviewCreated: false,
    };

    const mockReviewData: MergeRequestReview = {
      ...mockMergeRequest,
      reviews: [
        {
          id: 1,
          reviewer: {
            id: 2,
            username: 'chana',
            name: '차나핑',
            avatarUrl: '/images/mocks/profile2.png',
          },
          createdAt: '2023-10-01T12:34:56Z',
          updatedAt: '2023-10-01T12:34:56Z',
          content:
            '## 코드 리뷰 요약\n- 상태 관리와 조건부 렌더링이 잘 되어 있습니다.\n- Tailwind CSS를 효과적으로 사용하였습니다.',
          comments: [
            {
              id: 1,
              reviewer: {
                id: 2,
                username: 'chana',
                name: '차나핑',
                avatarUrl: '/images/mocks/profile2.png',
              },
              createdAt: '2023-10-01T12:35:00Z',
              updatedAt: '2023-10-01T12:35:00Z',
              content:
                '### 개선사항\n- `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다.`useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다. `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다. `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다. `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다.  \n### 개선사항\n\n - `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다.### 개선사항\n\n- `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다.### 개선사항\n\n- `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다.### 개선사항\n\n- `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다.### 개선사항\n\n- `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다.### 개선사항\n\n- `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다.### 개선사항\n\n- `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다.### 개선사항\n\n- `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다.### 개선사항\n\n- `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다.### 개선사항\n\n- `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다.### 개선사항\n\n- `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다.### 개선사항\n\n- `useState`를 `useReducer`로 변경하면 가독성이 높아질 수 있습니다.',
              resolved: false,
              resolvable: true,
              replies: [
                {
                  id: 1,
                  author: {
                    id: 1,
                    username: 'hatchu',
                    name: '하츄핑',
                    avatarUrl: '/images/mocks/profile1.png',
                  },
                  content: '네, 수정하겠습니다. 좋은 의견 감사합니다!',
                  createdAt: '2023-10-01T12:36:00Z',
                  updatedAt: '2023-10-01T12:36:00Z',
                },
              ],
            },
          ],
        },
        {
          id: 2,
          reviewer: {
            id: 3,
            username: 'hachu',
            name: '하츄핑',
            avatarUrl: '/images/mocks/profile1.png',
          },
          createdAt: '2023-10-02T10:00:00Z',
          updatedAt: '2023-10-02T10:00:00Z',
          content:
            '## 코드 스타일\n\n- 코드 스타일이 일관적입니다.\n- 주석이 잘 작성되어 있습니다.',
          comments: [
            {
              id: 2,
              reviewer: {
                id: 2,
                username: 'chana',
                name: '차나핑',
                avatarUrl: '/images/mocks/profile2.png',
              },
              createdAt: '2023-10-02T10:05:00Z',
              updatedAt: '2023-10-02T10:05:00Z',
              content: '### 추가 제안\n\n- 함수 이름을 더 명확하게 변경하는 것이 좋겠습니다.',
              resolved: false,
              resolvable: false,
              replies: [],
            },
          ],
        },
      ],
    };

    return res(ctx.status(200), ctx.json(mockReviewData));
  }),
  rest.get('/api/reviews/:reviewId/retrivals', (req, res, ctx) => {
    const mockReferences = [
      {
        id: 1,
        fileName: 'src/components/Album/Detail.java',
        type: 'CODE',
        language: 'java',
        content: 'Some code content here...',
      },
      {
        id: 2,
        fileName: 'docs/specifications.md',
        type: 'TEXT',
        language: 'plaintext',
        content: 'Some text content here...',
      },
      {
        id: 3,
        fileName: 'docs/example.md',
        type: 'TEXT',
        language: 'plaintext',
        content:
          'Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...Some text content here...',
      },
      {
        id: 4,
        fileName: 'src/components/Album/Detail.tsx',
        type: 'CODE',
        language: 'typescript',
        content: `export default function AlbumDetail() {
      const [albumData, setAlbumData] = useState<AlbumType | null>(null);
      const [isLoading, setIsLoading] = useState(true);
      const { id } = useParams();
    
      useEffect(() => {
        async function fetchAlbumData() {
          try {
            const response = await axios.get(\`/api/albums/\${id}\`);
            setAlbumData(response.data);
          } catch (error) {
            console.error('Failed to fetch album data:', error);
          } finally {
            setIsLoading(false);
          }
        }
    
        fetchAlbumData();
      }, [id]);
    
      if (isLoading) {
        return <LoadingSpinner />;
      }
    
      if (!albumData) {
        return <div>앨범을 찾을 수 없습니다.</div>;
      }
    
      return (
        <div className="container mx-auto px-4 py-8">
          <div className="flex flex-col md:flex-row gap-8">
            <div className="w-full md:w-1/3">
              <img
                src={albumData.coverImage}
                alt={albumData.title}
                className="w-full rounded-lg shadow-lg"
              />
            </div>
            <div className="w-full md:w-2/3">
              <h1 className="text-3xl font-bold mb-4">{albumData.title}</h1>
              <p className="text-gray-600 mb-6">{albumData.description}</p>
              {/* Additional album details */}
            </div>
          </div>
        </div>
      );
    }`,
      },
    ];

    return res(ctx.status(200), ctx.json(mockReferences));
  }),

  rest.get('/api/badges', (req, res, ctx) => {
    const mockBadgeResponse: BadgeResponse = {
      status: 'SUCCESS',
      data: {
        mainBadgeCodeId: 2,
        badgeDetails: [
          {
            badgeCodeId: 1,
            name: '첫 모험가',
            description: '처음 서비스 가입 시 기본 획득',
            imageUrl: '/images/mocks/badges/badge_00.png',
            isAcquired: true,
          },
          {
            badgeCodeId: 2,
            name: '리뷰 탐색자',
            description: '첫 AI리뷰 재생성',
            imageUrl: '/images/mocks/badges/badge_01.png',
            isAcquired: false,
          },
          {
            badgeCodeId: 3,
            name: '코드 마스터',
            description: '10개 이상의 MR 리뷰 완료',
            imageUrl: '/images/mocks/badges/badge_02.png',
            isAcquired: false,
          },
          {
            badgeCodeId: 4,
            name: '프로젝트 마스터',
            description: '10개 이상의 MR 리뷰 완료',
            imageUrl: '/images/mocks/badges/badge_03.png',
            isAcquired: false,
          },
          {
            badgeCodeId: 5,
            name: '행운의 발견',
            description: '접속 시 1% 확률로 획득',
            imageUrl: '/images/mocks/badges/badge_04.png',
            isAcquired: true,
          },
          // ... 더 많은 뱃지들 추가 가능
        ],
      },
    };

    return res(ctx.status(200), ctx.json(mockBadgeResponse));
  }),
  rest.delete('/api/badges', (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        status: 'SUCCESS',
        message: '대표 뱃지가 해제되었습니다.',
      }),
    );
  }),

  rest.patch('/api/badges', async (req: any, res, ctx) => {
    const { badgeId } = await req.body;

    return res(
      ctx.status(200),
      ctx.json({
        status: 'SUCCESS',
        message: '대표 뱃지가 설정되었습니다.',
        data: {
          badgeId,
        },
      }),
    );
  }),
];
