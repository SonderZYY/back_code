package com.xuecheng.api.content;

import com.xuecheng.api.content.model.file.UploadTokenResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "内容管理文件API")
public interface FileManagerApi {
    @ApiOperation("获得上传文件的凭证方法")
    UploadTokenResult generateUploadToken();
}
