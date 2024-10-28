package com.reviewping.coflo.domain.webhookchannel.service;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.webhookchannel.entity.ChannelCode;
import com.reviewping.coflo.domain.webhookchannel.entity.WebhookChannel;
import com.reviewping.coflo.domain.webhookchannel.repository.WebhookChannelRepository;
import com.reviewping.coflo.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebhookChannelService {

    private final WebhookChannelRepository webhookChannelRepository;
    private final ChannelCodeService channelCodeService;
    private final ProjectRepository projectRepository;

    // TODO: ProjectRepository => ProjectService
    @Transactional
    public void addWebhookChannel(Long projectId, Long channelCodeId, String webhookUrl) {
        Project project =
                projectRepository
                        .findById(projectId)
                        .orElseThrow(() -> new BusinessException(PROJECT_NOT_EXIST));

        ChannelCode channelCode = channelCodeService.findById(channelCodeId);

        WebhookChannel webhookChannel =
                WebhookChannel.builder()
                        .project(project)
                        .channelCode(channelCode)
                        .webhookUrl(webhookUrl)
                        .build();

        webhookChannelRepository.save(webhookChannel);
    }
}
