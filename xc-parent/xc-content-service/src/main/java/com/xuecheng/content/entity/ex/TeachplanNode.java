package com.xuecheng.content.entity.ex;

import com.xuecheng.content.entity.Teachplan;
import lombok.Data;

import java.util.List;
/**
 * <p>
 *     课程计划树形结构扩展PO类
 * </p>
 *
 * @Description: 查询三级课程计划树形结构扩展类
 */
@Data
public class TeachplanNode extends Teachplan {
    /**
     * 子节点数据集合，课程计划为3级
     * 为方便树形结构数据的返回，定义子节点的集合属性
     */
    List<TeachplanNode> childrenNodes;
}