package com.xuecheng.api.content.qo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("课程基础信息查询封装对象")
public class QueryCourseBaseModel {
    private String audiStatus;
    private String courseName;
}
