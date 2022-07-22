package com.xuecheng.feign.system.sentinel;

import com.xuecheng.api.system.model.dto.CourseCategoryDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.feign.system.SystemApiAgent;
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
public class SystemApiAgentFallbackFactory implements FallbackFactory<SystemApiAgent> {

    @Override
    public SystemApiAgent create(Throwable throwable) {
        return new SystemApiAgent() {
            @Override
            public RestResponse<CourseCategoryDTO> getById(String id) {
                log.error(CommonErrorCode.E_999981.getDesc()+" error {}",throwable.getMessage());
                return RestResponse.validfail(CommonErrorCode.E_999981);
            }
        };
    }
}
