package com.reviewping.coflo.domain.webhookchannel.repository;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.webhookchannel.entity.WebhookChannel;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookChannelRepository extends JpaRepository<WebhookChannel, Long> {
    List<WebhookChannel> findAllByProject(Project project);

    default WebhookChannel getById(Long id) {
        return findById(id).orElseThrow(() -> new BusinessException(WEBHOOK_CHANNEL_NOT_EXIST));
    }
}
