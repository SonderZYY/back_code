package com.xuecheng.content.controller;

import com.xuecheng.api.content.CourseBaseApi;
import com.xuecheng.api.content.qo.QueryCourseBaseModel;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.util.SecurityUtil;
import com.xuecheng.content.service.CourseBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 课程基本信息 前端控制器
 * </p>
 *
 * @author itcast
 */
@Slf4j
@RestController
@RequestMapping("courseBase")
public class CourseBaseController implements CourseBaseApi {

    @Autowired
    private CourseBaseService courseBaseService;

    /**
     * SpringMvc 对于参数方式接收：
     * 默认类型是：QueryString --> @RequestParam(该注解不需要添加)
     * ps: 传入的参数key 和对象中的属性名一致
     * Path (Restful 风格) --> @PathVariable
     * body 请求体参数 --> @RquestBody
     */
    @PostMapping("/course/list")
    public PageVO queryCourseList(PageRequestParams params, @RequestBody QueryCourseBaseModel model) {

        //解析请求头并获得公司id
        Long companyId = SecurityUtil.getCompanyId();

        return courseBaseService.queryCourseBaseList(params,model,companyId);
    }
}
