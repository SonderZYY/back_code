package com.xuecheng.api.content;

import com.xuecheng.api.content.model.qo.QueryCourseBaseModel;
import com.xuecheng.api.content.model.vo.CourseAuditVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程基本信息管理-运营 Api", tags = "课程基本信息管理-运营")
public interface CourseAuditApi {
    @ApiOperation("课程基础信息条件分页查询-运营")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "model", dataType = "QueryCourseBaseModel", paramType = "body")
    })
    PageVO queryCourseList(PageRequestParams params, QueryCourseBaseModel model);


    @ApiOperation("课程审核(只审核已提交的课程)")
    @ApiImplicitParam(name = "courseAuditVO", value = "课程信息VO", required = true, dataType = "CourseAuditVO", paramType = "body")
    void approveCourseBase(CourseAuditVO courseAuditVO);
}
