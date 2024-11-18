package com.reviewping.coflo.domain.webhookchannel.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelCode {
    @Id private Long id;

    @Enumerated(value = EnumType.STRING)
    private ChannelType name;

    private String imageUrl;
}
