import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import org.junit.jupiter.api.Test;

/**
 * <p></p>
 *
 * @Description:
 */
public class AliyunVodTest {
    private String accessKeyId = "LTAI5t8dtbLBswMuNrykpTA7";
    private String accessKeySecret = "Yzo7biOnbXw04LbFu1reznHhsRFGuN";
    private String regionId = "cn-shanghai";

    /**
     * 初始化代码
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return
     * @throws ClientException
     */
    public static DefaultAcsClient initVodClient(String regionId, String accessKeyId, String accessKeySecret) {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    @Test
    public void testUpload() throws ClientException {
        //1.创建请求对象
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setTitle("test001 file");
        request.setFileName("filename.mp4");
        //2.获得客户端对象
        DefaultAcsClient client = initVodClient(regionId, accessKeyId, accessKeySecret);

        // 3.获得响应对象
        CreateUploadVideoResponse response = client.getAcsResponse(request);
        // 4.解析响应对象
        try {
            System.out.print("VideoId = " + response.getVideoId() + "\n");
            System.out.print("UploadAddress = " + response.getUploadAddress() + "\n");
            System.out.print("UploadAuth = " + response.getUploadAuth() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
}