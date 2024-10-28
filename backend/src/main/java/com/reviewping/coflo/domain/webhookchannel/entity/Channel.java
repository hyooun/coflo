package com.reviewping.coflo.domain.webhookchannel.entity;

import lombok.Getter;

@Getter
public enum Channel {
    MATTERMOST("MATTERMOST"),
    DISCORD("DISCORD");

    private final String name;

    Channel(String name) {
        this.name = name;
    }
}
