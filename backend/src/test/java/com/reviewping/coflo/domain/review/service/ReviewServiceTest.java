package com.reviewping.coflo.domain.review.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.reviewping.coflo.domain.project.entity.MrInfo;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.MrInfoRepository;
import com.reviewping.coflo.domain.review.entity.Review;
import com.reviewping.coflo.domain.review.repository.ReviewRepository;
import com.reviewping.coflo.global.error.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock private MrInfoRepository mrInfoRepository;

    @Mock private ReviewRepository reviewRepository;

    @InjectMocks private ReviewService reviewService;

    @Test
    public void shouldSaveReviewSuccessfully() {
        // given
        Long projectId = 1L;
        Long iid = 100L;
        String chatResult = "This is a review";

        MrInfo mrInfo = MrInfo.builder().project(Project.builder().build()).build();
        given(mrInfoRepository.getByProjectIdAndGitlabMrIid(projectId, iid)).willReturn(mrInfo);

        // when
        reviewService.saveReview(projectId, iid, chatResult);

        // then
        then(reviewRepository).should().save(any(Review.class));
    }

    @Test
    public void shouldThrowExceptionWhenMrInfoNotFound() {
        // given
        Long projectId = 1L;
        Long iid = 100L;
        String chatResult = "This is a review";

        given(mrInfoRepository.getByProjectIdAndGitlabMrIid(projectId, iid))
                .willThrow(BusinessException.class);
        assertThrows(
                BusinessException.class,
                () -> {
                    reviewService.saveReview(projectId, iid, chatResult);
                });

        then(reviewRepository).should(never()).save(any(Review.class));
    }
}
