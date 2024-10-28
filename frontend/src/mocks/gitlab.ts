import { rest } from 'msw';
import { GitlabProjectListResponse } from 'types/gitLab';
import { UpdateRepositoryRequest } from 'types/api';

export const handlers = [
  rest.get('/api/link/search', (req, res, ctx) => {
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
  rest.post<UpdateRepositoryRequest>('/api/link/:repoId', (req, res, ctx) => {
    const { repoId } = req.params;
    const { token } = req.body;

    if (!token) {
      return res(
        ctx.status(400),
        ctx.json({
          status: 'ERROR',
          message: '토큰이 필요합니다.',
        }),
      );
    }

    return res(
      ctx.status(200),
      ctx.json({
        status: 'SUCCESS',
        message: `리포지토리 ${repoId}의 토큰이 업데이트되었습니다.`,
      }),
    );
  }),

  rest.get('/api/link/status', (req, res, ctx) => {
    const isLinked = Math.random() < 0.5;

    return res(
      ctx.status(200),
      ctx.json({
        status: 'SUCCESS',
        data: { isLinked },
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
        merged_at: null,
        created_at: '2024-10-26T10:00:31.209Z',
        updated_at: '2024-10-26T10:24:28.281Z',
        closed_at: null,
        source_branch: 'etc/S11P31A210-270-git-hook-수정',
        target_branch: 'be/dev',
        labels: ['CI'],
        has_conflicts: false,
        assignee: {
          username: 'ajsthfldu',
          avatar_url:
            'https://secure.gravatar.com/avatar/de5618f1a5aedaa97da4da8aea212a4f10088fef603b68a9ef38c7cc3f569930?s=80&d=identicon',
        },
        reviewer: {
          username: 'anjs134',
          avatar_url:
            'https://secure.gravatar.com/avatar/18d31feb03d8981c6c569b9924031f8be04855d7bf40d32a2d66e9093d49cc09?s=80&d=identicon',
        },
        isAiReviewCreated: false,
      },
      {
        id: 639387,
        iid: 31,
        title: '[feat/#272] gitlab에서 mr diff가져오기 dto 및 헤더 수정',
        state: 'opened',
        merged_at: null,
        created_at: '2024-10-26T09:36:52.252Z',
        updated_at: '2024-10-26T09:36:53.62Z',
        closed_at: null,
        source_branch: 'be/S11P31A210-272-gitlab-mr정보-가져오기-수정',
        target_branch: 'be/dev',
        labels: ['Backend', '🐛 Fix'],
        has_conflicts: false,
        assignee: {
          username: 'ajsthfldu',
          avatar_url:
            'https://secure.gravatar.com/avatar/de5618f1a5aedaa97da4da8aea212a4f10088fef603b68a9ef38c7cc3f569930?s=80&d=identicon',
        },
        reviewer: {
          username: 'anjs134',
          avatar_url:
            'https://secure.gravatar.com/avatar/18d31feb03d8981c6c569b9924031f8be04855d7bf40d32a2d66e9093d49cc09?s=80&d=identicon',
        },
        isAiReviewCreated: false,
      },
      {
        id: 639386,
        iid: 30,
        title: '[feat/#255] 커스텀 프롬프트 수정 및 삭제',
        state: 'opened',
        merged_at: null,
        created_at: '2024-10-26T07:44:37.935Z',
        updated_at: '2024-10-26T09:23:10.799Z',
        closed_at: null,
        source_branch: 'be/S11P31A210-225-커스텀-프롬프트-수정-및-삭제',
        target_branch: 'be/dev',
        labels: ['✨ Feature', '🐛 Fix'],
        has_conflicts: false,
        assignee: {
          username: 'fkgnssla',
          avatar_url: 'https://lab.ssafy.com/uploads/-/system/user/avatar/17530/avatar.png',
        },
        reviewer: {
          username: 'anjs134',
          avatar_url:
            'https://secure.gravatar.com/avatar/18d31feb03d8981c6c569b9924031f8be04855d7bf40d32a2d66e9093d49cc09?s=80&d=identicon',
        },
        isAiReviewCreated: false,
      },
      {
        id: 639371,
        iid: 29,
        title: '[feat/#261] 로그인 UI 수정 및 페이지네이션 구현',
        state: 'opened',
        merged_at: null,
        created_at: '2024-10-25T09:49:12.275Z',
        updated_at: '2024-10-25T09:49:13.636Z',
        closed_at: null,
        source_branch: 'fe/S11P31A210-261-ui-나의-레포지토리-목록',
        target_branch: 'fe/dev',
        labels: [],
        has_conflicts: false,
        assignee: {
          username: 'anjs134',
          avatar_url:
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
        merged_at: null,
        created_at: '2024-10-25T08:51:54.913Z',
        updated_at: '2024-10-26T10:26:34.518Z',
        closed_at: null,
        source_branch: 'be/S11P31A210-220-link-project',
        target_branch: 'be/dev',
        labels: ['Backend', '♻️ Refactor', '✨ Feature', '📬 API'],
        has_conflicts: false,
        assignee: {
          username: 'jimmi219',
          avatar_url: 'https://lab.ssafy.com/uploads/-/system/user/avatar/17537/avatar.png',
        },
        reviewer: null,
        isAiReviewCreated: false,
      },
    ];

    const responseData = {
      gitlabMrList,
      totalElements: gitlabMrList.length,
      totalPages: 1,
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
];
