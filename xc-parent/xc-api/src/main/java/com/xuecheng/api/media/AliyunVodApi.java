package com.xuecheng.api.media;

import com.xuecheng.api.media.model.aliyun.VodUploadRequest;
import com.xuecheng.api.media.model.aliyun.VodUploadToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p></p>
 *
 * @Description:
 */
@Api("媒资管理服务Api")
public interface AliyunVodApi {


    @ApiOperation("获得媒资文件上传凭证")
    VodUploadToken generateUploadToken(VodUploadRequest uploadRequest);


    @ApiOperation("刷新媒资文件上传凭证")
    @ApiImplicitParam(name = "videoId",value = "媒资文件的id值",required = true,paramType = "path",dataType = "String")
    VodUploadToken refreshUploadToken(String videoId);

    @ApiOperation("获得媒资播放地址")
    String getPlayUrlByMediaId(Long mediaId);

    @ApiOperation("获得媒资播放地址-运营平台")
    String getPlayUrlByMediaId4Audit(Long mediaId);

}
