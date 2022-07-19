package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.api.content.model.qo.QueryCourseBaseModel;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.content.entity.CourseBase;

/**
 * <p>
 * 课程基本信息 服务类
 * </p>
 *
 * @author itcast
 * @since 2022-07-07
 */
public interface CourseBaseService extends IService<CourseBase> {

    /**
     * 课程基础信息分页条件查询
     *
     * @param params PageRequestParams 分页数据
     * @param model  QueryCourseBaseModel 查询条件

     * @return PageVO 分页查询条件结果封装对象
     */
    PageVO queryCourseBaseList(PageRequestParams params, QueryCourseBaseModel model, Long companyId);

    /**
     * 课程基础信息的添加
     * @param dto CourseBaseDTO 前端传入的vo转换为dto
     * @return CourseBaseDTO
     */
    CourseBaseDTO creatCourseBase(CourseBaseDTO dto);

    /**
     * 根据id获取课程基本信息
     * @param courseBaseId Long 课程id值
     * @param companyId Long 公司标识id
     * @return CourseBaseDTO
     */
    CourseBaseDTO getCourseBaseById(Long courseBaseId, Long companyId);

    /**
     * 根据id修改课程基础信息
     * @param dto CourseBaseDTO 封装后包含机构id的数据
     * @return CourseBaseDTO
     */
    CourseBaseDTO modifyCourseBaseById(CourseBaseDTO dto);

    /**
     * 根据id删除课程基础信息
     * @param courseBaseId 课程Id
     */
    void removeCourseById(Long courseBaseId,Long companyId);

    /**
     * 根据课程id提交课程审核方法
     * @param courseBaseId 课程id
     * @param companyId 机构id
     */
    void commitCourseBase(Long courseBaseId, Long companyId);

    /**
     * 课程审核业务实现
     * @param dto CourseBaseDTO
     */
    void approve(CourseBaseDTO dto);
}
