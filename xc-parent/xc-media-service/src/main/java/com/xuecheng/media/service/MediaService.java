package com.xuecheng.media.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.api.media.model.qo.QueryMediaModel;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.media.entity.Media;

/**
 * <p>
 * 媒资信息 服务类
 * </p>
 *
 * @author itcast
 * @since 2022-07-18
 */
public interface MediaService extends IService<Media> {

    /**
     * 保存媒资信息功能
     *
     * @param dto 传入service层的dto类型数据
     * @return MediaDTO
     */
    MediaDTO createMedia(MediaDTO dto);

    /**
     * 分页条件查询课程媒资基本信息
     *
     * @param params    分页封装数据
     * @param model     条件查询封装数据
     * @param companyId 机构id
     * @return PageVO 分页封装数据
     */
    PageVO queryMediaList(PageRequestParams params, QueryMediaModel model, Long companyId);

    /**
     * 获取媒资资源路径功能实现方法
     *
     * @param mediaId   媒资id
     * @param companyId 机构id
     * @return 媒资地址
     */
    String getPlayUrlByMediaId(Long mediaId, Long companyId);

    /**
     * 根据id删除媒资信息功能实现
     *
     * @param mediaId   媒资id
     * @param companyId 机构id
     */
    void removeMedia(Long mediaId, Long companyId);

    /**
     * 根据id查询媒资信息--远程调用
     *
     * @param mediaId 媒资id
     * @return RestResponse<MediaDTO>
     */
    RestResponse<MediaDTO> getById4Service(Long mediaId);

    /**
     * 运营平台审核媒资功能实现
     *
     * @param mediaDTO 审核信息封装类
     */
    void approveMedia(MediaDTO mediaDTO);
}
