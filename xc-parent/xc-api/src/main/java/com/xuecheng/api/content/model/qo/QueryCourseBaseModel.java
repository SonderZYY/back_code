package com.xuecheng.api.content.model.qo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p></p>
 *
 * @Description:
 */
@Data
@ApiModel("课程基础信息查询封装对象")
public class QueryCourseBaseModel {

   private String auditStatus;
   private String courseName;

}
