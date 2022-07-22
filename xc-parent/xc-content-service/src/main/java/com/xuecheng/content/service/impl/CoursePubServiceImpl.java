package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.api.content.model.dto.TeachplanDTO;
import com.xuecheng.api.system.model.dto.CourseCategoryDTO;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.enums.content.CourseAuditEnum;
import com.xuecheng.common.enums.content.CourseModeEnum;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.JsonUtil;
import com.xuecheng.content.common.constant.ContentErrorCode;
import com.xuecheng.content.common.constant.CoursePubTemplateKey;
import com.xuecheng.content.convert.CoursePubConvert;
import com.xuecheng.content.entity.*;
import com.xuecheng.content.mapper.CoursePubMapper;
import com.xuecheng.content.service.*;
import com.xuecheng.feign.system.SystemApiAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程发布 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CoursePubServiceImpl extends ServiceImpl<CoursePubMapper, CoursePub> implements CoursePubService {

    @Autowired
    private CourseBaseService courseBaseService;

    @Autowired
    private CourseMarketService courseMarketService;

    @Autowired
    private TeachplanService teachplanService;


    @Autowired
    private SystemApiAgent systemApiAgent;

    @Autowired
    private CourseTeacherService courseTeacherService;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CoursePubMsgService coursePubMsgService;

    @Value("${course.publish.exchange}")
    private String exchange;
    @Value("${course.publish.routingkey}")
    private String routingkey;


    /**
     * 课程预览主业务
     * 业务分析：
     * 0.事务-开启
     * 1.判断关键数据
     * 2.构建CoursePub数据
     * 3.根据CoursePub数据生产数据模型DataMap
     * 4.返回数据模型DataMap
     */
    @Transactional
    public Map<String, Object> previewCourse(Long courseBaseId, Long companyId) {
        //1.判断关键数据
        if (ObjectUtils.isEmpty(courseBaseId) || ObjectUtils.isEmpty(companyId)) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.构建CoursePub数据
        CoursePub coursePub = generateCoursePub(courseBaseId, companyId, false);
        //3.根据CoursePub数据生产数据模型DataMap
        //4.返回数据模型DataMap
        return generateDataMap(coursePub);
    }


    /**
     *1.判断关键数据
     *  courseId companyId
     * 2.判断业务数据
     *  课程基础信息
     *      判断是否存在
     *      判断是否是同一家机构
     *      判断是否删除
     *      判断审核状态：教学机构课程预览--未提交、审核未通过
     *  课程营销
     *      判断是否存在：根据courseid
     *  课程计划
     *      获得课程计划：根据courseId和companyId（树形结构）
     *  课程教师
     *      判断教师信息是否存在：一定要确保课程最少有一个教师信息
     *
     *  课程分类数据并完善Coursepub数据
     *      调用system服务获得课程分类的名称
     * 3.保存数据的业务数据和消息数据
     *     业务数据：CoursePub数据进行保存
     *     消息数据：CoursePubMsg数据进行保存
     * 4.发送消息给MQ
     */
    @Transactional
    public void publish(Long courseBaseId, Long companyId, boolean isReSend) {
//1.判断关键数据
        //   courseBaseId companyid
        if (ObjectUtils.isEmpty(courseBaseId) ||
                ObjectUtils.isEmpty(companyId)
        ) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        CoursePubMsg coursePubMsg = null;
        // 定时器重新发送消息获得消息表中的数据
        if (isReSend) {
            LambdaQueryWrapper<CoursePubMsg> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(CoursePubMsg::getCourseId, courseBaseId);
            coursePubMsg = coursePubMsgService.getOne(queryWrapper);
        } else {
            // 2.判断业务数据和构建coursepub
            //  课程发布要求：课程基础信息的审核状态--审核通过
            CoursePub coursePub = generateCoursePub(courseBaseId, companyId, true);
            // 3.创建课程发布消息表
            //    CoursePubMsg
            // 如果没有创建数据
            coursePubMsg = coursePubMsgService.getById(coursePub.getId());
            if (ObjectUtils.isEmpty(coursePubMsg)) {

                coursePubMsg = CoursePubMsg.builder().courseId(coursePub.getCourseId())
                        .pubId(coursePub.getId())
                        .pubStatus(CoursePubMsg.UNSENT)
                        .pubName(coursePub.getName())
                        .companyId(coursePub.getCompanyId())
                        .build();

                boolean result = coursePubMsgService.save(coursePubMsg);
                if (!result) {
                    ExceptionCast.cast(ContentErrorCode.E_120202);
                }
            } else {
                // 如果有：消息存在，不要再次发送消息
                return;
            }
        }
        // 4.发送消息给mq
        //   执行消息唯一性：唯一标识-coursePubid
        //   声明confirmcallback
        //   构建发送的消息：coursePubMsg-jsonString
        CorrelationData correlationData = new CorrelationData(coursePubMsg.getPubId().toString());
        // 异步操作
        correlationData.getFuture().addCallback(
                confirm -> {
                    if (confirm.isAck()) {
                        executeChangeLocalStatusData(correlationData.getId());
                    } else {
                        log.error("mq交换机接受消息失败，消息id ：{} ", correlationData.getId());
                    }
                },
                // mq服务异常会调用此方法
                throwable -> {
                    log.error("消息发送失败，mq服务异常，消息的id：{} ，errormsg：{}", correlationData.getId(),
                            throwable.getMessage());
                }
        );
        String jsonString = JsonUtil.objectTojson(coursePubMsg);
        /*
         * 参数：
         *   1.交换机的名称
         *   2.rontingkey
         *   3.消息
         *       消息表的数据：CoursePubMsg（json）
         *   4.CorrelationData(设置confirm callback)
         *       接受交互机处理数据的结果（应答机制）
         *           ack：交换机处理成功--> 修改业务数据和消息表的状态
         *           nack：交换机处理失败--> 记录错误消息，并不操作业务数据和消息表
         * */
        rabbitTemplate.convertAndSend(exchange, routingkey, jsonString, correlationData);
    }

    @Transactional
    public void executeChangeLocalStatusData(String coursePubIdStr) {
        // 0.判断消息是否已经修改
        // 查询coursePubMsg表中的数据并判断其状态
        // CoursePubMsg中的id和CoursePub表的id是唯一、一致
        LambdaQueryWrapper<CoursePubMsg> pubMsgQueryWrapper = new LambdaQueryWrapper<>();
        pubMsgQueryWrapper.eq(CoursePubMsg::getPubId, new Long(coursePubIdStr));
        pubMsgQueryWrapper.eq(CoursePubMsg::getPubStatus, CoursePubMsg.UNSENT);

        CoursePubMsg coursePubMsg = coursePubMsgService.getOne(pubMsgQueryWrapper);
        if (ObjectUtils.isEmpty(coursePubMsg)) {
            log.info("无修改的课程发布消息数据，CoursPubId：{}", coursePubIdStr);
            return;
        }

        //1.修改课程发布消息表数据
        //    未发送-->已发送
        Long coursePubId = new Long(coursePubIdStr);
        LambdaUpdateWrapper<CoursePubMsg> pubMsgUpdateWrapper = new LambdaUpdateWrapper<>();
        pubMsgUpdateWrapper.set(CoursePubMsg::getPubStatus, CoursePubMsg.SENT);
        pubMsgUpdateWrapper.set(CoursePubMsg::getChangeDate, LocalDateTime.now());
        pubMsgUpdateWrapper.eq(CoursePubMsg::getPubId, coursePubId);
        coursePubMsgService.update(pubMsgUpdateWrapper);
        // 2.修改课程基础信息数据
        //    课程审核状态：已发布
        LambdaUpdateWrapper<CourseBase> baseUpdateWrapper = new LambdaUpdateWrapper<>();
        baseUpdateWrapper.set(CourseBase::getAuditStatus, CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode());
        baseUpdateWrapper.set(CourseBase::getChangeDate, LocalDateTime.now());
        baseUpdateWrapper.eq(CourseBase::getId, coursePubMsg.getCourseId());
        courseBaseService.update(baseUpdateWrapper);
    }

    /*
     * 构建CoursePub数据
     *
     *业务分析：
     * 1.判断业务数据
     *  课程基础信息
     *     判断是否存在
     *     判断是否是同一家结构
     *     判断是否删除
     *     判断审核状态
     *     查询条件：courseId、companyid
     *  课程营销
     *     判断是否存在（课程营销是和课程基础信息属于同一个业务操作）
     *     查询条件：courseId
     *  课程计划（一个课程的课程计划树形结构）
     *     查询条件：courseid、companyid
     *  课程分类
     *     从系统管理服务中获得课程分类数据
     *     查询条件：courseCategoryId（mt、st）
     *  课程教师信息
     *     查询条件：courseId
     *2.构建CoursePub数据
     *     courseBase
     *     teachplan
     *     courseMarket
     *     courseTeacher
     *  保存coursepub数据
     *     判断是否存在
     *         如果存在：更新数据
     *         不存在：添加操作
     *3.返回coursePub
     * */
    private CoursePub generateCoursePub(Long courseBaseId, Long companyId, boolean isPublish) {
        //1.判断业务数据
        //  课程基础信息
        //     判断是否存在
        //     判断是否是同一家结构
        //     判断是否删除
        CourseBaseDTO courseBase = courseBaseService.getCourseBaseById(courseBaseId, companyId);
        //     判断审核状态
        //     查询条件：courseId、companyid
        String auditStatus = courseBase.getAuditStatus();
        if (isPublish) {
            //课程发布的课程状态的判断：要求课程审核通过
            if (!(CourseAuditEnum.AUDIT_PASTED_STATUS.getCode().equals(auditStatus))) {
                ExceptionCast.cast(ContentErrorCode.E_120014);
            } else {
                //课程预览的课程审核状态的判断：要求不能是已发布的课程
                if (CourseAuditEnum.AUDIT_PUBLISHED_STATUS.getCode().equals(auditStatus)) {
                    ExceptionCast.cast(ContentErrorCode.E_120014);
                }
            }
        }
        //  课程营销
        //     判断是否存在（课程营销是和课程基础信息属于同一个业务操作）
        //     查询条件：courseId
        LambdaQueryWrapper<CourseMarket> marketWrapper = new LambdaQueryWrapper<>();
        marketWrapper.eq(CourseMarket::getCourseId, courseBaseId);
        CourseMarket courseMarket = courseMarketService.getOne(marketWrapper);
        if (ObjectUtils.isEmpty(courseMarket)) {
            ExceptionCast.cast(ContentErrorCode.E_120101);
        }
        String marketJsonString = JsonUtil.objectTojson(courseMarket);
        //  课程计划（一个课程的课程计划树形结构）
        //     查询条件：courseid、companyid
        TeachplanDTO teachPlan = teachplanService.queryTreeNodesByCourseId(courseBaseId, companyId);
        String teachPlanJsonString = JsonUtil.objectTojson(teachPlan);
        //  课程分类
        //     从系统管理服务中获得课程分类数据
        //     查询条件：courseCategoryId（mt、st）
        String mt = courseBase.getMt();
        String st = courseBase.getSt();
        RestResponse<CourseCategoryDTO> mtResponse = systemApiAgent.getById(mt);
        if (!(mtResponse.isSuccessful())) {
            ExceptionCast.castWithCodeAndDesc(mtResponse.getCode(), mtResponse.getMsg());
        }
        CourseCategoryDTO mtCateGory = mtResponse.getResult();
        RestResponse<CourseCategoryDTO> stResponse = systemApiAgent.getById(st);
        if (!(stResponse.isSuccessful())) {
            ExceptionCast.castWithCodeAndDesc(stResponse.getCode(), stResponse.getMsg());
        }
        CourseCategoryDTO stCategory = stResponse.getResult();
        //  课程教师信息
        //     查询条件：courseId
        LambdaQueryWrapper<CourseTeacher> teacherWrapper = new LambdaQueryWrapper<>();
        teacherWrapper.eq(CourseTeacher::getCourseId, courseBaseId);
        List<CourseTeacher> teachers = courseTeacherService.list();
        String teacherJsonString = null;
        if (!(CollectionUtils.isEmpty(teachers))) {
            teacherJsonString = JsonUtil.objectTojson(teachers);
        }
        //2.构建CoursePub数据
        //     courseBase
        //     teachplan
        //     courseMarket
        //     courseTeacher
        CoursePub coursePub = CoursePubConvert.INSTANCE.courseBase2coursePub(courseBase);
        coursePub.setTeachplan(teachPlanJsonString);
        coursePub.setMarket(marketJsonString);
        coursePub.setTeachers(teacherJsonString);
        //课程营销的冗余字段
        coursePub.setPrice(courseMarket.getPrice());
        coursePub.setCharge(courseMarket.getCharge());
        //课程分类的信息完善
        coursePub.setStName(stCategory.getName());
        coursePub.setMtName(stCategory.getName());
        //课程发布与课程基础信息关联
        coursePub.setCourseId(courseBaseId);
        //  保存coursepub数据
        //     判断是否存在
        LambdaQueryWrapper<CoursePub> pubWrapper = new LambdaQueryWrapper<>();
        pubWrapper.eq(CoursePub::getCourseId, courseBaseId);
        CoursePub pub = this.getOne(pubWrapper);
        boolean result = false;
        if (ObjectUtils.isEmpty(pub)) {
            //         不存在：添加操作
            result = this.save(coursePub);
        } else {
            //         如果存在：更新数据
            coursePub.setId(pub.getId());
            result = this.updateById(coursePub);
        }
        if (!result) {
            ExceptionCast.cast(ContentErrorCode.E_120205);
        }
        return coursePub;

    }

    /**
     * 构建数据模型DataMap
     */
    private Map<String, Object> generateDataMap(CoursePub coursePub) {
        //构建数据模型dataMap
        HashMap<String, Object> dataMap = new HashMap<>();
        //课程发布
        dataMap.put(CoursePubTemplateKey.COURSEPUB, coursePub);
        //课程营销表
        String marketStringJson = coursePub.getMarket();
        CourseMarket courseMarket = JsonUtil.jsonToObject(marketStringJson, CourseMarket.class);
        dataMap.put(CoursePubTemplateKey.COURSEMARKET, courseMarket);
        //课程计划
        String teachPlan = coursePub.getTeachplan();
        TeachplanDTO teachplanDTO = JsonUtil.jsonToObject(teachPlan, TeachplanDTO.class);
        dataMap.put(CoursePubTemplateKey.TEACHPLANNODE, teachplanDTO);
        //课程模式
        CourseModeEnum[] values = CourseModeEnum.values();
        dataMap.put(CoursePubTemplateKey.COURSETEACHMODEENUMS, values);
        //课程教师
        String teachers = coursePub.getTeachers();
        CourseTeacher courseTeacher = JsonUtil.jsonToObject(teachers, CourseTeacher.class);
        dataMap.put(CoursePubTemplateKey.TEACHERS, courseTeacher);
        return dataMap;
    }

}

