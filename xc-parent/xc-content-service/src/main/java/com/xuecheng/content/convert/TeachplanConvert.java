package com.xuecheng.content.convert;

import com.xuecheng.api.content.model.dto.TeachplanDTO;
import com.xuecheng.api.content.model.vo.TeachplanVO;
import com.xuecheng.content.entity.Teachplan;
import com.xuecheng.content.entity.ex.TeachplanNode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * <p></p>
 *
 * @Description:
 */
@Mapper
public interface TeachplanConvert {

    TeachplanConvert INSTANCE = Mappers.getMapper(TeachplanConvert.class);


    @Mappings({
            @Mapping(source = "id",target = "teachPlanId"),
            @Mapping(source = "childrenNodes",target = "teachPlanTreeNodes")
    })
    TeachplanDTO node2dto(TeachplanNode node);


    @Mapping(source = "teachPlanId",target = "id")
    Teachplan dto2entity(TeachplanDTO dto);

    @Mapping(source = "id",target = "teachPlanId")
    TeachplanDTO entity2dto(Teachplan teachplan);

    TeachplanDTO vo2dto(TeachplanVO vo);

}
