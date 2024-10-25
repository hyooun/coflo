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
  rest.post<UpdateRepositoryRequest>('/api/link/repository/:repoId/update', (req, res, ctx) => {
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
];
