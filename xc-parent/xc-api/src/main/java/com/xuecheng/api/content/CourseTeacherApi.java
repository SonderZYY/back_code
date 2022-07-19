package com.xuecheng.api.content;

import com.xuecheng.api.content.model.dto.CourseTeacherDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api("课程教师管理接口")
public interface CourseTeacherApi {

    @ApiOperation("根据课程id查询课程教师信息")
    @ApiImplicitParam(name = "courseBaseId", value = "课程Id值", required = true, dataType = "Long", paramType = "path")
    List<CourseTeacherDTO> queryCourseTeacherList(Long courseBaseId);

}
