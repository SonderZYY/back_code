package com.xuecheng.swagger.controller;

import com.xuecheng.swagger.domain.Student;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "hello Control",
        tags = "hello api 接口文档-tags",
        description = "hello Api接口文档 - desc")
public interface HelloApi {

    @ApiOperation("修改学员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "学员id值",required = true,dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "name", value = "学员姓名",required = true,dataType = "String",paramType = "query")
    })
    Student mofidyStudentBynNum(String id, String name, Student student);
}
