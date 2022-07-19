package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.api.content.model.dto.CourseTeacherDTO;
import com.xuecheng.content.entity.CourseTeacher;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务类
 * </p>
 *
 * @author itcast
 * @since 2022-07-19
 */
public interface CourseTeacherService extends IService<CourseTeacher> {
    /**
     * 根据课程id查询课程教师基础信息
     * @param companyId 机构id
     * @param courseBaseId 课程id
     * @return List<CourseTeacherDTO>
     */
    List<CourseTeacherDTO> queryCourseTeacherList(Long companyId, Long courseBaseId);
}
