package com.reviewping.coflo.domain.webhookchannel.controller.dto.response;

import com.reviewping.coflo.domain.webhookchannel.entity.ChannelCode;

public record ChannelCodeResponse(Long channelCodeId, String channelName) {
    public static ChannelCodeResponse of(ChannelCode channelCode) {
        return new ChannelCodeResponse(channelCode.getId(), channelCode.getName().name());
    }
}
