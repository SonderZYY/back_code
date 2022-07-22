package com.xuecheng.content.controller;

import com.xuecheng.api.content.CourseTeacherApi;
import com.xuecheng.api.content.model.dto.CourseTeacherDTO;
import com.xuecheng.api.content.model.vo.CourseTeacherVO;
import com.xuecheng.common.util.SecurityUtil;
import com.xuecheng.content.convert.CourseTeacherConvert;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("courseTeacher")
    public CourseTeacherDTO createOrModifyCourseTeach(@RequestBody CourseTeacherVO courseTeacherVO) {
        //1. 获得公司Id
        Long companyId = SecurityUtil.getCompanyId();
        //2.VO类转换成DTO类传给业务层
        CourseTeacherDTO dto = CourseTeacherConvert.INSTANCE.vo2dto(courseTeacherVO);
        return courseTeacherService.createOrModifyCourseTeach(dto, companyId);
    }

    @DeleteMapping("courseTeacher/course/{courseId}/{courseTeacherId}")
    public void removeCourseTeachById(@PathVariable Long courseId, @PathVariable Long courseTeacherId) {
        //1. 获得公司Id
        Long companyId = SecurityUtil.getCompanyId();
        courseTeacherService.removeCourseTeachById(courseId,courseTeacherId,companyId);
    }
}
