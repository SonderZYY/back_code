package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.api.content.model.dto.TeachplanDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.enums.content.CourseAuditEnum;
import com.xuecheng.common.enums.content.TeachPlanEnum;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.StringUtil;
import com.xuecheng.content.common.constant.ContentErrorCode;
import com.xuecheng.content.convert.TeachplanConvert;
import com.xuecheng.content.entity.Teachplan;
import com.xuecheng.content.entity.TeachplanMedia;
import com.xuecheng.content.entity.ex.TeachplanNode;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.service.TeachplanMediaService;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService {

    @Autowired
    private CourseBaseService courseBaseService;

    @Autowired
    private TeachplanMediaService mediaService;

    /**
     * 根据课程Id查询课程计划树形结构(树形结构为三级目录)
     * 业务分析
     * 1.判断是否需要添加事务 0
     * 2.判断关键数据
     * 课程id,机构标识id
     * 3.判断业务数据
     * 判断是否存在
     * 判断是否删除
     * 判断是否同一家机构
     * 4.根据courseId 查询课程计划
     * 5.通过java中的递归来生产课程计划的树形结构
     * 6.转换为DTO类型的数据返回
     */
    public TeachplanDTO queryTreeNodesByCourseId(Long courseId, Long companyId) {
        //2.判断关键数据
        // 课程id,机构标识id
        //3.判断业务数据
        // 判断是否存在
        // 判断是否删除
        // 判断是否同一家机构
        CourseBaseDTO courseBase = courseBaseService.getCourseBaseById(courseId, companyId);
        //4.根据courseId 查询课程计划
        TeachplanMapper teachplanMapper = this.getBaseMapper();
        List<TeachplanNode> nodes = teachplanMapper.selectTreeNodesByCourseId(courseId);
        TeachplanDTO resultDTO = null;
        //5.通过java中的递归来生成课程计划的树形结构
        if (CollectionUtils.isEmpty(nodes)) {
            resultDTO = new TeachplanDTO();
        } else {
            TeachplanNode rootNode = nodes.remove(0);
            generateTreeNodes(rootNode, nodes);
            resultDTO = TeachplanConvert.INSTANCE.node2dto(rootNode);
        }
        //6.转换为DTO类型的数据返回
        return resultDTO;
    }

    /*生成课程计划的树形结构*/
    private void generateTreeNodes(TeachplanNode parentNode, List<TeachplanNode> nodes) {
        //判断父级的结构是否为空
        if (CollectionUtils.isEmpty(parentNode.getChildrenNodes())) {
            parentNode.setChildrenNodes(new ArrayList<>());
        }
        //2.遍历nodes生产树结构并递归
        for (TeachplanNode node : nodes) {
            //条件满足，需要将子级的数据放到父级的集合数据中
            //子级的parentId=父级.id
            if (ObjectUtils.nullSafeEquals(parentNode.getId(), node.getParentid())) {
                parentNode.getChildrenNodes().add(node);
                //出口：如果课程为等级为三级则不递归
                if (!(TeachPlanEnum.THIRD_LEVEL.equals(node.getGrade()))) {
                    generateTreeNodes(node, nodes);
                }
            }
        }
    }

    /**
     * 主方法：课程计划的添加和修改
     * 业务分析
     * 0.判断是否需要开启事务 需要开启
     * 分离的添加和修改的方法不需要重复添加事务，因为事务具有继承性
     * 1.判断业务数据
     * 判断teachId是否为空
     * 如果为空：执行课程假话添加方法
     * 如果不为空：执行修改课程计划的方法
     * 2.执行方法并返回结果DTO
     */
    @Transactional
    public TeachplanDTO creatOrModifyTeachplan(TeachplanDTO dto, Long companyId) {
        //1.判断业务数据
        //    判断teachId是否为空
        Long teachPlanId = dto.getTeachPlanId();
        TeachplanDTO resultDTO = null;
        if (ObjectUtils.isEmpty(teachPlanId)) {
            //如果为空：执行课程假话添加方法
            resultDTO = creatTeachplan(dto, companyId);
        } else {
            //如果不为空：执行修改课程计划的方法
            resultDTO = modifyTeachplan(dto, companyId);
        }
        //2.返回结果DTO
        return resultDTO;
    }

    /**
     * 课程计划修改方法
     * 业务分析
     * 1.判断关键数据
     * 课程id
     * 课程计划名称
     * 父级id
     * 是否免费
     * 教学机构id
     * 2.判断业务数据
     * 课程基础信息
     * 判断是否存在
     * 判断是否删除
     * 判断是否同一家机构
     * 判断审核状态：未提交，审核未通过 符合条件
     * 课程计划
     * 判断是否存在
     * 判断等级：教学机构只能操作二级和三级课程计划
     * 3.将dto转换为po并保存
     * 为了数据的安全性，只让前端修改的字段
     * 课程计划名称、是否免费、开始时间、结束时间、mediatype
     * 4.将数据库最新的数据返回给前端（dto类型）
     */
    private TeachplanDTO modifyTeachplan(TeachplanDTO dto, Long companyId) {
        //1.判断关键数据,课程id,课程计划名称,父级id,是否免费,教学机构id
        //2.判断业务数据
        // 课程基础信息
        //判断是否存在,判断是否删除,判断是否同一家机构,判断审核状态：未提交，审核未通过 符合条件
        if (ObjectUtils.isEmpty(dto.getParentid())
                || StringUtil.isBlank(dto.getIsPreview())) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        CourseBaseDTO courseBase = verifyTeachplanMsg(dto, companyId);
        // 课程计划
        // 判断是否存在
        // 判断等级：教学机构只能操作二级和三级课程计划
        Teachplan teachplan = this.getById(dto.getTeachPlanId());
        if (ObjectUtils.isEmpty(teachplan)) {
            ExceptionCast.cast(ContentErrorCode.E_120402);
        }

        Long parentid = teachplan.getParentid();
        if (TeachPlanEnum.FIRST_PARENTID_FLAG.equals(parentid)) {
            ExceptionCast.cast(ContentErrorCode.E_120417);
        }
        //3.将dto转换为po并保存
        // 为了数据的安全性，只让前端修改的字段
        // 课程计划名称、是否免费、开始时间、结束时间
        LambdaUpdateWrapper<Teachplan> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Teachplan::getPname, dto.getPname());
        wrapper.set(Teachplan::getIsPreview, dto.getIsPreview());
        wrapper.set(Teachplan::getStartTime, dto.getStartTime());
        wrapper.set(Teachplan::getEndTime, dto.getEndTime());
        wrapper.set(Teachplan::getChangeDate, LocalDateTime.now());
        wrapper.eq(Teachplan::getId, dto.getTeachPlanId());
        boolean result = this.update(wrapper);
        if (!result) {
            ExceptionCast.cast(ContentErrorCode.E_120407);
        }
        //4.将数据库最新的数据返回给前端（dto类型）
        Teachplan po = this.getById(dto.getTeachPlanId());
        return TeachplanConvert.INSTANCE.entity2dto(po);
    }

    /**
     * 课程计划添加方法
     */
    private TeachplanDTO creatTeachplan(TeachplanDTO dto, Long companyId) {
        //1.判断关键数据
        //   companyId  pname（课程计划的名称） courseId
        if (ObjectUtils.isEmpty(companyId)||
                StringUtil.isBlank(dto.getPname())||
                ObjectUtils.isEmpty(dto.getCourseId())
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        // 2.判断业务数据
        //   课程基础信息
        //      判断是否存在
        //      判断是否是同一家教学机构
        //      判断是否删除
        //      判断课程审核状态
        //          未提交和审核未通过才可以编辑课程计划
        CourseBaseDTO courseBase = courseBaseService.getCourseBaseById(dto.getCourseId(), companyId);

        String auditStatus = courseBase.getAuditStatus();
        if (CourseAuditEnum.AUDIT_PASTED_STATUS.getCode().equals(auditStatus)||
                CourseAuditEnum.AUDIT_COMMIT_STATUS.getCode().equals(auditStatus)||
                CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode().equals(auditStatus)
        ) {
            ExceptionCast.cast(ContentErrorCode.E_120015);
        }
        // 3.获得添加课程计划的父级数据
        //     添加课程计划的parentid = 父级.id
        //     添加课程计划的grade = 父级.grade+1
        //     添加课程计划的orderby = 父级的子集数据个数+1  ： select count(*) from  teachplan where parentid = ?
        Teachplan parentNode = generateParentNode(dto,courseBase);
        dto.setParentid(parentNode.getId());
        dto.setGrade(parentNode.getGrade()+1);

        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getParentid,parentNode.getId());

        int count = this.count(queryWrapper);
        dto.setOrderby(count+1);
        // 4.保存课程计划信息
        Teachplan teachplan = TeachplanConvert.INSTANCE.dto2entity(dto);

        boolean result = this.save(teachplan);

        if (!result) {
            ExceptionCast.cast(ContentErrorCode.E_120407);
        }
        // 5.返回数据库最新数据dto
        Teachplan po = this.getById(teachplan.getId());
        TeachplanDTO resultDTO = TeachplanConvert.INSTANCE.entity2dto(po);
        return resultDTO;
    }

    /**
     *根据课程id删除课程计划信息
     * 业务分析：
     *  判断关键数据信息：
     *      课程id
     *      机构id
     * 判断业务数据
     *      判断课程基本信息是否存在
     */
    public void removeTeachPlan(Long teachPlanId, Long companyId) {
        //1.判断关键数据的合法性
        if (ObjectUtils.isEmpty(teachPlanId)||
                ObjectUtils.isEmpty(companyId)
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }

        Teachplan teachplan = getById(teachPlanId);
        if (ObjectUtils.isEmpty(teachplan)) ExceptionCast.cast(ContentErrorCode.E_120402);



        //1.2 判断课程基本信息是否存在
        CourseBaseDTO courseBase = getCourseAndVerify(companyId, teachplan.getCourseId());


        // 2. 根据课程计划等级进行业务判断
        if (teachplan.getGrade() == TeachPlanEnum.SECEND_LEVEL) {
            // 判断二级课程计划是否有子级课程计划信息
            // select count(*) from teachplan where parentid = ?
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getParentid, teachPlanId);
            int count = count(queryWrapper);
            if (count > 0)
                ExceptionCast.cast(ContentErrorCode.E_120409);
        } else {
            // 判断三级课程计划是否关联课程媒资信息
            LambdaQueryWrapper<TeachplanMedia>
                    mediaLambdaQueryWrapper = new LambdaQueryWrapper<>();
            mediaLambdaQueryWrapper.eq(TeachplanMedia::getTeachplanId,teachPlanId);
            int mediaCount = mediaService.count(mediaLambdaQueryWrapper);
            if (mediaCount > 0) ExceptionCast.cast(ContentErrorCode.E_120413);
        }
        // 4.根据Id删除课程计划
        removeById(teachPlanId);
    }

    /*判断关键数据 : 课程id 课程计划名称 机构id*/
    private CourseBaseDTO verifyTeachplanMsg(TeachplanDTO dto, Long companyId) {
        if (ObjectUtils.isEmpty(companyId) ||
                StringUtil.isBlank(dto.getPname()) ||
                ObjectUtils.isEmpty(dto.getCourseId())) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //判断关数据的方法：
        Long courseId = dto.getCourseId();
        return getCourseAndVerify(courseId, companyId);
    }

    /*
     *课程基础信息
     *判断是否存在
     *判断是否同一家机构
     *判断是否删除
     *判断课程基础信息审核状态：未提交和审核未通过
     * */
    private CourseBaseDTO getCourseAndVerify(Long courseId, Long companyId) {
        CourseBaseDTO courseBase = courseBaseService.getCourseBaseById(courseId, companyId);
        String auditStatus = courseBase.getAuditStatus();
        if (CourseAuditEnum.AUDIT_PASTED_STATUS.getCode().equals(auditStatus) ||
                CourseAuditEnum.AUDIT_COMMIT_STATUS.getCode().equals(auditStatus) ||
                CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode().equals(auditStatus)
        ) {
            ExceptionCast.cast(ContentErrorCode.E_120015);
        }
        return courseBase;
    }

    /* */
    private Teachplan generateParentNode(TeachplanDTO dto, CourseBaseDTO courseBase) {
        //1.判断获得二级或三级课程计划的父级标识：
        //     判断parentid是否为空
        //        如果为空：应获得二级课程计划的父级数据
        //        如果不为空：应获得三级课程计划的父级数据
        Long parentid = dto.getParentid();
        // 2.获得父级数据
        if (ObjectUtils.isEmpty(parentid)) {
            //    获得二级课程计划父级数据
            //        判断父级数据是否存在：courseid 、 parentid = 0  grade =1
            //           如果不存在--在后端创建出数据内容（一级课程计划）
            //           如果存在--直接返回
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getCourseId,dto.getCourseId());
            queryWrapper.eq(Teachplan::getParentid,TeachPlanEnum.FIRST_PARENTID_FLAG);

            Teachplan rootNode = this.getOne(queryWrapper);

            if (ObjectUtils.isEmpty(rootNode)) {

                rootNode = Teachplan.builder()
                        .pname(courseBase.getName())
                        .parentid(new Long(TeachPlanEnum.FIRST_PARENTID_FLAG.toString()))
                        .grade(TeachPlanEnum.FIRST_LEVEL)
                        .orderby(TeachPlanEnum.FIRST_LEVEL)
                        .courseId(dto.getCourseId())
                        .description(courseBase.getDescription())
                        .build();

                boolean result = this.save(rootNode);

                if (!result) {
                    ExceptionCast.cast(ContentErrorCode.E_120415);
                }
            }

            return rootNode;
        } else {
            //    获得三级课程计划父级数据
            //        判断父级数据是否存在
            //          如果不存在--抛出异常
            //          如果存在--直接返回
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getId, dto.getParentid());

            Teachplan parentNode = this.getOne(queryWrapper);

            if (ObjectUtils.isEmpty(parentNode)) {
                ExceptionCast.cast(ContentErrorCode.E_120408);
            }
            return parentNode;
        }
    }

}
