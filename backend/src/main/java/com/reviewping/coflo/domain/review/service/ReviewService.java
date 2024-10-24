package com.reviewping.coflo.domain.review.service;

import com.reviewping.coflo.domain.project.entity.MrInfo;
import com.reviewping.coflo.domain.project.repository.MrInfoRepository;
import com.reviewping.coflo.domain.review.entity.Review;
import com.reviewping.coflo.domain.review.repository.ReviewRepository;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final MrInfoRepository mrInfoRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public void saveReview(Long projectId, Long iid, String chatResult) {
        MrInfo mrInfo = getMrInfo(projectId, iid);
        Review review = Review.builder().mrInfo(mrInfo).content(chatResult).build();
        reviewRepository.save(review);
    }

    private MrInfo getMrInfo(Long projectId, Long iid) {
        return mrInfoRepository
                .findByProjectIdAndGitlabMrIid(projectId, iid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MR_INFO_NOT_EXIST));
    }
}
