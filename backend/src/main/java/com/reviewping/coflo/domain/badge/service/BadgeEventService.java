package com.reviewping.coflo.domain.badge.service;

import static com.reviewping.coflo.domain.badge.entity.BadgeType.*;

import com.reviewping.coflo.domain.badge.entity.BadgeCode;
import com.reviewping.coflo.domain.badge.entity.UserBadge;
import com.reviewping.coflo.domain.badge.repository.BadgeCodeRepository;
import com.reviewping.coflo.domain.badge.repository.UserBadgeRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.user.repository.LoginHistoryRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BadgeEventService {

    // TODO: 운영시에 count 바꾸기
    private static final Long PROJECT_LINK_TARGET_COUNT = 1L;
    private static final Long LOGIN_TARGET_COUNT = 1L;

    private final UserBadgeRepository userBadgeRepository;
    private final BadgeCodeRepository badgeCodeRepository;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final UserProjectRepository userProjectRepository;
    private final LoginHistoryRepository loginHistoryRepository;

    private UserBadge userBadge;
    private BadgeCode badgeCode;

    // 첫 모험가 - 처음 서비스 가입 시 획득
    public void eventFirstLogin(User user) {
        badgeCode = badgeCodeRepository.getById(FIRST_ADVENTURER.getId());
        if (userBadgeRepository.existsByUserAndBadgeCode(user, badgeCode)) return;

        Integer count = gitlabAccountRepository.countByUser(user);

        if (count == 0) {
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
        }
    }

    // 프로젝트 마스터 - 연동한 프로젝트 개수 n개 이상 시 획득
    public void eventProjectLinkAchievement(User user) {
        badgeCode = badgeCodeRepository.getById(PROJECT_MASTER.getId());
        if (userBadgeRepository.existsByUserAndBadgeCode(user, badgeCode)) return;

        List<GitlabAccount> gitlabAccounts = gitlabAccountRepository.findAllByUser(user);
        List<Long> gitlabAccountIds = gitlabAccounts.stream().map(GitlabAccount::getId).toList();
        Long projectCount = userProjectRepository.countByGitlabAccountIds(gitlabAccountIds);

        if (projectCount == PROJECT_LINK_TARGET_COUNT) {
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
        }
    }

    // 단골 손님 - 서비스 로그인 n회 이상 (1일 1회)
    public void eventLoginCount(User user) {
        badgeCode = badgeCodeRepository.getById(REGULAR_CUSTOMER.getId());
        if (userBadgeRepository.existsByUserAndBadgeCode(user, badgeCode)) return;

        long loginCount = loginHistoryRepository.countByUserId(user.getId());
        if (loginCount >= LOGIN_TARGET_COUNT) {
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
        }
    }
}
