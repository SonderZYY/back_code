package com.xuecheng.feign.system;

import com.xuecheng.api.system.model.dto.CourseCategoryDTO;
import com.xuecheng.common.constant.XcFeignServiceNameList;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.feign.system.sentinel.SystemApiAgentFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>
 *     内容管理对系统管理的 Feign 的远程调用
 * </p>
 *
 * @Description:
 */
@FeignClient(value = XcFeignServiceNameList.XC_SYSTEM_SERVICE,fallbackFactory = SystemApiAgentFallbackFactory.class)
public interface SystemApiAgent {

    String PREFIX_FLAG = "/system/l/";

    @GetMapping(PREFIX_FLAG+"course-category/{id}")
    RestResponse<CourseCategoryDTO> getById(@PathVariable String id);

}