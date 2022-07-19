package com.xuecheng.media.agent.fallback;

import com.xuecheng.api.content.model.dto.TeachplanMediaDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.media.agent.ContentApiAgent;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p></p>
 *
 * @Description:
 */
@Slf4j
@Component
public class ContentApiAgentFallbackFactory implements FallbackFactory<ContentApiAgent> {

    @Override
    public ContentApiAgent create(Throwable throwable) {
        return new ContentApiAgent() {
            @Override
            public RestResponse<List<TeachplanMediaDTO>> getByMediaId4s(Long mediaId) {
                log.error("Feign 接口调用熔断降级，错误信息：{}", throwable.getMessage());
                return RestResponse.validfail(CommonErrorCode.E_100101);
            }
        };
    }
}
