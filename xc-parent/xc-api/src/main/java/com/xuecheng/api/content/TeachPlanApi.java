package com.xuecheng.api.content;

import com.xuecheng.api.content.model.dto.TeachplanDTO;
import com.xuecheng.api.content.model.vo.TeachplanVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程计划信息管理")
public interface TeachPlanApi {

    @ApiOperation(value = "根据课程Id查询课程计划树形结构(树形结构为三级目录)")
    @ApiImplicitParam(name = "courseId", value = "课程Id值", required = true, dataType = "Long", paramType = "path")
    public TeachplanDTO queryTreeNodesByCourseId(Long courseId);

    @ApiOperation(value = "课程计划的添加和修改")
    TeachplanDTO creatOrModifyTeachplan(TeachplanVO vo);
}
