package com.xuecheng.content.convert;

import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.api.content.model.vo.CourseBaseVO;
import com.xuecheng.content.entity.CourseBase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CourseBaseConvert {
    CourseBaseConvert INSTANCE = Mappers.getMapper(CourseBaseConvert.class);

    @Mapping(source = "id", target = "courseBaseId")
    CourseBaseDTO entity(CourseBase courseBase);


    CourseBaseDTO vo2dto(CourseBaseVO vo);


    @Mapping(source = "courseBaseId", target = "id")
    CourseBase dto2entity(CourseBaseDTO courseBaseDTO);

    /**
     * 集合对象的转换：
     * 需要依赖单个对象属性转换的方法
     */

    List<CourseBaseDTO> entity2dtos(List<CourseBase> courseBases);

}
