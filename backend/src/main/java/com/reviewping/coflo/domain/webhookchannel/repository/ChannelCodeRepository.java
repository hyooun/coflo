package com.reviewping.coflo.domain.webhookchannel.repository;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.domain.webhookchannel.entity.ChannelCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelCodeRepository extends JpaRepository<ChannelCode, Long> {
    default ChannelCode findChannelCodeById(Long channelCodeId) {
        return findById(channelCodeId)
                .orElseThrow(() -> new BusinessException(CHANNEL_CODE_NOT_EXIST));
    }
}
