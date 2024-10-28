package com.reviewping.coflo.domain.webhookchannel.entity;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebhookChannel extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_code_id")
    private ChannelCode channelCode;

    @Column(nullable = false, unique = true)
    private String webhookUrl;

    @Builder
    public WebhookChannel(Project project, ChannelCode channelCode, String webhookUrl) {
        this.project = project;
        this.channelCode = channelCode;
        this.webhookUrl = webhookUrl;
    }
}
