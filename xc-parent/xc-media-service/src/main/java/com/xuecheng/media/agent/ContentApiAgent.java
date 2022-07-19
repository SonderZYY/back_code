package com.xuecheng.media.agent;

import com.xuecheng.api.content.model.dto.TeachplanMediaDTO;
import com.xuecheng.common.constant.XcFeignServiceNameList;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.media.agent.fallback.ContentApiAgentFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p></p>
 *
 * @Description:
 */
@FeignClient(value = XcFeignServiceNameList.XC_CONTENT_SERVICE,fallbackFactory = ContentApiAgentFallbackFactory.class)
public interface ContentApiAgent {

    String PREFIX_FLAG = "/content/l/";

    @GetMapping(PREFIX_FLAG+"teachplan/media/{mediaId}")
    RestResponse<List<TeachplanMediaDTO>> getByMediaId4s(@PathVariable Long mediaId);

}
