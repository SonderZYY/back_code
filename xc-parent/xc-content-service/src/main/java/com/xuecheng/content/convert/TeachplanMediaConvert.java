package com.xuecheng.content.convert;


import com.xuecheng.api.content.model.dto.TeachplanMediaDTO;
import com.xuecheng.content.entity.TeachplanMedia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p></p>
 *
 * @Description:
 */
@Mapper
public interface TeachplanMediaConvert {

    TeachplanMediaConvert INSTANCE = Mappers.getMapper(TeachplanMediaConvert.class);

    @Mapping(source = "id",target = "teachplanId")
    TeachplanMediaDTO entity2dto(TeachplanMedia teachplanMedia);
    TeachplanMedia dto2entity(TeachplanMediaDTO teachplanDTO);

    List<TeachplanMediaDTO> entitys2dtos(List<TeachplanMedia> teachplanMedias);
    
}
