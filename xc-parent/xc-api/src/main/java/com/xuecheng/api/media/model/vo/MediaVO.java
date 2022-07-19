package com.xuecheng.api.media.model.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 媒资保存信息
 */
@Data
@ApiModel(value="MediaVO", description="媒资保存信息")
public class MediaVO {
    @ApiModelProperty(value = "文件名称", required = true)
    private String filename;
    @ApiModelProperty(value = "文件类型（文档，音频，视频）", required = true)
    private String type;
    @ApiModelProperty(value = "文件标识， 七牛的key,视频的videoId", required = true)
    private String fileId;
    @ApiModelProperty(value = "标签")
    private String tags;
    @ApiModelProperty(value = "存储空间")
    private String bucket;
}