package com.xuecheng.content.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author itcast
 */
@Data
@Builder
@TableName("course_pub_msg")
public class CoursePubMsg implements Serializable {

    // 消息为发送成功
    public static final Integer UNSENT = 0;
    // 消息成功发送
    public static final Integer SENT = 1;

    /**
     * 课程发布id
     */
    @TableId
    private Long pubId;

    /**
     * 课程发布名称
     */
    private String pubName;

    /**
     * 课程发布消息状态(0:未发送，1:已发送)
     */
    private Integer pubStatus;

    /**
     * 课程基本信息id
     */
    private Long courseId;

    /**
     * 教学机构id
     */
    private Long companyId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime changeDate;


}
