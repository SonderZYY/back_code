package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.api.content.model.qo.QueryCourseBaseModel;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.enums.common.CommonEnum;
import com.xuecheng.common.enums.content.CourseAuditEnum;
import com.xuecheng.common.enums.content.CourseChargeEnum;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.StringUtil;
import com.xuecheng.content.common.constant.ContentErrorCode;
import com.xuecheng.content.controller.CourseAuditController;
import com.xuecheng.content.convert.CourseBaseConvert;
import com.xuecheng.content.entity.CourseBase;
import com.xuecheng.content.entity.CourseMarket;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.service.CourseMarketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Autowired
    private CourseMarketService courseMarketService;

    //TODO: 分页查询课程基础信息的方法
    public PageVO queryCourseBaseList(PageRequestParams params, QueryCourseBaseModel model, Long companyId) {
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
        ////3.1. 条件一：课程名称 like
        //if (StringUtil.isNotBlank(model.getCourseName())) {
        //    wrapper.like(CourseBase::getName, model.getCourseName());
        //}
        ////3.2.条件二：课程审核状态 equals.
        //if (StringUtil.isNotBlank(model.getAudiStatus())) {
        //    wrapper.eq(CourseBase::getAuditStatus, model.getAudiStatus());
        //}
        //简化版写法
        //3.1. 条件一：课程名称 like
        wrapper.like(StringUtil.isNotBlank(model.getCourseName()),
                CourseBase::getName,
                model.getCourseName());
        //3.2.条件二：课程审核状态 equals.
        wrapper.eq(StringUtil.isNotBlank(model.getAuditStatus()),
                CourseBase::getAuditStatus,
                model.getAuditStatus());
        if (!(ObjectUtils.nullSafeEquals(companyId, CourseAuditController.OPERATION_FLAG))) {
            wrapper.eq(CourseBase::getCompanyId, companyId);
        }
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

    /**
     * TODO:添加课程基础信息的方法
     * 业务分析
     * 1.判断是否与实务有关
     * 新增需要添加事务
     * 2.判断关键数据的正确性
     * 关键数据：前端传入的数据
     * 判断是否有值-必填项：可以通过前端页面，接口文档，数据库字段的属性查看
     * 错误数据的处理方式：抛出对应异常
     * 3.保存数据
     * 前端传入的数据
     * 后端自行维护的数据
     * 保存课程基本信息并验证数据的正确性
     * 保存课程营销数据并判断保存后的数据正确性
     * 4.返回数据
     * 将保存的最新数据返回前端
     */
    @Transactional
    public CourseBaseDTO creatCourseBase(CourseBaseDTO dto) {
        //2.判断关键数据的正确性
        // 关键数据：前端传入的数据
        // 判断是否有值-必填项：可以通过前端页面，接口文档，数据库字段的属性查看
        // 错误数据的处理方式：抛出对应异常
        judgeCourseBaseData(dto);
        //3.保存数据
        // 前端传入的数据
        CourseBase courseBase = CourseBaseConvert.INSTANCE.dto2entity(dto);
        // 后端自行维护的数据
        //status(数据库会自定义填充)  create_date  update_date（mp的自动填充功能来赋值）  auditStatus（添加课程时需要赋值为：未提交）
        //设置课程状态
        courseBase.setStatus(null);
        //添加课程状态
        courseBase.setAuditStatus(CourseAuditEnum.AUDIT_UNPAST_STATUS.getCode());
        //课程id自增，设置为null即可
        courseBase.setId(null);
        // 保存课程基本信息并验证数据的正确性
        boolean flag = this.save(courseBase);
        if (!flag) {
            throw new RuntimeException("课程基础信息保存失败");
        }
        // 保存课程营销数据并判断保存后的数据正确性
        CourseMarket courseMarket = new CourseMarket();
        // id自增不需要设置，设置课程id
        courseMarket.setCourseId(courseBase.getId());
        //设置课程收费规则
        courseMarket.setCharge(dto.getCharge());
        //判断收费规则
        if (CourseChargeEnum.CHARGE_YES.getCode().equals(dto.getCharge())) {
            courseMarket.setPrice(dto.getPrice().floatValue());
        }
        boolean marketResult = courseMarketService.save(courseMarket);
        if (!marketResult) {
            throw new RuntimeException("课程营销信息保存失败");
        }
        // 将保存的最新数据返回前端
        CourseBase po = this.getById(courseBase.getId());
        CourseBaseDTO resultDTO = CourseBaseConvert.INSTANCE.entity(po);
        resultDTO.setPrice(dto.getPrice());
        resultDTO.setCharge(dto.getCharge());
        return resultDTO;
    }

    /**
     * TODO: 数据回显需要修改的课程基础信息
     * 业务分析：
     * 修改信息之前查询的必要性：
     * 1.确保数据的安全性
     * 2.用于前端数据回显
     * 0.查询不需要事务支持
     * 1.数据的校验，判断前端传入的数据是否存在
     * courseBaseId，companyId
     * 2.判断业务数据：根据关键数据来判断需要操作的数据
     * 1.判断课程信息是否存在
     * 2.判断查询的课程信息和传入的机构id是否相同
     * 3.判断课程信息是否删除
     * 3.回显数据
     */
    public CourseBaseDTO getCourseBaseById(Long courseBaseId, Long companyId) {

        //1.数据的校验，判断前端传入的数据是否存在
        //    courseBaseId，companyId
        if (ObjectUtils.isEmpty(courseBaseId) || ObjectUtils.isEmpty(companyId)) {
            throw new RuntimeException("传入的参数不符合要求");
        }
        //2.判断业务数据：根据关键数据来判断需要操作的数据
        //    1.判断课程信息是否存在
        //    2.判断查询的课程信息和传入的机构id是否相同
        //    3.判断课程信息是否删除
        CourseBase courseBase = getCourseBaseByBaseId(courseBaseId, companyId);
        //3.回显数据
        //1.将CourseBase转为CourseBaseDTO
        CourseBaseDTO courseBaseDTO = CourseBaseConvert.INSTANCE.entity(courseBase);
        //2.查询课程营销的数据封装到CourseBaseDTO
        CourseMarket courseMarket = getCourseMarketByCourseId(courseBaseId);
        courseBaseDTO.setCharge(courseMarket.getCharge());
        courseBaseDTO.setPrice(new BigDecimal(courseMarket.getPrice().toString()));
        return courseBaseDTO;
    }

    /**
     * TODO：根据id修改课程基础信息
     * 业务流程分析：
     *  0.是否需要添加事务 需要添加事务
     *  1.判断传入数据的是否存在
     *      特别注意：需要判断课程id是否为空，为空则直接抛出异常
     *  2.判断业务数据
     *      课程基础信息
     *          课程id
     *          机构id
     *          课程是否删除
     *          课程审核状态
     *      课程营销数据
     *          判断是否存在
     *  3.修改数据
     *      先修改课程基本信息
     *      后修改课程营销信息
     *  4.返回修改后的最新数据
     */
    @Transactional
    public CourseBaseDTO modifyCourseBaseById(CourseBaseDTO dto) {
        //1.判断传入数据的是否存在
        //特别注意：需要判断课程id是否为空，为空则直接抛出异常
        judgeCourseBaseData(dto);
        if (ObjectUtils.isEmpty(dto.getCourseBaseId())) {
            throw new RuntimeException("课程id不能为空");
        }
        //2.判断业务数据
        //课程基础信息
        //课程id、机构id、课程是否删除、课程审核状态
        getCourseBaseByLogic(dto.getCourseBaseId(), dto.getCompanyId());
        //课程营销数据
        //判断是否存在
        CourseMarket courseMarket = getCourseMarketByCourseId(dto.getCourseBaseId());
        //3.修改数据
        //先修改课程基本信息
        LambdaUpdateWrapper<CourseBase> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CourseBase::getName, dto.getName());
        updateWrapper.set(CourseBase::getTags, dto.getTags());
        updateWrapper.set(CourseBase::getMt, dto.getMt());
        updateWrapper.set(CourseBase::getSt, dto.getSt());
        updateWrapper.set(CourseBase::getDescription, dto.getDescription());
        updateWrapper.set(CourseBase::getUsers, dto.getUsers());
        updateWrapper.set(CourseBase::getPic, dto.getPic());
        //无法自动生成，自行设置
        updateWrapper.set(CourseBase::getChangeDate, LocalDateTime.now());
        //where条件
        updateWrapper.eq(CourseBase::getId, dto.getCourseBaseId());
        boolean result = this.update(updateWrapper);
        if (!result) {
            ExceptionCast.cast(ContentErrorCode.E_120001);
        }
        //后修改课程营销信息
        LambdaUpdateWrapper<CourseMarket> marketWrapper = new LambdaUpdateWrapper<>();
        marketWrapper.set(CourseMarket::getCharge, dto.getCharge());
        //根据Charge的状态码判断课程是否收费
        String charge = dto.getCharge();
        if (CourseChargeEnum.CHARGE_YES.getCode().equals(charge)) {
            marketWrapper.set(CourseMarket::getPrice, dto.getPrice());
        } else {
            marketWrapper.set(CourseMarket::getPrice, 0F);
        }
        marketWrapper.eq(CourseMarket::getCourseId, dto.getCourseBaseId());
        boolean marketResult = courseMarketService.update(marketWrapper);
        if (!marketResult) {
            ExceptionCast.cast(ContentErrorCode.E_120107);
        }
        //查询最新的数据
        CourseBase courseBase = this.getById(dto.getCourseBaseId());
        //封装数据
        CourseBaseDTO courseBaseDTO = CourseBaseConvert.INSTANCE.entity(courseBase);
        courseBaseDTO.setCharge(dto.getCharge());
        courseBaseDTO.setPrice(dto.getPrice());
        //4.返回修改后的最新数据
        return courseBaseDTO;
    }

    /**
     * TODO:根据id删除课程基础信息
     * 业务分析：
     * 1.事务需要开启事务 需要开启事务
     * 2.数据检验：课程id,机构id
     * 3.业务数据检验
     * 机构id,课程状态,判断课程是否已经删除
     * 4.删除课程基本信息
     * 删除课程营销信息
     */
    @Transactional
    public void removeCourseById(Long courseBaseId, Long companyId) {
        //2.数据检验：课程id,机构id
        if (ObjectUtils.isEmpty(courseBaseId) || ObjectUtils.isEmpty(companyId)) {
            ExceptionCast.cast(ContentErrorCode.E_120009);
        }
        //3.业务数据检验
        //机构id,课程状态,判断课程是否已经删除
        getCourseBaseByLogic(courseBaseId, companyId);
        //4.删除课程基本信息
        LambdaUpdateWrapper<CourseBase> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CourseBase::getStatus, CommonEnum.DELETE_FLAG.getCode());
        wrapper.set(CourseBase::getChangeDate, LocalDateTime.now());
        //where条件
        wrapper.eq(CourseBase::getId, courseBaseId);
        //删除课程营销信息
        boolean deleteResult = update(wrapper);
        if (!deleteResult) {
            ExceptionCast.cast(ContentErrorCode.E_120012);
        }

    }

    /**
     * TODO：课程提交审核功能
     * <p>
     * 业务分析：
     * 0.是否开启事务—开启
     * 1.判断关键数据
     * 机构id，课程id
     * 2.判断业务数据
     * 是否同一家机构
     * 课程状态是否为未提交，审核未通过
     * 课程是否存在
     * 课程是否删除
     * 3.修改审核状态
     */
    @Transactional
    public void commitCourseBase(Long courseBaseId, Long companyId) {
        //1.判断关键数据
        //  机构id，课程id
        if (ObjectUtils.isEmpty(courseBaseId) || ObjectUtils.isEmpty(companyId)) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.判断业务数据
        // 是否同一家机构
        // 课程状态是否为未提交，审核未通过
        // 课程是否存在
        // 课程是否删除
        getCourseBaseByLogic(courseBaseId, companyId);
        //3.修改审核状态
        LambdaUpdateWrapper<CourseBase> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(CourseBase::getAuditStatus, CourseAuditEnum.AUDIT_COMMIT_STATUS.getCode());
        wrapper.set(CourseBase::getChangeDate, LocalDateTime.now());
        wrapper.eq(CourseBase::getId, courseBaseId);
        boolean updateResult = this.update(wrapper);
        if (!updateResult) {
            ExceptionCast.cast(ContentErrorCode.E_120017);
        }
    }

    /**
     * TODO:课程审核功能实现
     * <p>
     * 业务分析
     * 0.判断事务—开启
     * 1.判断关键数据
     * auditMind—
     * auditStatus—课程状态
     * courseId—课程id
     * 2.判断业务数据
     * 课程基础信息
     * 判断是否存在
     * 判断是否删除
     * 判断审核状态—已提交
     * 审核状态修改
     * 运营平台只能给课程审核状态赋值：审核通过、不通过
     * 3.修改课程审核信息
     * auditMind、auditStatus、auditNum
     */
    @Transactional
    public void approve(CourseBaseDTO dto) {
        //1.判断关键数据
        // auditMind—
        // auditStatus—课程状态
        // courseId—课程id
        if (StringUtil.isBlank(dto.getAuditMind())
                || StringUtil.isBlank(dto.getAuditStatus())
                || ObjectUtils.isEmpty(dto.getCourseBaseId())) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.判断业务数据
        // 课程基础信息
        //     判断是否存在
        //     判断是否删除
        //     判断审核状态—已提交
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseBase::getId, dto.getCourseBaseId());
        wrapper.eq(CourseBase::getStatus, CommonEnum.USING_FLAG.getCode());
        wrapper.eq(CourseBase::getAuditStatus, CourseAuditEnum.AUDIT_COMMIT_STATUS);
        int count = this.count(wrapper);
        if (count < 1) {
            ExceptionCast.cast(ContentErrorCode.E_120023);
        }
        // 审核状态修改
        //     运营平台只能给课程审核状态赋值：审核通过、不通过
        String auditStatus = dto.getAuditStatus();
        if (CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode().equals(auditStatus)
                || CourseAuditEnum.AUDIT_COMMIT_STATUS.getCode().equals(auditStatus)
                || CourseAuditEnum.AUDIT_UNPAST_STATUS.getCode().equals(auditStatus)) {
            ExceptionCast.cast(ContentErrorCode.E_120016);
        }
        //3.修改课程审核信息
        // auditMind、auditStatus、auditNum
        LambdaUpdateWrapper<CourseBase> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(CourseBase::getAuditStatus, dto.getAuditStatus());
        updateWrapper.set(CourseBase::getAuditMind, dto.getAuditMind());
        updateWrapper.setSql("audit_nums = audit_nums+1");
        updateWrapper.eq(CourseBase::getId, dto.getCourseBaseId());
        boolean updateResult = this.update(updateWrapper);
        if (!updateResult) {
            ExceptionCast.cast(ContentErrorCode.E_120017);
        }
    }

    /**
     * 课程id、机构id、
     * 课程是否删除、课程审核状态 的数据是否符合条件
     */
    private void getCourseBaseByLogic(Long courseBaseId, Long companyId) {
        CourseBase courseBase = getCourseBaseByBaseId(courseBaseId, companyId);
        String auditStatus = courseBase.getAuditStatus();
        if (CourseAuditEnum.AUDIT_COMMIT_STATUS.getCode().equals(auditStatus)
                || CourseAuditEnum.AUDIT_PASTED_STATUS.getCode().equals(auditStatus)
                || CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode().equals(auditStatus)) {
            ExceptionCast.cast(ContentErrorCode.E_120015);
        }
    }

    /**
     * TODO:查询课程营销表的数据
     **/
    private CourseMarket getCourseMarketByCourseId(Long courseBaseId) {
        LambdaQueryWrapper<CourseMarket> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseMarket::getId, courseBaseId);
        CourseMarket courseMarket = courseMarketService.getOne(wrapper);
        if (ObjectUtils.isEmpty(courseMarket)) {
            ExceptionCast.cast(ContentErrorCode.E_120109);
        }
        return courseMarket;
    }

    /**
     * 判断业务数据的方法：根据传入的课程id和公司id判断
     * 1.判断课程信息是否存在
     * 2.判断查询的课程信息和传入的机构id是否相同
     * 3.判断课程信息是否删除
     */
    private CourseBase getCourseBaseByBaseId(Long courseBaseId, Long companyId) {
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        //1.判断课程信息是否存在
        wrapper.eq(CourseBase::getId, courseBaseId);
        //2.判断查询的课程信息和传入的机构id是否相同
        wrapper.eq(CourseBase::getCompanyId, companyId);
        CourseBase courseBase = this.getOne(wrapper);
        if (ObjectUtils.isEmpty(courseBase)) {
            ExceptionCast.cast(ContentErrorCode.E_120013);
        }
        //3.判断课程信息是否未删除
        Integer status = courseBase.getStatus();
        if (!(CommonEnum.USING_FLAG.getCodeInt().equals(status))) {
            ExceptionCast.cast(ContentErrorCode.E_120021);
        }
        return courseBase;
    }

    /**
     * TODO:判断传入的数据方法
     */
    private void judgeCourseBaseData(CourseBaseDTO dto) {
        //判断公司id是否为空
        if (ObjectUtils.isEmpty(dto.getCompanyId())) {
            ExceptionCast.cast(ContentErrorCode.E_120013);
        }
        //判断课程名是否为空
        if (StringUtil.isBlank(dto.getName())) {
            ExceptionCast.cast(ContentErrorCode.E_120004);
        }
        //判断课程大分类是否为空
        if (StringUtil.isBlank(dto.getMt())) {
            ExceptionCast.cast(ContentErrorCode.E_120002);
        }
        //判断课程小分类是否为空
        if (StringUtil.isBlank(dto.getSt())) {
            ExceptionCast.cast(ContentErrorCode.E_120003);
        }
        //判断适用人群是否为空
        if (StringUtil.isBlank(dto.getUsers())) {
            ExceptionCast.cast(ContentErrorCode.E_120019);
        }
        //判断课程收费是否为空
        if (StringUtil.isBlank(dto.getCharge())) {
            ExceptionCast.cast(ContentErrorCode.E_120020);
        }
        //判断收费课程的价格是否为空
        String charge = dto.getCharge();
        if (CourseChargeEnum.CHARGE_YES.getCode().equals(charge)) {
            if (ObjectUtils.isEmpty(dto.getPrice())) {
                ExceptionCast.cast(ContentErrorCode.E_120024);
            }
        } else {
            dto.setPrice(new BigDecimal(0));
        }
    }
}
