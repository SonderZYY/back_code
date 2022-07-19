package com.xuecheng.api.media.model.aliyun;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <P>
 * 视频上传凭证，前端使用此凭证上传
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("视频上传凭证封装类")
public class VodUploadToken {
    /**
     * 请求id
     */
    @ApiModelProperty("请求id")
    private String requestId;
    /**
     * 上传视频的唯一标识
     */
    @ApiModelProperty("上传视频的唯一标识")
    private String videoId;
    /**
     * 上传URL
     */
    @ApiModelProperty("上传URL")
    private String uploadAddress;
    /**
     * 上传凭证
     */
    @ApiModelProperty("上传凭证")
    private String uploadAuth;
}