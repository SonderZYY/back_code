package com.xuecheng.content.controller;

import com.xuecheng.api.content.TeachPlanApi;import com.xuecheng.api.content.model.dto.TeachplanDTO;
import com.xuecheng.api.content.model.dto.TeachplanMediaDTO;
import com.xuecheng.api.content.model.vo.TeachplanVO;
import com.xuecheng.api.media.model.vo.BindTeachplanMediaVO;
import com.xuecheng.common.util.SecurityUtil;
import com.xuecheng.content.convert.TeachplanConvert;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程计划 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
public class TeachplanController implements TeachPlanApi {

    @Autowired
    private TeachplanService teachplanService;

    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public TeachplanDTO queryTreeNodesByCourseId(@PathVariable Long courseId) {
        Long companyId = SecurityUtil.getCompanyId();
        return teachplanService.queryTreeNodesByCourseId(courseId, companyId);

    }

    @PostMapping("/teachplan")
    public TeachplanDTO creatOrModifyTeachplan(@RequestBody TeachplanVO vo) {
        Long companyId = SecurityUtil.getCompanyId();
        TeachplanDTO dto = TeachplanConvert.INSTANCE.vo2dto(vo);
        return teachplanService.creatOrModifyTeachplan(dto, companyId);
    }

    @DeleteMapping("/teachplan/{teachPlanId}")
    public void removeTeachPlan(@PathVariable Long teachPlanId) {
        Long companyId = SecurityUtil.getCompanyId();
        teachplanService.removeTeachPlan(teachPlanId, companyId);
    }

    @PostMapping("teachplan/media/association")
    public TeachplanMediaDTO associateMedia(BindTeachplanMediaVO vo) {
        Long companyId = SecurityUtil.getCompanyId();
        TeachplanMediaDTO teachplanMediaDTO = new TeachplanMediaDTO();
        teachplanMediaDTO.setTeachplanId(vo.getTeachplanId());
        teachplanMediaDTO.setMediaId(vo.getMediaId());
        return teachplanService.associateMedia(teachplanMediaDTO, companyId);
    }
}
