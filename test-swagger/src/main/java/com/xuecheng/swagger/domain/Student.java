package com.xuecheng.swagger.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@ApiModel("学员封装实体类")
public class Student {
    //学员编号
    @ApiModelProperty("学员编号")
    private String stuNo;
    //学员名称
    @ApiModelProperty("学员姓名")
    private String name;
    //学员年龄
    @ApiModelProperty("学员年龄")
    private int age;
    //学员地址
    @ApiModelProperty("学员住址")
    private String address;
    
    //getter/setter 省略
}