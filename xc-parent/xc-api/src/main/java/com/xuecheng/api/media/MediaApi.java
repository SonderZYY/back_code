package com.xuecheng.api.media;

import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.api.media.model.qo.QueryMediaModel;
import com.xuecheng.api.media.model.vo.MediaVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "媒资管理", tags = "媒资管理Api", description = "媒资信息管理")
public interface MediaApi {
    @ApiOperation("保存媒资信息")
    @ApiImplicitParam(name = "vo", value = "媒资信息保存", required = true, dataType = "MediaVo", paramType = "body")
    MediaDTO createMedia(MediaVO vo);

    @ApiOperation("分页查询媒资信息")
    PageVO queryMediaList(PageRequestParams params, QueryMediaModel model);

    @ApiOperation("根据id删除媒资信息")
    @ApiImplicitParam(name = "mediaId",value = "媒资id",required = true,dataType = "Long",paramType = "path")
    void removeMedia(Long mediaId);

    @ApiOperation("根据id查询媒资信息--远程调用")
    @ApiImplicitParam(name = "mediaId",value = "媒资id",required = true,dataType = "Long",paramType = "path")
    RestResponse<MediaDTO> getMediaById4s(Long mediaId);
}
