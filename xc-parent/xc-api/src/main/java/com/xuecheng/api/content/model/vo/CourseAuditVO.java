package com.xuecheng.api.content.model.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 *     课程审核时的VO对象
 * </p>
 */
@Data
@ApiModel(value="CourseBaseAuditVO", description="课程审核时的VO对象")
public class CourseAuditVO {
    @ApiModelProperty(value = "课程Id")
    private Long courseBaseId;
    @ApiModelProperty(value = "审核状态：参照数据字典 code 为 202")
    private String auditStatus;
    @ApiModelProperty(value = "审核意见")
    private String auditMind;
}