package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.entity.CoursePub;

import java.util.Map;

/**
 * <p>
 * 课程发布 服务类
 * </p>
 *
 * @author itcast
 */
public interface CoursePubService extends IService<CoursePub> {


    /**
     * 课程预览
     * @param courseBaseId 课程id
     * @param companyId 机构id
     * @return Map集合封装的dattaMap数据模型
     */
    Map<String, Object> previewCourse(Long courseBaseId, Long companyId);



    /**
     * 课程发布
     * @param courseBaseId 课程id
     * @param companyId 机构id
     * @param isReSend  mq消息是否发生成功
     */
    void publish(Long courseBaseId, Long companyId,boolean isReSend);
}
