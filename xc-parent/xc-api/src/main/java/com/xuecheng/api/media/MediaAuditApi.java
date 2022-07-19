package com.xuecheng.api.media;

import com.xuecheng.api.media.model.qo.QueryMediaModel;
import com.xuecheng.api.media.model.vo.MediaAuditVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("运营平台媒资信息管理")
public interface MediaAuditApi {
    @ApiOperation("运营平台分页条件查询媒资信息")
    PageVO queryMediaList(PageRequestParams params, QueryMediaModel model);

    @ApiOperation("运营平台审核媒资")
    void approveMedia(MediaAuditVO vo);
}
