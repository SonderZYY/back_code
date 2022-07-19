package com.xuecheng.media.common.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;
import org.springframework.util.ObjectUtils;

/**
 * <p></p>
 *
 * @Description:
 */
public class AliyunVODUtil {

    private AliyunVODUtil() {
    }

    /**
     * 初始化VOD客户端对象
     * @param regionId       --区域id（华北2：cn-beijing，华东2：cn-shanghai）
     * @param accessKeyId    --秘钥的ackId
     * @param accessKeySecret--秘钥的ackSecret
     * @return
     * @throws ClientException
     */
    public static DefaultAcsClient initVodClient(String regionId, String accessKeyId, String accessKeySecret) throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }


    /**
     * 获取视频上传地址和凭证
     * @param client 发送请求客户端
     * @param fileTitle 流媒体文件的显示名称
     * @param fileName  流媒体文件的原始名称
     * @return CreateUploadVideoResponse 获取视频上传地址和凭证响应数据
     * @throws Exception
     */
    public static CreateUploadVideoResponse createUploadVideo(DefaultAcsClient client,String fileTitle,String fileName) throws Exception {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setTitle(fileTitle);
        request.setFileName(fileName);
        return client.getAcsResponse(request);
    }


    /**
     * 刷新视频上传凭证
     * @param client 发送请求客户端
     * @return RefreshUploadVideoResponse 刷新视频上传凭证响应数据
     * @throws Exception
     */
    public static RefreshUploadVideoResponse refreshUploadVideo(DefaultAcsClient client, String videoId) throws Exception {
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        request.setVideoId(videoId);
        return client.getAcsResponse(request);
    }


    /**
     * 获得媒资播放请求对象
     * @param client
     * @param videoId
     * @return
     * @throws Exception
     */
    public static GetPlayInfoResponse getPlayInfo(DefaultAcsClient client, String videoId) throws Exception {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(videoId);
        return client.getAcsResponse(request);
    }


    /**
     * 删除视频
     * @param client 发送请求客户端
     * @param videoIds 流媒体文件的fileId值
     * @return DeleteVideoResponse 删除视频响应数据
     * @throws Exception
     */
    public static void deleteVideo(DefaultAcsClient client,String... videoIds) throws Exception {
        DeleteVideoRequest request = new DeleteVideoRequest();
        if (ObjectUtils.isEmpty(videoIds)) {
            return ;
        }
        //支持传入多个视频ID，多个用逗号分隔
        String videoIdsStr = String.join(",", videoIds);
        request.setVideoIds(videoIdsStr);
        client.getAcsResponse(request);
    }
}
