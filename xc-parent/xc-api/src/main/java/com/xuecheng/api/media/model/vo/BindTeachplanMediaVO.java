package com.xuecheng.api.media.model.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 *     教学计划-媒资绑定提交数据
 * </p>
 *
 * @Description:
 */
@Data
@ApiModel(value="BindTeachplanMediaVO", description="教学计划-媒资绑定提交数据")
public class BindTeachplanMediaVO {
    @ApiModelProperty(value = "媒资信息标识", required = true)
    private Long mediaId;
    @ApiModelProperty(value = "课程计划标识", required = true)
    private Long teachplanId;
}