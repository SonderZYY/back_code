package com.xuecheng.content.convert;


import com.xuecheng.api.content.model.dto.CourseTeacherDTO;
import com.xuecheng.api.content.model.vo.CourseTeacherVO;
import com.xuecheng.content.entity.CourseTeacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CourseTeacherConvert {
    CourseTeacherConvert INSTANCE = Mappers.getMapper(CourseTeacherConvert.class);

    // 将po转为dto数据
    @Mapping(source = "id",target = "courseTeacherId")
    CourseTeacherDTO entity2dto(CourseTeacher courseTeacher);
    // 将pos转为dtos数据
    List<CourseTeacherDTO> entitys2dtos(List<CourseTeacher> courseTeacher);

    // 将vo转为dto数据
    CourseTeacherDTO vo2dto(CourseTeacherVO courseTeacherVO);

}
