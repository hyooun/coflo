package com.reviewping.coflo.domain.webhookchannel.service;

import static com.reviewping.coflo.global.error.ErrorCode.WEBHOOK_REQUEST_SERIALIZATION_ERROR;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.webhookchannel.controller.dto.request.DiscordContent;
import com.reviewping.coflo.domain.webhookchannel.controller.dto.request.MattermostContent;
import com.reviewping.coflo.domain.webhookchannel.controller.dto.request.WebhookContent;
import com.reviewping.coflo.domain.webhookchannel.controller.dto.response.WebhookChannelResponse;
import com.reviewping.coflo.domain.webhookchannel.entity.ChannelCode;
import com.reviewping.coflo.domain.webhookchannel.entity.ChannelType;
import com.reviewping.coflo.domain.webhookchannel.entity.WebhookChannel;
import com.reviewping.coflo.domain.webhookchannel.repository.ChannelCodeRepository;
import com.reviewping.coflo.domain.webhookchannel.repository.WebhookChannelRepository;
import com.reviewping.coflo.global.error.exception.BusinessException;
import com.reviewping.coflo.global.util.RestTemplateUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebhookChannelService {

    private final ObjectMapper objectMapper;

    private final WebhookChannelRepository webhookChannelRepository;
    private final ChannelCodeRepository channelCodeRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void addWebhookChannel(Long projectId, Long channelCodeId, String webhookUrl) {
        Project project = projectRepository.getById(projectId);
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
        Project project = projectRepository.getById(projectId);

        List<WebhookChannel> webhookChannels = webhookChannelRepository.findAllByProject(project);
        return webhookChannels.stream().map(WebhookChannelResponse::of).toList();
    }

    public void sendData(Long projectId, String content) {
        Project project = projectRepository.getById(projectId);
        List<WebhookChannel> webhookChannels = project.getWebhookChannels();

        webhookChannels.stream()
                .forEach(
                        webhookChannel -> {
                            send(
                                    webhookChannel.getWebhookUrl(),
                                    content,
                                    webhookChannel.getChannelCode());
                        });
    }

    @Transactional
    public void updateWebhookChannel(Long webhookChannelId, String webhookUrl) {
        WebhookChannel webhookChannel = webhookChannelRepository.getById(webhookChannelId);
        webhookChannel.updateWebhookUrl(webhookUrl);
    }

    @Transactional
    public void deleteWebhookChannel(Long webhookChannelId) {
        WebhookChannel webhookChannel = webhookChannelRepository.getById(webhookChannelId);
        webhookChannelRepository.delete(webhookChannel);
    }

    private void send(String url, String content, ChannelCode channelCode) {
        HttpHeaders headers = RestTemplateUtils.createHeaders(APPLICATION_JSON_VALUE);
        WebhookContent webhookContent = getWebhookContent(content, channelCode.getName());

        String body;
        try {
            body = objectMapper.writeValueAsString(webhookContent);
        } catch (JsonProcessingException e) {
            throw new BusinessException(WEBHOOK_REQUEST_SERIALIZATION_ERROR);
        }

        ResponseEntity<String> response =
                RestTemplateUtils.sendPostRequest(
                        url, headers, body, new ParameterizedTypeReference<>() {});
    }

    private static WebhookContent getWebhookContent(String content, ChannelType channelType) {
        switch (channelType) {
            case ChannelType.MATTERMOST:
                return new MattermostContent(content);
            case ChannelType.DISCORD:
                return new DiscordContent(content);
            default:
                return null;
        }
    }
}
