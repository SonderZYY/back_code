package com.xuecheng.feign.media.sentinel;

import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.feign.media.MediaApiAgent;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p></p>
 *
 * @Description:
 */
@Slf4j
@Component
public class MediaApiAgentFallbackFactory implements FallbackFactory<MediaApiAgent> {


    @Override
    public MediaApiAgent create(Throwable throwable) {
        return new MediaApiAgent() {
            @Override
            public RestResponse<MediaDTO> getMediaById4s(Long mediaId) {
                log.error(CommonErrorCode.E_999981.getDesc()+" error {}",throwable.getMessage());
                return RestResponse.validfail(CommonErrorCode.E_999981);
            }
        };
    }
}
