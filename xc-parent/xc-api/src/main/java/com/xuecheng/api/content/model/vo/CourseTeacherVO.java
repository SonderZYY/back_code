package com.xuecheng.api.content.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value="CourseTeacherVO", description="教师信息")
public class CourseTeacherVO {
    @ApiModelProperty(value = "教师id")
    private Long courseTeacherId;
    @ApiModelProperty(value = "课程标识", required = true)
    private Long courseId;
    @ApiModelProperty(value = "课程发布标识")
    private Long coursePubId;
    @ApiModelProperty(value = "教师简介")
    private String introduction;
    @ApiModelProperty(value = "照片", required = true)
    private String photograph;
    @ApiModelProperty(value = "教师职位", required = true)
    private String position;
    @ApiModelProperty(value = "教师姓名", required = true)
    private String teacherName;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;


}
