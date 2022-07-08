package com.xuecheng.swagger.controller;

import com.xuecheng.swagger.domain.Student;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("stu")
public class HelloController implements HelloApi {
    /**
     * 测试无参数接口地址
       无参：get  http://ip:port/rootPath/stu/hello
     */
    @GetMapping("hello")
    public String hello() {
        return "hello";
    }
    /**
     * 测试 queryString 风格的参入参数
       QueryString(问号传参)：
       get	http://ip:port/rootPath/stu?num=xxx
     */
    @GetMapping
    public Student modifyStudentNum(@RequestParam("num") String num) {
        Student student = new Student(num, "xiaohong", 10, "parts");
        return student;
    }
    /**
     * 测试Restful风格的参入参数
       path(Restful):
       get	http://ip:port/rootPath/stu/xxxx
     */
    @GetMapping("{name}")
    public Student modifyStudentName(@PathVariable("name") String name) {
        Student student = new Student("002", name, 10, "parts");
        return student;
    }
    /**
     * 测试json格式的传入参数
       请求体传参(json格式的数据):
       	post http://ip:port/rootPath/stu
       	requestBody：
        {
        	"xxxxx"："xxxx"
        }
     */
    @PostMapping
    public Student modifyStudent(@RequestBody Student student) {
        student.setName("modifyName");
        return student;
    }
    /**
     * 测试 queryString 、Restful 和 json 格式的参数
       put	http://ip:port/rootPath/stu/xxxx?name=xxx
       	requestBody：
        {
        	"xxxxx"："xxxx"
        }
     */
    @Override
    @PutMapping("{id}")
    public Student mofidyStudentBynNum(@PathVariable("id") String id, @RequestParam("name") String name, @RequestBody Student student) {
        student.setName(name);
        student.setStuNo(id);
        return student;
    }
}