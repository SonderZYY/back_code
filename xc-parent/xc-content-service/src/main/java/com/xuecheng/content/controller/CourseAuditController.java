package com.xuecheng.content.controller;

import com.xuecheng.api.content.CourseAuditApi;
import com.xuecheng.content.mapper.dto.CourseBaseDTO;
import com.xuecheng.api.content.model.qo.QueryCourseBaseModel;
import com.xuecheng.api.content.model.vo.CourseAuditVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.content.convert.CourseBaseConvert;
import com.xuecheng.content.service.CourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CourseAuditController implements CourseAuditApi {
    //添加运营平台的查询表示
    public static final Long OPERATION_FLAG = -96949392L;

    @Autowired
    private CourseBaseService courseBaseService;

    @PostMapping("/m/course/list")
    public PageVO queryCourseList(PageRequestParams params, @RequestBody QueryCourseBaseModel model) {
        return courseBaseService.queryCourseBaseList(params, model, OPERATION_FLAG);
    }

    @PostMapping("/courseReview/approve")
    public void approveCourseBase(@RequestBody CourseAuditVO courseAuditVO) {
        CourseBaseDTO dto = CourseBaseConvert.INSTANCE.audit2dto(courseAuditVO);
        courseBaseService.approve(dto);
    }
}
