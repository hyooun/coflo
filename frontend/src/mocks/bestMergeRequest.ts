// src/mocks/handlers.ts
import { rest } from 'msw';

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
];
