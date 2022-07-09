package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.content.dto.CourseBaseDTO;
import com.xuecheng.api.content.qo.QueryCourseBaseModel;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.util.StringUtil;
import com.xuecheng.content.convert.CourseBaseConvert;
import com.xuecheng.content.entity.CourseBase;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.service.CourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 课程基本信息 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {

    @Override
    public PageVO queryCourseBaseList(PageRequestParams params, QueryCourseBaseModel model,Long companyId) {
        //1.判断传入数据params是否不为空
        //1.1.判断查询是否合格
        if (params.getPageNo() < 1) {
            params.setPageNo(PageRequestParams.DEFAULT_PAGE_NUM);
        }
        //1.2.判断每页条数是否合格
        if (params.getPageSize() < 1) {
            params.setPageSize(PageRequestParams.DEFAULT_PAGE_SIZE);
        }
        //2.创建分页查询对象，设置当前页和每页条数
        Page<CourseBase> page = new Page<>(params.getPageNo(), params.getPageSize());
        //3.构建查询条件
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        //完整流程写法
//        //3.1. 条件一：课程名称 like
//        if (StringUtil.isNotBlank(model.getCourseName())) {
//            wrapper.like(CourseBase::getName, model.getCourseName());
//        }
//        //3.2.条件二：课程审核状态 equals.
//        if (StringUtil.isNotBlank(model.getAudiStatus())) {
//            wrapper.eq(CourseBase::getAuditStatus, model.getAudiStatus());
//        }
        //简化版写法
        //3.1. 条件一：课程名称 like
        wrapper.like(StringUtil.isNotBlank(model.getCourseName()),
                CourseBase::getName,
                model.getCourseName());
        //3.2.条件二：课程审核状态 equals.
        wrapper.eq(StringUtil.isNotBlank(model.getAudiStatus()),
                CourseBase::getAuditStatus,
                model.getAudiStatus());
        wrapper.eq(CourseBase::getCompanyId, companyId);

        //4.查询数据
        Page<CourseBase> pageResult = this.page(page, wrapper);
        //4.1.获取总条数
        long total = pageResult.getTotal();
        //4.2.获取当前页数据
        List<CourseBase> records = pageResult.getRecords();

        List<CourseBaseDTO> dtos = Collections.EMPTY_LIST;

        if (!(CollectionUtils.isEmpty(records))) {
            dtos = CourseBaseConvert.INSTANCE.entity2dtos(records);
        }
        //5.封装查询结果，PageVO
        PageVO pageVO = new PageVO(dtos, total, params.getPageNo(), params.getPageSize());
        //
        //6.返回数据
        return pageVO;
    }
}
