package com.reviewping.coflo.domain.webhookchannel.repository;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.webhookchannel.entity.WebhookChannel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookChannelRepository extends JpaRepository<WebhookChannel, Long> {
    List<WebhookChannel> findAllByProject(Project project);
}
