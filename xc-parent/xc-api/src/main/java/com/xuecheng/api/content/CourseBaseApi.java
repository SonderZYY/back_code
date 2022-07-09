package com.xuecheng.api.content;

import com.xuecheng.api.content.qo.QueryCourseBaseModel;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "CourseBase Api", tags = "课程基础信息管理Api")
public interface CourseBaseApi {

    @ApiOperation("课程基础信息分页条件查询")
    //对象的形参不添加Swagger注解
    PageVO queryCourseList(PageRequestParams params, QueryCourseBaseModel model);
}
