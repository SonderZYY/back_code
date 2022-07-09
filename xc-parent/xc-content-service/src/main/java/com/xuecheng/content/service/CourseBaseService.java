package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.api.content.qo.QueryCourseBaseModel;
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
     * @param companyId Long 教学机构id值
     * @return PageVO 分页查询条件结果封装对象
     */
    PageVO queryCourseBaseList(PageRequestParams params, QueryCourseBaseModel model, Long companyId);
}
