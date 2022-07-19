package com.xuecheng.media.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.xuecheng.api.media.AliyunVodApi;
import com.xuecheng.api.media.model.aliyun.VodUploadRequest;
import com.xuecheng.api.media.model.aliyun.VodUploadToken;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.common.util.SecurityUtil;
import com.xuecheng.media.common.constant.MediaErrorCode;
import com.xuecheng.media.common.utils.AliyunVODUtil;
import com.xuecheng.media.service.MediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AliyunVodController implements AliyunVodApi {

    @Value("${aliyun.region}")
    private String region;
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @PostMapping("media/vod-token")
    public VodUploadToken generateUploadToken(VodUploadRequest uploadRequest) {
        // 当前方法在使用过程中，会有不稳定因素
        // 不稳定因素：网络
        // 特点:第三方的服务
        try {

            DefaultAcsClient client = AliyunVODUtil.initVodClient(region, accessKeyId, accessKeySecret);

            CreateUploadVideoResponse response = AliyunVODUtil.createUploadVideo(client, uploadRequest.getTitle(), uploadRequest.getFileName());


            VodUploadToken token = VodUploadToken.builder().uploadAuth(response.getUploadAuth())
                    .requestId(response.getRequestId())
                    .videoId(response.getVideoId())
                    .uploadAddress(response.getUploadAddress())
                    .build();

            return token;

        } catch (Exception e) {

            // 错误胡记录到日志中，日志文件在本地存储，操作时会实际到io
            // 在实际工作中，绝对不要出现。
            // e.printStackTrace();
            log.error(MediaErrorCode.E_140011.getDesc() + " error msg: {}", e.getMessage());
            ExceptionCast.cast(MediaErrorCode.E_140011);
            return null;
        }
    }

    @GetMapping("media/refresh-vod-token/{videoId}")
    public VodUploadToken refreshUploadToken(String videoId) {
        // 当前方法在使用过程中，会有不稳定因素
        // 不稳定因素：网络
        // 特点:第三方的服务
        try {

            DefaultAcsClient client = AliyunVODUtil.initVodClient(region, accessKeyId, accessKeySecret);

            RefreshUploadVideoResponse response = AliyunVODUtil.refreshUploadVideo(client, videoId);

            VodUploadToken token = VodUploadToken.builder().uploadAuth(response.getUploadAuth())
                    .requestId(response.getRequestId())
                    .videoId(response.getVideoId())
                    .uploadAddress(response.getUploadAddress())
                    .build();

            return token;

        } catch (Exception e) {

            // 错误胡记录到日志中，日志文件在本地存储，操作时会实际到io
            // 在实际工作中，绝对不要出现。
            // e.printStackTrace();
            log.error(MediaErrorCode.E_140015.getDesc() + " error msg: {}", e.getMessage());
            ExceptionCast.cast(MediaErrorCode.E_140015);
            return null;
        }
    }

    @Autowired
    private MediaService mediaService;

    @GetMapping("media/preview/{mediaId}")
    public String getPlayUrlByMediaId(@PathVariable Long mediaId) {
        Long companyId = SecurityUtil.getCompanyId();
        return mediaService.getPlayUrlByMediaId(mediaId, companyId);
    }

    @GetMapping("m/media/preview/{mediaId}")
    public String getPlayUrlByMediaId4Audit(@PathVariable Long mediaId) {
        return mediaService.getPlayUrlByMediaId(mediaId, MediaAuditController.OPERATION_FLAG);
    }
}
