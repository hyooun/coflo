package com.reviewping.coflo.domain.badge.service;

import static com.reviewping.coflo.domain.badge.entity.BadgeType.*;

import com.reviewping.coflo.domain.badge.entity.BadgeCode;
import com.reviewping.coflo.domain.badge.entity.UserBadge;
import com.reviewping.coflo.domain.badge.repository.BadgeCodeRepository;
import com.reviewping.coflo.domain.badge.repository.UserBadgeRepository;
import com.reviewping.coflo.domain.customPrompt.repository.PromptHistoryRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.GitlabAccountRepository;
import com.reviewping.coflo.domain.user.repository.LoginHistoryRepository;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.global.util.RandomUtil;
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
    private static final Long PROMTPT_UPDATE_TARGET_COUNT = 2L;

    private final UserBadgeRepository userBadgeRepository;
    private final BadgeCodeRepository badgeCodeRepository;
    private final GitlabAccountRepository gitlabAccountRepository;
    private final UserProjectRepository userProjectRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final PromptHistoryRepository promptHistoryRepository;

    private final RandomUtil randomUtil;

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

    // 프롬프트 창조자 - 커스텀 프롬프트 수정 n회 이상
    public void eventUpdateCustomPrompt(User user) {
        long promptCount = promptHistoryRepository.countByUserId(user.getId());
        if (promptCount == PROMTPT_UPDATE_TARGET_COUNT) {
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
        }
    }

    // 행운의 발견 - 접속 시 1% 확률로 랜덤 획득
    @Transactional
    public void eventRandomLucky(User user) {
        if (!randomUtil.random()) return;

        badgeCode = badgeCodeRepository.getById(LUCKY_FIND.getId());
        if (!userBadgeRepository.existsByUserAndBadgeCode(user, badgeCode)) {
            userBadge = UserBadge.of(user, badgeCode);
            userBadgeRepository.save(userBadge);
        }
    }
}
