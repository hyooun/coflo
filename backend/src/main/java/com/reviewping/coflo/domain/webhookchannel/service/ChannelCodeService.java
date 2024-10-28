package com.reviewping.coflo.domain.webhookchannel.service;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.domain.webhookchannel.entity.ChannelCode;
import com.reviewping.coflo.domain.webhookchannel.repository.ChannelCodeRepository;
import com.reviewping.coflo.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChannelCodeService {

    private final ChannelCodeRepository channelCodeRepository;

    public ChannelCode findById(Long channelCodeId) {
        return channelCodeRepository
                .findById(channelCodeId)
                .orElseThrow(() -> new BusinessException(CHANNEL_CODE_NOT_EXIST));
    }
}
