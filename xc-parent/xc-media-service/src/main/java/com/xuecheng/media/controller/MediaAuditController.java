package com.xuecheng.media.controller;

import com.xuecheng.api.media.MediaAuditApi;
import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.api.media.model.qo.QueryMediaModel;
import com.xuecheng.api.media.model.vo.MediaAuditVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.media.convert.MediaConvert;
import com.xuecheng.media.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 媒资信息 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
public class MediaAuditController implements MediaAuditApi {

    // 运营平台标识--可以查询所哟教学机构的数据
    public static final Long OPERATION_FLAG = -99887799L;

    @Autowired
    private MediaService mediaService;


    @PostMapping("m/media/list")
    public PageVO queryMediaList(PageRequestParams params, @RequestBody QueryMediaModel model) {
        return mediaService.queryMediaList(params, model, OPERATION_FLAG);
    }

    @PutMapping("m/media/audit")
    public void approveMedia(@RequestBody MediaAuditVO vo) {
        MediaDTO mediaDTO = MediaConvert.INSTANCE.audit2dto(vo);
        mediaService.approveMedia(mediaDTO);
    }
}
