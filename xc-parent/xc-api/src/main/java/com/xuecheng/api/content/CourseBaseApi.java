package com.xuecheng.api.content;


import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.api.content.model.qo.QueryCourseBaseModel;
import com.xuecheng.api.content.model.vo.CourseBaseVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "CourseBase Api", tags = "课程基础信息管理Api")
public interface CourseBaseApi {

    //对象的形参不添加Swagger注解
    @ApiOperation("课程基础信息分页条件查询")
    PageVO queryCourseList(PageRequestParams params, QueryCourseBaseModel model);

    @ApiOperation("课程基础信息的添加")
    CourseBaseDTO creatCourseBase(CourseBaseVO vo);

    @ApiOperation("根据id获取课程基础信息")
    @ApiImplicitParam(name = "courseBaseId", value = "课程基本信息id", required = true, dataType = "Long", paramType = "path")
    CourseBaseDTO getCourseBaseById(Long courseBaseId);

    @ApiOperation("根据id修改课程基础信息")
    CourseBaseDTO modifyCourseBaseById(CourseBaseVO vo);

    @ApiOperation("根据id删除课程基础信息")
    @ApiImplicitParam(name = "courseBaseId", value = "课程基本信息id", required = true, dataType = "Long", paramType = "path")
    void removeCourseById(Long courseId);

    @ApiOperation(value = "课程提交审核功能")
    @ApiImplicitParam(name = "courseBaseId",value = "课程id",required = true,dataType = "Long", paramType = "path")
    void commitCourseBase(Long courseBaseId);
}
