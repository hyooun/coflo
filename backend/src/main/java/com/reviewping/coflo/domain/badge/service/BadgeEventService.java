package com.reviewping.coflo.domain.badge.service;

import static com.reviewping.coflo.domain.badge.entity.BadgeType.*;

import com.reviewping.coflo.domain.badge.entity.BadgeCode;
import com.reviewping.coflo.domain.badge.entity.UserBadge;
import com.reviewping.coflo.domain.badge.repository.BadgeCodeRepository;
import com.reviewping.coflo.domain.badge.repository.UserBadgeRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BadgeEventService {

    private static final Long PROJECT_LINK_TARGET_COUNT = 1L;

    private final UserBadgeRepository userBadgeRepository;
    private final BadgeCodeRepository badgeCodeRepository;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final UserProjectRepository userProjectRepository;

    private UserBadge userBadge;
    private BadgeCode badgeCode;

    // 처음 서비스 가입 시 획득
    public void eventFirstLogin(User user) {
        Integer count = gitlabAccountRepository.countByUser(user);
        badgeCode = badgeCodeRepository.getById(FIRST_ADVENTURER.getId());

        if (count == 0 && !userBadgeRepository.existsByUserAndBadgeCode(user, badgeCode)) {
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
        }
    }

    // 연동한 프로젝트 개수 n개 이상 시 획득
    public void eventProjectLinkAchievement(User user) {
        List<GitlabAccount> gitlabAccounts = gitlabAccountRepository.findAllByUser(user);
        List<Long> gitlabAccountIds = gitlabAccounts.stream().map(GitlabAccount::getId).toList();
        badgeCode = badgeCodeRepository.getById(PROJECT_MASTER.getId());
        Long projectCount = userProjectRepository.countByGitlabAccountIds(gitlabAccountIds);

        if (projectCount == PROJECT_LINK_TARGET_COUNT
                && !userBadgeRepository.existsByUserAndBadgeCode(user, badgeCode)) {
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
        }
    }
}
