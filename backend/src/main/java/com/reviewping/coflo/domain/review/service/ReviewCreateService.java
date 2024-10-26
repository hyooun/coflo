package com.reviewping.coflo.domain.review.service;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabMrDiffsContent;
import com.reviewping.coflo.domain.gitlab.service.GitLabApiService;
import com.reviewping.coflo.domain.openai.dto.response.ChatCompletionContent;
import com.reviewping.coflo.domain.openai.service.OpenaiApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewCreateService {

    private final GitLabApiService gitLabApiService;
    private final OpenaiApiService openAIApiService;
    private final ReviewService reviewService;

    public void makeCodeReviewWhenCalledByWebhook(
            String gitlabUrl,
            String token,
            Long gitlabProjectId,
            Long iid,
            String mrDescription,
            Long projectId) {
        // 1. 변경사항가져오기
        List<GitlabMrDiffsContent> mrDiffs =
                gitLabApiService.getMrDiffs(gitlabUrl, token, gitlabProjectId, iid);
        // 2. 프롬프트 빌드
        String prompt = buildPrompt(mrDescription, mrDiffs);
        // 2. openAI API 호출
        ChatCompletionContent chatCompletionContent = openAIApiService.chat(prompt);
        String chatMessage = chatCompletionContent.choices().getFirst().message().content();
        // 3. 리뷰 저장
        reviewService.saveReview(projectId, iid, chatMessage);
        // 4. 응답 반환(gitlab service)
        gitLabApiService.addNoteToMr(gitlabUrl, token, gitlabProjectId, iid, chatMessage);
    }

    // TODO: MR변경사항을 Prompt에 넣을 때 더 좋은 형식으로 넣기
    private String makeMrDiffsToPromptInput(List<GitlabMrDiffsContent> mrDiffs) {
        return mrDiffs.toString();
    }

    // TODO: Prompt 엔지니어링
    private String buildPrompt(String mrDescription, List<GitlabMrDiffsContent> mrDiffs) {
        String promptPreset = getPromptPreset();
        return mrDescription + makeMrDiffsToPromptInput(mrDiffs) + promptPreset;
    }

    // TODO: Prompt 엔지니어링
    private String getPromptPreset() {
        return "\n"
                + "주어진 코드를 검토하고 개선할 부분을 제안해 주세요. 성능, 가독성, 유지보수성, 또는 스타일 측면에서 수정이 필요한 부분이 있으면"
                + " 알려주세요.\n";
    }
}
