package com.reviewping.coflo.domain.webhookchannel.service;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.webhookchannel.controller.dto.response.WebhookChannelResponse;
import com.reviewping.coflo.domain.webhookchannel.entity.ChannelCode;
import com.reviewping.coflo.domain.webhookchannel.entity.WebhookChannel;
import com.reviewping.coflo.domain.webhookchannel.repository.ChannelCodeRepository;
import com.reviewping.coflo.domain.webhookchannel.repository.WebhookChannelRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebhookChannelService {

    private final WebhookChannelRepository webhookChannelRepository;
    private final ChannelCodeRepository channelCodeRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void addWebhookChannel(Long projectId, Long channelCodeId, String webhookUrl) {
        Project project = projectRepository.findProjectById(projectId);
        ChannelCode channelCode = channelCodeRepository.findChannelCodeById(channelCodeId);

        WebhookChannel webhookChannel =
                WebhookChannel.builder()
                        .project(project)
                        .channelCode(channelCode)
                        .webhookUrl(webhookUrl)
                        .build();

        webhookChannelRepository.save(webhookChannel);
    }

    public List<WebhookChannelResponse> getWebhookChannelList(Long projectId) {
        Project project = projectRepository.findProjectById(projectId);

        List<WebhookChannel> webhookChannelList =
                webhookChannelRepository.findAllByProject(project);
        return webhookChannelList.stream()
                        .map(WebhookChannelResponse::of)
                        .toList();
    }
}
