package com.xuecheng.content.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.api.content.model.dto.CourseTeacherDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.StringUtil;
import com.xuecheng.content.common.constant.ContentErrorCode;
import com.xuecheng.content.convert.CourseTeacherConvert;
import com.xuecheng.content.entity.CourseTeacher;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private CourseBaseService courseBaseService;

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
        //2.判断业务数据
        // 判断数据是否存在
        // 判断是否同一家机构
        // 判断课程基础信息是否删除
        CourseBaseDTO courseBaseDTO = courseBaseService.getCourseBaseById(courseBaseId, companyId);
        //3.根据id查询教师
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId, courseBaseId);
        List<CourseTeacher> list = this.list(queryWrapper);
        //4.返回数据
        List<CourseTeacherDTO> dtoList = CourseTeacherConvert.INSTANCE.entitys2dtos(list);
        return dtoList;
    }

    /**
     * 主业务分析：
     * 1.是否开启事务（开启）
     * 2.判断是添加还是修改操作（有courseTeachId为修改，没有为添加操作）
     * 3.操作数据库
     * 4.查询最新数据返回前端
     */
    @Transactional
    public CourseTeacherDTO createOrModifyCourseTeach(CourseTeacherDTO dto, Long companyId) {
        //判断是添加还是修改操作（有courseTeachId为修改，没有为添加操作）
        //操作数据库
        if (ObjectUtils.isEmpty(dto.getCourseTeacherId())) {
            //添加操作
            CourseTeacher courseTeacher = createCourseTeach(dto, companyId);
            dto.setCourseTeacherId(courseTeacher.getId());
        } else {
            //修改操作
            modifyCourseTeach(dto, companyId);
        }
        //查询最新数据返回前端
        CourseTeacher courseTeacher = this.getById(dto.getCourseTeacherId());
        //po数据转化为dto数据
        CourseTeacherDTO courseTeacherDTO = CourseTeacherConvert.INSTANCE.entity2dto(courseTeacher);
        return courseTeacherDTO;
    }

    /**
     * 业务分析：
     * 1.判断关键数据
     * courseId、photograph、position、teacherName、companyId
     * 2.判断业务数据
     * 判断课程是否存在
     * 判断课程是否删除
     * 判断是否同一家机构
     * 判断审核状态为未提交和审核不通过才可以添加课程教师信息
     * 判断教师数据是否存在
     * 3.修改教师信息（只修改前端显示的字段才可以操作）
     */
    private void modifyCourseTeach(CourseTeacherDTO dto, Long companyId) {
        //1.判断关键数据
        //    courseId、photograph、position、teacherName、companyId
        if (ObjectUtils.isEmpty(dto.getCourseId()) ||
                StringUtil.isBlank(dto.getPhotograph()) ||
                StringUtil.isBlank(dto.getPosition()) ||
                StringUtil.isBlank(dto.getTeacherName()) ||
                ObjectUtils.isEmpty(companyId)
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.判断业务数据
        //    判断课程是否存在
        //    判断课程是否删除
        //    判断是否同一家机构
        //    判断审核状态为未提交和审核不通过才可以添加课程教师信息
        CourseBaseServiceImpl courseBaseService = (CourseBaseServiceImpl) this.courseBaseService;
        courseBaseService.getCourseBaseByLogic(companyId, dto.getCourseId());
        //    判断教师数据是否存在
        CourseTeacher courseTeacher = this.getById(dto.getCourseTeacherId());
        if (ObjectUtils.isEmpty(courseTeacher)) {
            ExceptionCast.cast(ContentErrorCode.E_1200503);
        }
        //3.修改教师信息（只修改前端显示的字段才可以操作）
        LambdaUpdateWrapper<CourseTeacher> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CourseTeacher::getCourseId, dto.getCourseId());
        updateWrapper.set(CourseTeacher::getCoursePubId, dto.getCoursePubId());
        updateWrapper.set(CourseTeacher::getCreateDate, dto.getCreateDate());
        updateWrapper.set(CourseTeacher::getIntroduction, dto.getIntroduction());
        updateWrapper.set(CourseTeacher::getPhotograph, dto.getPhotograph());
        updateWrapper.set(CourseTeacher::getPosition, dto.getPosition());
        updateWrapper.set(CourseTeacher::getTeacherName, dto.getTeacherName());
        updateWrapper.eq(CourseTeacher::getId, dto.getCourseTeacherId());
        boolean update = this.update(updateWrapper);
        if (!(update)) {
            ExceptionCast.cast(ContentErrorCode.E_1200501);
        }

    }

    /**
     * 业务分析：
     * 1.判断关键数据
     * courseId、photograph、position、teacherName、companyId
     * 2.判断业务数据
     * 判断课程是否存在
     * 判断课程是否删除
     * 判断是否同一家机构
     * 判断审核状态为未提交和审核不通过才可以添加课程教师信息
     * 3.添加教师信息
     * 4.返回添加结果
     */
    private CourseTeacher createCourseTeach(CourseTeacherDTO dto, Long companyId) {
        //1.判断关键数据
        //    courseId、photograph、position、teacherName、companyId
        if (ObjectUtils.isEmpty(dto.getCourseId()) ||
                StringUtil.isBlank(dto.getPhotograph()) ||
                StringUtil.isBlank(dto.getPosition()) ||
                StringUtil.isBlank(dto.getTeacherName()) ||
                ObjectUtils.isEmpty(companyId)
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.判断业务数据
        //    判断课程是否存在
        //    判断课程是否删除
        //    判断是否同一家机构
        //    判断审核状态为未提交和审核不通过才可以添加课程教师信息
        CourseBaseServiceImpl courseBaseService = (CourseBaseServiceImpl) this.courseBaseService;
        courseBaseService.getCourseBaseByLogic(companyId, dto.getCourseId());
        //3.添加教师信息
        CourseTeacher courseTeacher = CourseTeacher.builder()
                .courseId(dto.getCourseId())
                .coursePubId(dto.getCoursePubId())
                .createDate(dto.getCreateDate())
                .introduction(dto.getIntroduction())
                .photograph(dto.getPhotograph())
                .position(dto.getPosition())
                .teacherName(dto.getTeacherName())
                .build();
        boolean save = this.save(courseTeacher);
        if (!(save)) {
            ExceptionCast.cast(ContentErrorCode.E_1200501);
        }
        return courseTeacher;
    }

    /**
     * 业务分析：
     *  0.是否开启事务（开启）
     *  1.判断关键数据
     *      courseId、courseTeacherId、companyId
     *  2.判断业务数据
     *      判断教师信息是否存在
     *  3.删除课程教师信息
     */
    @Transactional
    public void removeCourseTeachById(Long courseId, Long courseTeacherId, Long companyId) {
        //1.判断关键数据
        //    courseId、courseTeacherId、companyId
        if(ObjectUtils.isEmpty(courseId)||
                ObjectUtils.isEmpty(courseTeacherId)||
                ObjectUtils.isEmpty(companyId)
        ){
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.判断业务数据
        //    判断教师信息是否存在
        CourseTeacher courseTeacher = this.getById(courseTeacherId);
        if (ObjectUtils.isEmpty(courseTeacher)){
            ExceptionCast.cast(ContentErrorCode.E_1200503);
        }
        //3.删除课程教师信息
        boolean result = this.removeById(courseTeacherId);
        if (!(result)){
            ExceptionCast.cast(ContentErrorCode.E_1200501);
        }
    }
}
