package com.xuecheng.media.controller;

import com.xuecheng.api.media.MediaApi;
import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.api.media.model.qo.QueryMediaModel;
import com.xuecheng.api.media.model.vo.MediaVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.util.SecurityUtil;
import com.xuecheng.media.convert.MediaConvert;
import com.xuecheng.media.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 媒资信息 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
@RequestMapping("mediaDTO")
public class MediaController implements MediaApi {

    @Autowired
    private MediaService mediaService;

    @PostMapping("media")
    public MediaDTO createMedia(@RequestBody MediaVO vo) {
        //将VO类型的参数转换为DTO类型
        MediaDTO dto = MediaConvert.INSTANCE.vo2dto(vo);
        //获取机构id,参数传递
        Long companyId = SecurityUtil.getCompanyId();
        dto.setCompanyId(companyId);
        return mediaService.createMedia(dto);
    }

    @PostMapping("media/list")
    public PageVO queryMediaList(PageRequestParams params, @RequestBody QueryMediaModel model) {
        Long companyId = SecurityUtil.getCompanyId();
        return mediaService.queryMediaList(params, model, companyId);
    }

    @DeleteMapping("media/{mediaId}")
    public void removeMedia(@PathVariable Long mediaId) {
        Long companyId = SecurityUtil.getCompanyId();
        mediaService.removeMedia(mediaId, companyId);
    }

    @GetMapping("l/media/{mediaId}")
    public RestResponse<MediaDTO> getMediaById4s(@PathVariable Long mediaId) {
        return mediaService.getById4Service(mediaId);
    }
}
