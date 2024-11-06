import { rest } from 'msw';
import { GitlabProjectListResponse } from 'types/gitLab';
import { UpdateRepositoryRequest } from 'types/api';
import badge1 from '@assets/images/badges/badge_none.png';
import { MergeRequestReview } from 'types/review';
import { GitlabMergeRequest } from 'types/mergeRequest';
import { Reference } from 'types/reference';

export const handlers = [
  rest.get('/api/gitlab/search', (req, res, ctx) => {
    const keyword = req.url.searchParams.get('keyword');
    const page = req.url.searchParams.get('page');
    const size = req.url.searchParams.get('size');

    if (keyword === undefined && page === undefined && size === undefined) {
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

  rest.get('/api/projects/:projectId', (req, res, ctx) => {
    const { projectId } = req.params;

    const projectInfo = {
      status: 'SUCCESS',
      data: {
        commitCount: 6,
        branchCount: 9,
        mergeRequestCount: 54,
        languages: [
          {
            language: 'shell',
            percentage: 66.37,
            color: '#89e051',
          },
          {
            language: 'Java',
            percentage: 33.74,
            color: '#b07219',
          },
        ],
        aiReviewCount: 0,
      },
      message: `프로젝트 ${projectId}에 대한 정보입니다.`,
    };

    console.log('Response data:', projectInfo);

    return res(ctx.status(200), ctx.json(projectInfo));
  }),
  rest.get('/api/projects/:projectId/scores', (req, res, ctx) => {
    const { projectId } = req.params;

    const scoresResponse = {
      status: 'SUCCESS',
      data: {
        startDate: '2024-10-21',
        endDate: '2024-10-27',
        userScores: [
          {
            userId: 1,
            username: '지민',
            profileImageUrl:
              'https://secure.gravatar.com/avatar/de5618f1a5aedaa97da4da8aea212a4f10088fef603b68a9ef38c7cc3f569930?s=80&d=identicon',
            badgeName: '첫 모험가',
            badgeImageUrl: badge1,
            scores: [
              { name: '가독성', score: 7 },
              { name: '일관성', score: 8 },
              { name: '재사용성', score: 6 },
              { name: '신뢰성', score: 9 },
              { name: '보안성', score: 7 },
              { name: '유지보수성', score: 8 },
            ],
          },
          {
            userId: 2,
            username: '보연',
            profileImageUrl:
              'https://secure.gravatar.com/avatar/de5618f1a5aedaa97da4da8aea212a4f10088fef603b68a9ef38c7cc3f569930?s=80&d=identicon',
            badgeName: '프로젝트 개척자',
            badgeImageUrl: badge1,
            scores: [
              { name: '가독성', score: 7 },
              { name: '일관성', score: 8 },
              { name: '재사용성', score: 6 },
              { name: '신뢰성', score: 9 },
              { name: '보안성', score: 7 },
              { name: '유지보수성', score: 8 },
            ],
          },
          {
            userId: 3,
            username: '선기',
            profileImageUrl:
              'https://secure.gravatar.com/avatar/de5618f1a5aedaa97da4da8aea212a4f10088fef603b68a9ef38c7cc3f569930?s=80&d=identicon',
            badgeName: '헌신의 발자국',
            badgeImageUrl: null,
            scores: [
              { name: '가독성', score: 8 },
              { name: '일관성', score: 7 },
              { name: '재사용성', score: 6 },
              { name: '신뢰성', score: 8 },
              { name: '보안성', score: 7 },
              { name: '유지보수성', score: 8 },
            ],
          },
          {
            userId: 4,
            username: '형민',
            profileImageUrl:
              'https://secure.gravatar.com/avatar/de5618f1a5aedaa97da4da8aea212a4f10088fef603b68a9ef38c7cc3f569930?s=80&d=identicon',
            badgeName: null,
            badgeImageUrl: badge1,
            scores: [
              { name: '가독성', score: 8 },
              { name: '일관성', score: 9 },
              { name: '재사용성', score: 8 },
              { name: '신뢰성', score: 7 },
              { name: '보안성', score: 9 },
              { name: '유지보수성', score: 8 },
            ],
          },
          // {
          //   userId: 5,
          //   username: '영수',
          //   profileImageUrl:
          //     'https://secure.gravatar.com/avatar/de5618f1a5aedaa97da4da8aea212a4f10088fef603b68a9ef38c7cc3f569930?s=80&d=identicon',
          //   badgeName: '팀 리더',
          //   badgeImageUrl: badge1,
          //   scores: [
          //     { name: '가독성', score: 9 },
          //     { name: '일관성', score: 9 },
          //     { name: '재사용성', score: 8 },
          //     { name: '신뢰성', score: 9 },
          //     { name: '보안성', score: 8 },
          //     { name: '유지보수성', score: 9 },
          //   ],
          // },
        ],
      },
    };

    console.log('Response data:', scoresResponse);

    return res(ctx.status(200), ctx.json(scoresResponse));
  }),

  rest.get('/api/projects/:projectId/statistics', (req, res, ctx) => {
    const { projectId } = req.params;
    const calculationType = req.url.searchParams.get('calculationType');
    const scoreDisplayType = req.url.searchParams.get('scoreDisplayType');

    console.log(`Request URL: ${req.url}`);
    console.log(
      `Project ID: ${projectId}, Calculation Type: ${calculationType}, Score Display Type: ${scoreDisplayType}`,
    );

    // 데이터 준비
    let responseData;

    if (calculationType === 'acquisition' && scoreDisplayType === 'total') {
      responseData = {
        status: 'SUCCESS',
        data: {
          startDate: '2024-10-14',
          endDate: '2024-10-27',
          scoreOfWeek: [
            { week: 1, score: 48 },
            { week: 2, score: 45 },
          ],
        },
      };
      console.log(responseData);
    } else if (calculationType === 'acquisition' && scoreDisplayType === 'individual') {
      responseData = {
        status: 'SUCCESS',
        data: {
          startDate: '2024-10-14',
          endDate: '2024-10-27',
          codeQualityScores: [
            {
              codeQualityName: '가독성',
              scoreOfWeek: [
                { week: 1, score: 8 },
                { week: 2, score: 7 },
              ],
            },
            {
              codeQualityName: '일관성',
              scoreOfWeek: [
                { week: 1, score: 7 },
                { week: 2, score: 8 },
              ],
            },
            {
              codeQualityName: '재사용성',
              scoreOfWeek: [
                { week: 1, score: 9 },
                { week: 2, score: 6 },
              ],
            },
            {
              codeQualityName: '신뢰성',
              scoreOfWeek: [
                { week: 1, score: 6 },
                { week: 2, score: 9 },
              ],
            },
            {
              codeQualityName: '보안성',
              scoreOfWeek: [
                { week: 1, score: 10 },
                { week: 2, score: 7 },
              ],
            },
            {
              codeQualityName: '유지보수성',
              scoreOfWeek: [
                { week: 1, score: 8 },
                { week: 2, score: 8 },
              ],
            },
          ],
        },
      };
    } else if (calculationType === 'cumulative' && scoreDisplayType === 'total') {
      responseData = {
        status: 'SUCCESS',
        data: {
          startDate: '2024-10-14',
          endDate: '2024-10-27',
          scoreOfWeek: [
            { week: 1, score: 48 },
            { week: 2, score: 93 },
          ],
        },
      };
    } else if (calculationType === 'cumulative' && scoreDisplayType === 'individual') {
      responseData = {
        status: 'SUCCESS',
        data: {
          startDate: '2024-10-14',
          endDate: '2024-10-27',
          codeQualityScores: [
            {
              codeQualityName: '유지보수성',
              scoreOfWeek: [
                { week: 1, score: 8 },
                { week: 2, score: 16 },
              ],
            },
            {
              codeQualityName: '재사용성',
              scoreOfWeek: [
                { week: 1, score: 9 },
                { week: 2, score: 15 },
              ],
            },
            {
              codeQualityName: '보안성',
              scoreOfWeek: [
                { week: 1, score: 10 },
                { week: 2, score: 17 },
              ],
            },
            {
              codeQualityName: '가독성',
              scoreOfWeek: [
                { week: 1, score: 8 },
                { week: 2, score: 15 },
              ],
            },
            {
              codeQualityName: '신뢰성',
              scoreOfWeek: [
                { week: 1, score: 6 },
                { week: 2, score: 15 },
              ],
            },
            {
              codeQualityName: '일관성',
              scoreOfWeek: [
                { week: 1, score: 7 },
                { week: 2, score: 15 },
              ],
            },
          ],
        },
      };
    }

    // 응답 반환
    return res(ctx.status(200), ctx.json(responseData));
  }),

  rest.get('/api/merge-requests/best', (req, res, ctx) => {
    const { projectId } = req.url.searchParams;
    return res(
      ctx.status(200),
      ctx.json({
        status: 'SUCCESS',
        data: [
          {
            id: 641081,
            iid: 69,
            title: '[feat/#349]커스텀-프롬프트',
            description: `
              <!--\n제목 : [{커밋유형}/#이슈숫자] 기능명\nex) [feat/#11] 로그인\n-->
              
              ## 주요 변경사항
              
              - fix:패키지명
              
              <br/>
              
              ## 리뷰 요청사항
              
              - [x] MR Approve
              
              <br/>
              
              ## ➕ 지라 링크
              
              - [S11P31A210-349](https://ssafy.atlassian.net/browse/S11P31A210-349)
              
              <br/>
            `,
            state: 'merged',
            mergedAt: '2024-10-30T06:08:01.482',
            createdAt: '2024-10-30T06:07:20.291',
            updatedAt: '2024-10-31T00:01:57.094',
            closedAt: null,
            sourceBranch: 'fe/S11P31A210-349-ui-커스텀-프롬프트',
            targetBranch: 'fe/dev',
            labels: ['🐛 Fix'],
            hasConflicts: false,
            assignee: {
              username: 'anjs134',
              avatarUrl:
                'https://secure.gravatar.com/avatar/18d31feb03d8981c6c569b9924031f8be04855d7bf40d32a2d66e9093d49cc09?s=80&d=identicon',
            },
            reviewer: {
              username: 'btothey99',
              avatarUrl:
                'https://secure.gravatar.com/avatar/5df4d4186f3aa8c84bf409a74f39adb23d0695b905365155357fda4ed004a8b8?s=80&d=identicon',
            },
            isAiReviewCreated: false,
          },
          {
            id: 641212,
            iid: 71,
            title: '[fix/#327] 테스트 실패 수정',
            description: `
              <!--\n제목 : [{커밋유형}/#이슈숫자] 기능명\nex) [feat/#327] 테스트 실패 수정\n-->
              
              ## 주요 변경사항
              
              - 통합테스트 실패 수정 data.sql이 테스트에서 정상 실행되도록 수정
              - reviewservice 단위테스트 픽스
              
              <br/>
              
              ## 리뷰 요청사항
              
              - [ ] MR Approve
              
              <br/>
              
              ## ➕ 지라 링크
              
              - [S11P31A210-327](https://ssafy.atlassian.net/browse/S11P31A210-327)
              
              <br/>
            `,
            state: 'merged',
            mergedAt: '2024-10-31T00:04:08.295',
            createdAt: '2024-10-30T12:03:43.485',
            updatedAt: '2024-10-31T00:04:08.611',
            closedAt: null,
            sourceBranch: 'be/S11P31A210-376-테스트-코드-실패-핫픽스',
            targetBranch: 'be/dev',
            labels: ['Backend', '✅ Test', '🐛 Fix'],
            hasConflicts: false,
            assignee: {
              username: 'ajsthfldu',
              avatarUrl:
                'https://secure.gravatar.com/avatar/de5618f1a5aedaa97da4da8aea212a4f10088fef603b68a9ef38c7cc3f569930?s=80&d=identicon',
            },
            reviewer: {
              username: 'jimmi219',
              avatarUrl: 'https://lab.ssafy.com/uploads/-/system/user/avatar/17537/avatar.png',
            },
            isAiReviewCreated: false,
          },
          {
            id: 641801,
            iid: 73,
            title: '[feat/#379] 팀 프로젝트 디테일 조회 시, 언어 색상 데이터도 추가',
            description: `
              <!--\n제목 : [{커밋유형}/#이슈숫자] 기능명\nex) [feat/#11] 로그인\n-->
              
              ## 주요 변경사항
              
              - 팀 프로젝트 디테일 조회 시, 언어 색상 데이터도 추가
              - LanguageCode 엔티티 추가
              
              <br/>
              
              ## 리뷰 요청사항
              
              - [ ] MR Approve
              
              <br/>
              
              ## ➕ 지라 링크
              
              - [S11P31A210-379](https://ssafy.atlassian.net/browse/S11P31A210-379)
              
              <br/>
            `,
            state: 'merged',
            mergedAt: '2024-10-31T06:44:17.143',
            createdAt: '2024-10-31T06:32:33.477',
            updatedAt: '2024-10-31T06:44:17.347',
            closedAt: null,
            sourceBranch: 'be/S11P31A210-379-팀-디테일-조회-언어-색상-추가',
            targetBranch: 'be/dev',
            labels: ['Backend', '✨ Feature', '🐛 Fix'],
            hasConflicts: false,
            assignee: {
              username: 'jimmi219',
              avatarUrl: 'https://lab.ssafy.com/uploads/-/system/user/avatar/17537/avatar.png',
            },
            reviewer: {
              username: 'fview',
              avatarUrl:
                'https://secure.gravatar.com/avatar/47167cd2ae5c88c5b69f4a690ab5cdb4554a141768b8d6f17c9d62b6380ee1d2?s=80&d=identicon',
            },
            isAiReviewCreated: false,
          },
        ],
      }),
    );
  }),
  rest.get('/api/review', (req, res, ctx) => {
    const { id } = req.params;
    const { projectId } = req.params;

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

    const mockReviews = [
      {
        id: 1,
        content: '리뷰유ㅠㅠㅠㅠㅠ111111',
        createdAt: '2024-11-01T16:23:50.538468',
        modifiedAt: '2024-11-01T16:23:50.538468',
      },
      {
        id: 2,
        content: '리뷰유ㅠㅠㅠㅠㅠㅠ222222222',
        createdAt: '2024-11-01T16:23:50.538468',
        modifiedAt: '2024-11-01T16:23:50.538468',
      },
      {
        id: 3,
        content: '리뷰유ㅠㅠㅠㅠㅠㅠㅠ33333',
        createdAt: '2024-11-01T16:23:50.538468',
        modifiedAt: '2024-11-01T16:23:50.538468',
      },
    ];

    const mockResponse = {
      status: 'SUCCESS',
      data: {
        mergeRequest: mockMergeRequest,
        reviews: mockReviews,
      },
    };

    return res(ctx.status(200), ctx.json(mockResponse));
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
  rest.get('/api/reviews/:reviewId/retrievals', (req, res, ctx) => {
    const { reviewId } = req.params;
    console.log('Intercepted request for reviewId:', reviewId);

    const mockReferences: Reference[] = [
      {
        id: 1,
        fileName: 'src/components/Album/Detail.java',
        language: 'JAVA',
        content: 'Some code content here...',
      },
      {
        id: 2,
        fileName: 'docs/specifications.md',
        language: 'PLAINTEXT',
        content: 'Some text content here...',
      },
      {
        id: 3,
        fileName: 'docs/example.md',
        language: 'PLAINTEXT',
        content: 'Some text content here...Some text content here...Some text content here...',
      },
      {
        id: 4,
        fileName: 'src/components/Album/Detail.tsx',
        language: 'TYPESCRIPT',
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

    const mockResponse = {
      status: 'SUCCESS',
      data: mockReferences,
    };

    return res(ctx.status(200), ctx.json(mockResponse));
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
  rest.get('/api/custom-prompts/:projectId', (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        status: 'SUCCESS',
        data: {
          customPromptId: 1,
          content:
            '메소드 분리에 더욱 초점을 맞춰줘.\n코드리뷰를 간결하고 명확하게 작성해줘.\n메소드 분리에 더욱 초점을 맞춰줘.\n코드리뷰를 간결하고 명확하게 작성해줘.\n메소드 분리에 더욱 초점을 맞춰줘.\n코드리뷰를 간결하고 명확하게 작성해줘.\n메소드 분리에 더욱 초점을 맞춰줘.\n코드리뷰를 간결하고 명확하게 작성해줘.\n메소드 분리에 더욱 초점을 맞춰줘.\n코드리뷰를 간결하고 명확하게 작성해줘.\n메소드 분리에 더욱 초점을 맞춰줘.\n코드리뷰를 간결하고 명확하게 작성해줘.\n',
        },
      }),
    );
  }),

  // 저장을 위한 POST 핸들러도 추가
  rest.put('/api/custom-prompts/:projectId', async (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        status: 'SUCCESS',
      }),
    );
  }),
];
