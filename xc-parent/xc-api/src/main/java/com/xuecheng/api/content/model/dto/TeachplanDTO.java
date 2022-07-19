package com.xuecheng.api.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
/**
 * <p>
 * 课程计划
 * </p>
 */
@Data
@ApiModel(value="TeachplanDTO", description="课程计划")
public class TeachplanDTO implements Serializable {

    // 可以免费观看
    public static final String IS_PREVIEW = "1";
    // 可以收费观看
    public static final String IS_NOT_PREVIEW = "0";


    @ApiModelProperty(value = "课程计划Id")
    private Long teachPlanId;
    @ApiModelProperty(value = "课程计划名称")
    private String pname;
    @ApiModelProperty(value = "课程计划父级Id")
    private Long parentid;
    @ApiModelProperty(value = "层级，分为1、2、3级")
    private Integer grade;
    @ApiModelProperty(value = "开始直播时间(仅限直播类型,直播时不能为空)")
    private LocalDateTime startTime;
    @ApiModelProperty(value = "开始直播时间(仅限直播类型,直播时不能为空)")
    private LocalDateTime endTime;
    
    @ApiModelProperty(value = "排序字段")
    private Integer orderby;
    @ApiModelProperty(value = "课程标识")
    private Long courseId;
    @ApiModelProperty(value = "课程发布标识")
    private Long cousePubId;
    @ApiModelProperty(value = "发布状态")
    private String status;
    @ApiModelProperty(value = "是否支持试学或预览")
    private String isPreview;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime changeDate;
    @ApiModelProperty(value = "课程计划子级树形结构集合")
    private List<TeachplanDTO> teachPlanTreeNodes;

    //其他代码省略
    @ApiModelProperty(value = "媒资信息")
    private TeachplanMediaDTO teachplanMedia;   //添加课程计划媒资信息属性
}