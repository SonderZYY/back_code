package com.xuecheng.media.convert;

import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.api.media.model.vo.MediaAuditVO;
import com.xuecheng.api.media.model.vo.MediaVO;
import com.xuecheng.media.entity.Media;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * <p></p>
 *
 * @Description:
 */
@Mapper
public interface MediaConvert {
    MediaConvert INSTANCE = Mappers.getMapper(MediaConvert.class);

    Media dto2entity(MediaDTO dto);

    MediaDTO entity2dto(Media media);

    List<MediaDTO> entitys2dtos(List<Media> media);


    MediaDTO vo2dto(MediaVO mediaVO);

    MediaDTO audit2dto(MediaAuditVO auditVO);


}
