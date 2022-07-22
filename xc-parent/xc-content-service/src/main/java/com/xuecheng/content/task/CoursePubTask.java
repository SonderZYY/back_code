package com.xuecheng.content.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.entity.CoursePubMsg;
import com.xuecheng.content.service.CoursePubMsgService;
import com.xuecheng.content.service.CoursePubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p></p>
 *
 * @Description:
 */
@Slf4j
@Component
public class CoursePubTask {

    @Autowired
    private CoursePubService coursePubService;

    @Autowired
    private CoursePubMsgService coursePubMsgService;

    /*
    * 业务操作：
    *   1.查询课程发布表数据
    *     查询未发送：0
    *   2.查询集合数据将数据逐条发送消息内容
    *
    * */
    @Scheduled(cron = "0/5 * * * * ?")
    public void doTask() {
        log.info("任务执行");

        //1.查询课程发布表数据
        //   查询未发送：0
        LambdaQueryWrapper<CoursePubMsg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CoursePubMsg::getPubStatus, CoursePubMsg.UNSENT);

        List<CoursePubMsg> list = coursePubMsgService.list(queryWrapper);

        // 2.查询集合数据将数据逐条发送消息内容
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        for (CoursePubMsg coursePubMsg : list) {
            coursePubService.publish(coursePubMsg.getCourseId(),
                    coursePubMsg.getCompanyId(),true);
        }

    }

}
