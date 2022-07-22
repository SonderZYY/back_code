package com.xuecheng.content.controller;

import com.xuecheng.api.content.CourseBaseApi;
import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.api.content.model.qo.QueryCourseBaseModel;
import com.xuecheng.api.content.model.vo.CourseBaseVO;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.util.SecurityUtil;
import com.xuecheng.content.convert.CourseBaseConvert;
import com.xuecheng.content.service.CourseBaseService;
import com.xuecheng.content.service.CoursePubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * <p>
 * 课程基本信息 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
public class CourseBaseController implements CourseBaseApi {

    @Autowired
    private CourseBaseService courseBaseService;

    @Autowired
    private CoursePubService coursePubService;

    /**
     * SpringMvc 对于参数方式接收：
     * 默认类型是：QueryString --> @RequestParam(该注解不需要添加)
     * ps: 传入的参数key 和对象中的属性名一致
     * Path (Restful 风格) --> @PathVariable
     * body 请求体参数 --> @RequestBody
     */
    @PostMapping("/course/list")
    public PageVO queryCourseList(PageRequestParams params, @RequestBody QueryCourseBaseModel model) {

        //解析请求头并获得公司id
        Long companyId = SecurityUtil.getCompanyId();

        return courseBaseService.queryCourseBaseList(params, model, companyId);
    }

    @PostMapping("/course")
    public CourseBaseDTO creatCourseBase(@RequestBody CourseBaseVO vo) {
        //获取唯一标识机构id
        Long companyId = SecurityUtil.getCompanyId();
        CourseBaseDTO dto = CourseBaseConvert.INSTANCE.vo2dto(vo);
        dto.setCompanyId(companyId);
        return courseBaseService.creatCourseBase(dto);
    }

    @GetMapping("/course/{courseBaseId}")
    public CourseBaseDTO getCourseBaseById(@PathVariable Long courseBaseId) {
        //获取唯一标识机构id
        Long companyId = SecurityUtil.getCompanyId();
        return courseBaseService.getCourseBaseById(courseBaseId, companyId);

    }

    @PutMapping("/course/")
    public CourseBaseDTO modifyCourseBaseById(@RequestBody CourseBaseVO vo) {
        //获取唯一标识机构id
        Long companyId = SecurityUtil.getCompanyId();
        CourseBaseDTO dto = CourseBaseConvert.INSTANCE.vo2dto(vo);
        dto.setCompanyId(companyId);
        return courseBaseService.modifyCourseBaseById(dto);
    }

    @DeleteMapping("/course/{courseBaseId}")
    public void removeCourseById(@PathVariable Long courseBaseId) {
        //获取唯一标识机构id
        Long companyId = SecurityUtil.getCompanyId();
        courseBaseService.removeCourseById(courseBaseId, companyId);
    }

    @GetMapping("/course/commit/{courseBaseId}")
    public void commitCourseBase(@PathVariable Long courseBaseId) {
        //获取唯一标识机构id
        Long companyId = SecurityUtil.getCompanyId();
        courseBaseService.commitCourseBase(courseBaseId, companyId);
    }

    @GetMapping("course/preview/{courseId}/{companyId}")
    public Object previewCourse(@PathVariable Long courseId, @PathVariable Long companyId) {
        //获取数据模型dataMap
        Map<String, Object> dataMap = coursePubService.previewCourse(courseId, companyId);
        return new ModelAndView("learing_article", dataMap);

    }

    @PostMapping("course_pub/publish/{courseId}")
    public void publish(@PathVariable("courseId") Long courseBaseId) {
        //获取唯一标识机构id
        Long companyId = SecurityUtil.getCompanyId();
        coursePubService.publish(courseBaseId, companyId, false);
    }
}
