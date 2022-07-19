package com.xuecheng.api.media.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @Description:
 */
@Data
public class MediaAuditVO {

    @ApiModelProperty(value = "媒资Id")
    private Long id;

    @ApiModelProperty(value = "审核状态：参照数据字典 code 为 202")
    private String auditStatus;

    @ApiModelProperty(value = "审核意见")
    private String auditMind;

}
