package com.xuecheng.content.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;

public class QiniuTest {
    @Test
    public void testFileUpload() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.huadong());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "YGIFrtclH9jrkvvqxgkJIXzzJJPZ1s60ZqKiwId_";
        String secretKey = "Cp-CHfMreH-SPFI4mBw20GWA3LT3LHZcVoMZmS7G";
        String bucket = "145-project2-xc";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\Sonder\\Desktop\\wallhaven-g83ydl_1920x1080.png";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "null";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet =
                    new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            System.out.println(putRet.key);
            System.out.println(putRet.hash);

        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }
}
