package com.xuecheng.feign.media;

import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.common.constant.XcFeignServiceNameList;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.feign.media.sentinel.MediaApiAgentFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p></p>
 *
 * @Description:
 */
@FeignClient(value = XcFeignServiceNameList.XC_MEDIA_SERVICE,fallbackFactory = MediaApiAgentFallbackFactory.class)
public interface MediaApiAgent {

    String PREFIX_FLAG = "/media/";

    /* feign的路径必须是完整路径（不要写协议、ip、port） */
    /* 少了服务的根路径 */
    @GetMapping(PREFIX_FLAG+"l/media/{mediaId}")
    public RestResponse<MediaDTO> getMediaById4s(@PathVariable Long mediaId);

}
