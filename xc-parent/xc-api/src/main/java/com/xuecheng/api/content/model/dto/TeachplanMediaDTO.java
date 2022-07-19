package com.xuecheng.api.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="TeachplanMediaDTO", description="")
public class TeachplanMediaDTO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long teachplanMediaId;

    @ApiModelProperty(value = "媒资信息标识")
    private Long mediaId;

    @ApiModelProperty(value = "课程计划标识")
    private Long teachplanId;

    @ApiModelProperty(value = "课程标识")
    private Long courseId;

    @ApiModelProperty(value = "课程发布标识")
    private Long coursePubId;

    @ApiModelProperty(value = "媒资文件原始名称")
    private String mediaFilename;

    //其他代码省略
    @ApiModelProperty(value = "媒资信息")
    private TeachplanMediaDTO teachplanMedia;   //添加课程计划媒资信息属性

}