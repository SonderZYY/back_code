package com.xuecheng.content.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.content.model.dto.CourseTeacherDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.content.common.constant.ContentErrorCode;
import com.xuecheng.content.entity.CourseTeacher;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements CourseTeacherService {

    /**
     * 根据课程id 查询课程教师信息
     * 业务分析
     * 0.是否开启事务-不开启
     * 1.判断关键数据
     * 机构id
     * 课程id
     * 2.判断业务数据
     * 判断数据是否存在
     * 判断是否同一家机构
     * 判断课程基础信息是否删除
     * 3.根据id查询教师
     * 4.返回数据
     */
    @Override
    public List<CourseTeacherDTO> queryCourseTeacherList(Long companyId, Long courseBaseId) {
        //1.判断关键数据
        // 机构id
        // 课程id
        if (ObjectUtils.isEmpty(companyId) || ObjectUtils.isEmpty(courseBaseId)) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.判断业务数据
        // 判断数据是否存在
        // 判断是否同一家机构
        // 判断课程基础信息是否删除
        //3.根据id查询教师
        CourseTeacher courseTeacher = this.getById(courseBaseId);
        if (ObjectUtils.nullSafeEquals(courseTeacher.getCoursePubId(), companyId) || ObjectUtils.isEmpty(courseTeacher)) {
            ExceptionCast.cast(ContentErrorCode.E_1200503);
        }
        //4.返回数据

        return null;
    }
}
