package com.reviewping.coflo.domain.webhookchannel.controller.dto.response;

import com.reviewping.coflo.domain.webhookchannel.entity.ChannelCode;

public record ChannelCodeResponse(Long channelCodeId, String channelName, String imageUrl) {
    public static ChannelCodeResponse of(ChannelCode channelCode) {
        return new ChannelCodeResponse(
                channelCode.getId(), channelCode.getName().name(), channelCode.getImageUrl());
    }
}
