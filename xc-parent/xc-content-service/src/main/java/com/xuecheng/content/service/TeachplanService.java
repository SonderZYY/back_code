package com.xuecheng.content.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.api.content.model.dto.TeachplanDTO;
import com.xuecheng.content.entity.Teachplan;

/**
 * <p>
 * 课程计划 服务类
 * </p>
 *
 * @author itcast
 * @since 2022-07-12
 */
public interface TeachplanService extends IService<Teachplan> {
    /**
     * 根据课程Id查询课程计划树形结构(树形结构为三级目录)
     * @param courseId 课程id
     * @param companyId 机构标识id
     * @return TeachplanDTO
     */
    TeachplanDTO queryTreeNodesByCourseId(Long courseId, Long companyId);

    /**
     * 课程计划的添加和修改
     * @param dto 前端传入数据—封装为DTO类型
     * @param companyId 机构标识id
     * @return TeachplanDTO
     */
    TeachplanDTO creatOrModifyTeachplan(TeachplanDTO dto, Long companyId);

    /**
     * 根据课程计划Id删除课程计划信息
     * @param teachPlanId {@link Long} 课程计划Id
     * @param companyId {@link Long} 公司Id
     */
    void removeTeachPlan(Long teachPlanId,Long companyId);
}
