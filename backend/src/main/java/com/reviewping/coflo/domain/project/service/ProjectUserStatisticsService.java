package com.reviewping.coflo.domain.project.service;

import com.reviewping.coflo.domain.project.controller.response.UserProjectIndividualScoreResponse;
import com.reviewping.coflo.domain.project.controller.response.UserProjectTotalScoreResponse;
import com.reviewping.coflo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectUserStatisticsService {

    public UserProjectTotalScoreResponse getTotalCumulativeScore(User user, Long projectId) {
        return null;
    }

    public UserProjectIndividualScoreResponse getIndividualCumulativeScore(
            User user, Long projectId) {
        return null;
    }

    public UserProjectTotalScoreResponse getTotalAcquisitionScore(User user, Long projectId) {
        return null;
    }

    public UserProjectIndividualScoreResponse getIndividualAcquisitionScore(
            User user, Long projectId) {
        return null;
    }
}
