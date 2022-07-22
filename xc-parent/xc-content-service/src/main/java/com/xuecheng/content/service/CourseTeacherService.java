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

    /**
     * 新增或修改教师信息
     * @param dto CourseTeacherDTO 前端接收的教师信息
     * @param companyId Long 机构id
     * @return CourseTeacherDTO 最新的课程教师信息DTO类型
     */
    CourseTeacherDTO createOrModifyCourseTeach(CourseTeacherDTO dto, Long companyId);

    /**
     * 根据Id删除教师信息
     * @param courseId Long 课程id
     * @param courseTeacherId Long 教师id
     * @param companyId Long 机构id
     */
    void removeCourseTeachById(Long courseId, Long courseTeacherId, Long companyId);
}
