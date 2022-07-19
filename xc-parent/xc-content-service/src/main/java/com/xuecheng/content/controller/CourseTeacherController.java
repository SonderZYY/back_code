package com.xuecheng.content.controller;

import com.xuecheng.api.content.CourseTeacherApi;
import com.xuecheng.api.content.model.dto.CourseTeacherDTO;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.util.SecurityUtil;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
public class CourseTeacherController implements CourseTeacherApi {

    @Autowired
    private CourseTeacherService courseTeacherService;

    @GetMapping("courseTeacher/list/{courseBaseId}")
    public List<CourseTeacherDTO> queryCourseTeacherList(@PathVariable Long courseBaseId) {
        Long companyId = SecurityUtil.getCompanyId();
        return courseTeacherService.queryCourseTeacherList(companyId, courseBaseId);

    }
}
