package com.xuecheng.api.content;

import com.xuecheng.api.content.model.dto.CourseTeacherDTO;
import com.xuecheng.api.content.model.vo.CourseTeacherVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api("课程教师管理接口")
public interface CourseTeacherApi {

    @ApiOperation("根据课程id查询课程教师信息")
    @ApiImplicitParam(name = "courseBaseId", value = "课程Id值", required = true, dataType = "Long", paramType = "path")
    List<CourseTeacherDTO> queryCourseTeacherList(Long courseBaseId);

    @ApiOperation(value= "新增或修改教师信息")
    @ApiImplicitParam(name = "courseTeacherVO",value = "教师信息VO" ,
            required = true, dataType = "CourseTeacherVO",paramType = "body")
    CourseTeacherDTO createOrModifyCourseTeach(CourseTeacherVO courseTeacherVO);


    @ApiOperation("根据Id删除教师信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", value = "课程id值", required = true, paramType = "path"),
            @ApiImplicitParam(name = "courseTeacherId", value = "教师id值", required = true, paramType = "path")
    })
    void removeCourseTeachById(Long courseId,Long courseTeacherId);

}
