package com.xuecheng.content.convert;

import com.xuecheng.api.content.model.dto.CourseBaseDTO;
import com.xuecheng.api.content.model.dto.CoursePubDTO;
import com.xuecheng.content.entity.CoursePub;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CoursePubConvert {

    CoursePubConvert INSTANCE = Mappers.getMapper(CoursePubConvert.class);


    CoursePub courseBase2coursePub(CourseBaseDTO courseBase);


    CoursePubDTO entity2dto(CoursePub coursePub);

}