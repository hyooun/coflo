import { rest } from 'msw';
import { GitlabProjectListResponse } from 'types/gitLab';
import { UpdateRepositoryRequest } from 'types/api';

export const handlers = [
  rest.get('/api/gitlab/search', (req, res, ctx) => {
    const keyword = req.url.searchParams.get('keyword');
    const page = req.url.searchParams.get('page');
    const size = req.url.searchParams.get('size');

    if (!keyword || !page || !size) {
      return res(
        ctx.status(400),
        ctx.json({
          status: 'ERROR',
          httpStatus: 'BAD_REQUEST',
          code: 'ERR001',
          message: 'Missing required query parameters',
        }),
      );
    }

    const pageNumber = parseInt(page, 10);
    const pageSize = parseInt(size, 10);

    // 예시 데이터 생성 (101개의 프로젝트)
    const gitlabProjectList = Array.from({ length: 101 }, (v, i) => ({
      gitlabProjectId: 10 + i,
      name: `Project ${String.fromCharCode(65 + (i % 26))}${Math.floor(i / 26)}`,
      isLinked: false,
      isLinkable: i % 2 === 0,
    }));

    // 페이지네이션에 맞는 데이터 잘라내기
    const start = (pageNumber - 1) * pageSize;
    const end = start + pageSize;
    const paginatedData = gitlabProjectList.slice(start, end);

    const responseData: GitlabProjectListResponse = {
      gitlabProjectList: paginatedData,
      totalPages: Math.ceil(gitlabProjectList.length / pageSize),
      totalElements: gitlabProjectList.length,
      isLast: pageNumber * pageSize >= gitlabProjectList.length,
      currPage: pageNumber,
    };

    return res(
      ctx.status(200),
      ctx.json({
        status: 'SUCCESS',
        data: responseData,
      }),
    );
  }),
  rest.post<UpdateRepositoryRequest>('/api/user-project/:repoId', (req, res, ctx) => {
    const { repoId } = req.params;
    const { botToken } = req.body;

    // `botToken`이 존재할 때만 검증
    if (botToken === undefined) {
      // `botToken` 없이도 성공 응답 반환
      return res(
        ctx.status(200),
        ctx.json({
          status: 'SUCCESS',
          message: `리포지토리 ${repoId}가 업데이트되었습니다.`,
        }),
      );
    }

    // `botToken`이 있을 때는 정상적으로 처리
    return res(
      ctx.status(200),
      ctx.json({
        status: 'SUCCESS',
        message: `리포지토리 ${repoId}의 토큰이 업데이트되었습니다.`,
      }),
    );
  }),
  rest.get('/api/user-project/status', (req, res, ctx) => {
    const isLinked = Math.random() < 0.5;

    return res(
      ctx.status(200),
      ctx.json({
        status: 'SUCCESS',
        data: { isLinked },
      }),
    );
  }),

  rest.delete('/api/user-project/:repoId', (req, res, ctx) => {
    const { repoId } = req.params;

    return res(
      ctx.status(200),
      ctx.json({
        status: 'SUCCESS',
        message: `리포지토리 ${repoId}가 삭제되었습니다.`,
      }),
    );
  }),

  rest.get('/api/merge-requests', (req, res, ctx) => {
    const gitlabMrList = [
      {
        id: 639389,
        iid: 32,
        title: '[chore/#270] git hook 수정',
        state: 'opened',
        mergedAt: null,
        createdAt: '2024-10-26T10:00:31.209Z',
        updatedAt: '2024-10-26T10:24:28.281Z',
        closedAt: null,
        sourceBranch: 'etc/S11P31A210-270-git-hook-수정',
        targetBranch: 'be/dev',
        labels: ['CI'],
        has_conflicts: false,
        assignee: {
          username: 'ajsthfldu',
          avatarUrl:
            'https://secure.gravatar.com/avatar/de5618f1a5aedaa97da4da8aea212a4f10088fef603b68a9ef38c7cc3f569930?s=80&d=identicon',
        },
        reviewer: {
          username: 'anjs134',
          avatarUrl:
            'https://secure.gravatar.com/avatar/18d31feb03d8981c6c569b9924031f8be04855d7bf40d32a2d66e9093d49cc09?s=80&d=identicon',
        },
        isAiReviewCreated: false,
      },
      {
        id: 639387,
        iid: 31,
        title: '[feat/#272] gitlab에서 mr diff가져오기 dto 및 헤더 수정',
        state: 'opened',
        mergedAt: null,
        createdAt: '2024-10-26T09:36:52.252Z',
        updatedAt: '2024-10-26T09:36:53.62Z',
        closedAt: null,
        sourceBranch: 'be/S11P31A210-272-gitlab-mr정보-가져오기-수정',
        targetBranch: 'be/dev',
        labels: ['Backend', '🐛 Fix'],
        has_conflicts: false,
        assignee: {
          username: 'ajsthfldu',
          avatarUrl:
            'https://secure.gravatar.com/avatar/de5618f1a5aedaa97da4da8aea212a4f10088fef603b68a9ef38c7cc3f569930?s=80&d=identicon',
        },
        reviewer: {
          username: 'anjs134',
          avatarUrl:
            'https://secure.gravatar.com/avatar/18d31feb03d8981c6c569b9924031f8be04855d7bf40d32a2d66e9093d49cc09?s=80&d=identicon',
        },
        isAiReviewCreated: false,
      },
      {
        id: 639386,
        iid: 30,
        title: '[feat/#255] 커스텀 프롬프트 수정 및 삭제',
        state: 'opened',
        mergedAt: null,
        createdAt: '2024-10-26T07:44:37.935Z',
        updatedAt: '2024-10-26T09:23:10.799Z',
        closedAt: null,
        sourceBranch: 'be/S11P31A210-225-커스텀-프롬프트-수정-및-삭제',
        targetBranch: 'be/dev',
        labels: ['✨ Feature', '🐛 Fix'],
        has_conflicts: false,
        assignee: {
          username: 'fkgnssla',
          avatarUrl: 'https://lab.ssafy.com/uploads/-/system/user/avatar/17530/avatar.png',
        },
        reviewer: {
          username: 'anjs134',
          avatarUrl:
            'https://secure.gravatar.com/avatar/18d31feb03d8981c6c569b9924031f8be04855d7bf40d32a2d66e9093d49cc09?s=80&d=identicon',
        },
        isAiReviewCreated: false,
      },
      {
        id: 639386,
        iid: 30,
        title: '[feat/#255] 커스텀 프롬프트 수정 및 삭제',
        state: 'opened',
        mergedAt: null,
        createdAt: '2024-10-26T07:44:37.935Z',
        updatedAt: '2024-10-26T09:23:10.799Z',
        closedAt: null,
        sourceBranch: 'be/S11P31A210-225-커스텀-프롬프트-수정-및-삭제',
        targetBranch: 'be/dev',
        labels: ['✨ Feature', '🐛 Fix'],
        has_conflicts: false,
        assignee: {
          username: 'fkgnssla',
          avatarUrl: 'https://lab.ssafy.com/uploads/-/system/user/avatar/17530/avatar.png',
        },
        reviewer: {
          username: 'anjs134',
          avatarUrl:
            'https://secure.gravatar.com/avatar/18d31feb03d8981c6c569b9924031f8be04855d7bf40d32a2d66e9093d49cc09?s=80&d=identicon',
        },
        isAiReviewCreated: false,
      },
      {
        id: 639386,
        iid: 30,
        title: '[feat/#255] 커스텀 프롬프트 수정 및 삭제',
        state: 'opened',
        mergedAt: null,
        createdAt: '2024-10-26T07:44:37.935Z',
        updatedAt: '2024-10-26T09:23:10.799Z',
        closedAt: null,
        sourceBranch: 'be/S11P31A210-225-커스텀-프롬프트-수정-및-삭제',
        targetBranch: 'be/dev',
        labels: ['✨ Feature', '🐛 Fix'],
        has_conflicts: false,
        assignee: {
          username: 'fkgnssla',
          avatarUrl: 'https://lab.ssafy.com/uploads/-/system/user/avatar/17530/avatar.png',
        },
        reviewer: {
          username: 'anjs134',
          avatarUrl:
            'https://secure.gravatar.com/avatar/18d31feb03d8981c6c569b9924031f8be04855d7bf40d32a2d66e9093d49cc09?s=80&d=identicon',
        },
        isAiReviewCreated: false,
      },
      {
        id: 639386,
        iid: 30,
        title: '[feat/#255] 커스텀 프롬프트 수정 및 삭제',
        state: 'opened',
        mergedAt: null,
        createdAt: '2024-10-26T07:44:37.935Z',
        updatedAt: '2024-10-26T09:23:10.799Z',
        closedAt: null,
        sourceBranch: 'be/S11P31A210-225-커스텀-프롬프트-수정-및-삭제',
        targetBranch: 'be/dev',
        labels: ['✨ Feature', '🐛 Fix'],
        has_conflicts: false,
        assignee: {
          username: 'fkgnssla',
          avatarUrl: 'https://lab.ssafy.com/uploads/-/system/user/avatar/17530/avatar.png',
        },
        reviewer: {
          username: 'anjs134',
          avatarUrl:
            'https://secure.gravatar.com/avatar/18d31feb03d8981c6c569b9924031f8be04855d7bf40d32a2d66e9093d49cc09?s=80&d=identicon',
        },
        isAiReviewCreated: false,
      },
      {
        id: 639371,
        iid: 29,
        title: '[feat/#261] 로그인 UI 수정 및 페이지네이션 구현',
        state: 'opened',
        mergedAt: null,
        createdAt: '2024-10-25T09:49:12.275Z',
        updatedAt: '2024-10-25T09:49:13.636Z',
        closedAt: null,
        sourceBranch: 'fe/S11P31A210-261-ui-나의-레포지토리-목록',
        targetBranch: 'fe/dev',
        labels: [],
        has_conflicts: false,
        assignee: {
          username: 'anjs134',
          avatarUrl:
            'https://secure.gravatar.com/avatar/18d31feb03d8981c6c569b9924031f8be04855d7bf40d32a2d66e9093d49cc09?s=80&d=identicon',
        },
        reviewer: null,
        isAiReviewCreated: false,
      },
      {
        id: 639357,
        iid: 27,
        title: '[feat/#220] 프로젝트 연동 API 구현',
        state: 'opened',
        mergedAt: null,
        createdAt: '2024-10-25T08:51:54.913Z',
        updatedAt: '2024-10-26T10:26:34.518Z',
        closedAt: null,
        sourceBranch: 'be/S11P31A210-220-link-project',
        targetBranch: 'be/dev',
        labels: ['Backend', '♻️ Refactor', '✨ Feature', '📬 API'],
        has_conflicts: false,
        assignee: {
          username: 'jimmi219',
          avatarUrl: 'https://lab.ssafy.com/uploads/-/system/user/avatar/17537/avatar.png',
        },
        reviewer: null,
        isAiReviewCreated: false,
      },
    ];

    const responseData = {
      gitlabMrList,
      totalElements: gitlabMrList.length,
      totalPages: 2,
      isLast: true,
      currPage: 1,
    };

    return res(
      ctx.status(200),
      ctx.json({
        status: 'SUCCESS',
        data: responseData,
      }),
    );
  }),
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
