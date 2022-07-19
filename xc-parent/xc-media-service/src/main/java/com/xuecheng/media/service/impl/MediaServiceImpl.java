package com.xuecheng.media.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.api.media.model.dto.MediaDTO;
import com.xuecheng.api.media.model.qo.QueryMediaModel;
import com.xuecheng.common.domain.code.CommonErrorCode;
import com.xuecheng.common.domain.page.PageRequestParams;
import com.xuecheng.common.domain.page.PageVO;
import com.xuecheng.common.domain.response.RestResponse;
import com.xuecheng.common.enums.common.AuditEnum;
import com.xuecheng.common.enums.common.ResourceTypeEnum;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.StringUtil;
import com.xuecheng.media.common.constant.MediaErrorCode;
import com.xuecheng.media.common.utils.AliyunVODUtil;
import com.xuecheng.media.controller.MediaAuditController;
import com.xuecheng.media.convert.MediaConvert;
import com.xuecheng.media.entity.Media;
import com.xuecheng.media.mapper.MediaMapper;
import com.xuecheng.media.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 媒资信息 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService {

    /**
     * 保存媒资信息的功能实现方法
     * 业务分析
     * 0.事务—开启
     * 1.判断关键数据
     * 机构id companyId
     * 媒资id fileId-videoId
     * 媒资名称 fileName
     * 2.给媒资信息的审核状态赋值
     * auditStatus：未审核
     * 3.保存信息
     * 4.返回信息
     */
    @Transactional
    public MediaDTO createMedia(MediaDTO dto) {
        //1.判断关键数据
        // 机构id companyId
        // 媒资id fileId-videoId
        // 媒资名称 fileName
        if (ObjectUtils.isEmpty(dto.getCompanyId())
                || StringUtil.isBlank(dto.getFileId())
                || StringUtil.isBlank(dto.getFilename())) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        // 2.给媒资信息的审核状态赋值
        //  auditStatus：未审核
        dto.setAuditStatus(AuditEnum.AUDIT_UNPAST_STATUS.getCode());
        // 3.保存信息
        Media media = MediaConvert.INSTANCE.dto2entity(dto);
        // 4.返回信息
        boolean result = this.save(media);
        if (!result) {
            ExceptionCast.cast(MediaErrorCode.E_140001);
        }
        Media mediaResult = this.getById(media.getId());
        return MediaConvert.INSTANCE.entity2dto(mediaResult);
    }

    /**
     * 分页条件查询媒资
     * 1.是否需要开启事务（查询不需要开启，增删改时需要开启事务）
     * 2.判断关键数据（代码健壮性）
     * 关键数据：数据来源是前端
     * 分页数据需要判断：pageNo、pageSize
     * 查询添加对象数据不需要判断
     * 3.构建Page（MP）对象
     * 4.构建查询构建对象
     * 5.查询数据
     * 分页对象Page
     * 查询构建对象
     * 6.将查询结果封装并返回
     */
    @Override
    public PageVO queryMediaList(PageRequestParams params, QueryMediaModel model, Long companyId) {
        //2.判断关键数据（代码健壮性）
        //    关键数据：数据来源是前端
        //        分页数据需要判断：pageNo、pageSize
        //        查询添加对象数据不需要判断
        if (params.getPageNo() < 1) {
            params.setPageNo(PageRequestParams.DEFAULT_PAGE_NUM);
        }
        if (params.getPageSize() < 1) {
            params.setPageSize(PageRequestParams.DEFAULT_PAGE_SIZE);
        }
        //3.构建Page（MP）对象
        Page<Media> page = new Page<>(params.getPageNo(), params.getPageSize());
        //4.构建查询构建对象
        LambdaQueryWrapper<Media> wrapper = new LambdaQueryWrapper<>();
        //5.查询数据
        //    分页对象Page
        //    查询构建对象
        wrapper.like(StringUtil.isBlank(model.getFilename()), Media::getFilename, model.getFilename());
        wrapper.eq(StringUtil.isBlank(model.getAuditStatus()), Media::getAuditStatus, model.getAuditStatus());
        if (!(ObjectUtils.nullSafeEquals(companyId, MediaAuditController.OPERATION_FLAG))) {
            wrapper.eq(Media::getCompanyId, companyId);
        }
        wrapper.orderByDesc(Media::getCreateDate);
        Page<Media> mediaResult = this.page(page, wrapper);
        //6.将查询结果封装并返回
        List<Media> records = mediaResult.getRecords();
        long total = mediaResult.getTotal();

        List<MediaDTO> dtoList = Collections.EMPTY_LIST;
        if (!(CollectionUtils.isEmpty(records))) {
            dtoList = new ArrayList<>();
            dtoList = MediaConvert.INSTANCE.entitys2dtos(records);
        }
        return new PageVO<>(dtoList, total, params.getPageNo(), params.getPageSize());
    }

    /**
     * 获取媒资播放地址功能实现
     * 业务分析
     * 0.事务-不开启
     * 1.判断关键数据
     * 机构id
     * 媒资id
     * 2.判断业务数据
     * 媒资信息是否存在
     * 是否同一家机构
     * 3.获取媒资文件的资源路径
     * 判断是否为视频类型，如果是需要获得aliyun视频的地址
     * 4.返回资源访问路径
     */
    @Override
    public String getPlayUrlByMediaId(Long mediaId, Long companyId) {
        //1.判断关键数据
        // 机构id
        // 媒资id
        if (ObjectUtils.isEmpty(mediaId) || ObjectUtils.isEmpty(companyId)) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.判断业务数据
        // 媒资信息是否存在
        LambdaQueryWrapper<Media> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Media::getId, mediaId);
        // if (!(ObjectUtils.nullSafeEquals(companyId,MediaAuditC)))
        Media result = this.getOne(wrapper);
        if (ObjectUtils.isEmpty(result)) {
            ExceptionCast.cast(MediaErrorCode.E_140005);
        }
        //3.获取媒资文件的资源路径
        // 判断是否为视频类型，如果是需要获得aliyun视频的地址
        String type = result.getType();
        String resourceUrl = null;
        if (ResourceTypeEnum.VIDEO.getCode().equals(type)) {
            resourceUrl = getAliyunVodPlayUrl(result.getFileId());
        } else if (ResourceTypeEnum.DOCUMENT.getCode().equals(type)) {
            //获取文档的访问路径
            System.out.println("暂未实现");
        } else if (ResourceTypeEnum.WORK.getCode().equals(type)) {
            //获取文档的访问路径
            System.out.println("暂未实现");
        }
        //4.返回资源访问路径
        return resourceUrl;
    }

    /**
     * 删除媒资信息功能实现
     * 业务分析
     * 0.事务：不开启
     * 1.关键数据
     * 媒资id
     * 机构id
     * 2.业务数据
     * 媒资是否删除
     * 媒资是否关联课程
     * 3.删除媒资信息
     * 先删除阿里视频信息
     * 再删除本地媒资信息
     */
    @Transactional
    public void removeMedia(Long mediaId, Long companyId) {
        //1.关键数据
        // 媒资id
        // 机构id
        if (ObjectUtils.isEmpty(mediaId) || ObjectUtils.isEmpty(companyId)) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.业务数据
        // 媒资是否删除
        LambdaQueryWrapper<Media> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Media::getId, mediaId);
        wrapper.eq(Media::getCompanyId, companyId);
        Media result = this.getOne(wrapper);
        if (ObjectUtils.isEmpty(result)) {
            ExceptionCast.cast(MediaErrorCode.E_140005);
        }
        // 媒资是否关联课程

        //3.删除媒资信息
        // 先删除阿里视频信息
        try {
            DefaultAcsClient client = AliyunVODUtil.initVodClient(region, accessKeyId, accessKeySecret);

            String fileId = result.getFileId();
            AliyunVODUtil.deleteVideo(client, fileId);

        } catch (Exception e) {
            log.error(MediaErrorCode.E_140016.getDesc() + " : {}", e.getMessage());
            ExceptionCast.cast(MediaErrorCode.E_140016);
        }
        // 再删除本地媒资信息
        boolean removeResult = this.removeById(result.getId());
        if (!removeResult) {
            ExceptionCast.cast(MediaErrorCode.E_140002);
        }
    }

    /**
     * 根据id查询媒资信息-远程调用
     * 业务分析：
     * 0.事务：不开启
     * 1.判断关键数据
     * mediaId
     * 2.判断业务数据
     * 媒资是否存在
     * 3.返回结果
     */
    @Override
    public RestResponse<MediaDTO> getById4Service(Long mediaId) {
        //1.判断关键数据
        // mediaId
        if (ObjectUtils.isEmpty(mediaId)) {
            return RestResponse.validfail(CommonErrorCode.E_100101);
        }
        //2.判断业务数据
        // 媒资是否存在
        Media media = this.getById(mediaId);
        if (ObjectUtils.isEmpty(media)) {
            return RestResponse.validfail(MediaErrorCode.E_140005);
        }
        //3.返回结果
        MediaDTO mediaDTO = MediaConvert.INSTANCE.entity2dto(media);
        return RestResponse.success(mediaDTO);
    }

    /**
     * 运营平台审核媒资
     * 业务分析
     * 0.事务-开启
     * 1.判断关键数据
     * mediaDTO的信息
     * 2.判断业务数据
     * 判断是否存在
     * 判断是否是未审核状态
     * 判断运营的审核状态是否合法
     * 3.修改媒资信息的审核状态
     */
    @Transactional
    public void approveMedia(MediaDTO dto) {
        //1.判断关键数据
        // mediaDTO的信息
        if (StringUtil.isBlank(dto.getAuditMind())
                || StringUtil.isBlank(dto.getAuditStatus())
                || ObjectUtils.isEmpty(dto.getId())) {
            ExceptionCast.cast(CommonErrorCode.E_100101);
        }
        //2.判断业务数据
        // 判断是否存在
        // 判断是否是未审核状态
        // 判断运营的审核状态是否合法
        Media media = this.getById(dto.getId());
        if (ObjectUtils.isEmpty(media)) {
            ExceptionCast.cast(MediaErrorCode.E_140005);
        }
        String auditStatus = media.getAuditStatus();
        if (!(AuditEnum.AUDIT_UNPAST_STATUS.getCode().equals(auditStatus))) {
            ExceptionCast.cast(MediaErrorCode.E_140017);
        }
        String status = dto.getAuditStatus();
        if (AuditEnum.AUDIT_UNPAST_STATUS.getCode().equals(status)) {
            ExceptionCast.cast(MediaErrorCode.E_140019);
        }
        //3.修改媒资信息的审核状态
        LambdaUpdateWrapper<Media> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Media::getAuditStatus, dto.getAuditStatus());
        wrapper.set(Media::getChangeDate, LocalDateTime.now());
        wrapper.eq(Media::getId, media.getId());
        boolean result = this.update(wrapper);
        if (!result) {
            ExceptionCast.cast(MediaErrorCode.E_140001);
        }

    }

    @Value("${aliyun.region}")
    private String region;
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 获取阿里云视频播放地址方法
     *
     * @param fileId 视频id
     * @return 资源路径
     */
    private String getAliyunVodPlayUrl(String fileId) {
        String playURL = null;
        try {
            DefaultAcsClient client = AliyunVODUtil.initVodClient(region, accessKeyId, accessKeySecret);

            GetPlayInfoResponse playInfo = AliyunVODUtil.getPlayInfo(client, fileId);

            List<GetPlayInfoResponse.PlayInfo> playInfoList = playInfo.getPlayInfoList();

            if (CollectionUtils.isEmpty(playInfoList)) {
                ExceptionCast.cast(MediaErrorCode.E_140018);
            }
            // 默认获得第一个播放地址
            playURL = playInfoList.get(0).getPlayURL();
        } catch (Exception e) {
            log.error(MediaErrorCode.E_140012.getDesc() + " : {}", e.getMessage());
            ExceptionCast.cast(MediaErrorCode.E_140012);
        }
        return playURL;
    }
}
