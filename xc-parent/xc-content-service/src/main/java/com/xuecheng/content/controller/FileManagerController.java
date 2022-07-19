package com.xuecheng.content.controller;

import com.xuecheng.api.content.FileManagerApi;
import com.xuecheng.api.content.model.file.UploadTokenResult;
import com.xuecheng.common.exception.ExceptionCast;
import com.xuecheng.content.common.constant.ContentErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class FileManagerController implements FileManagerApi {

    /**
     * 从nacos配置中心获取配置
     */
    @Value("${file.service.url}")
    private String url;
    @Value("${file.service.bucket}")
    private String bucket;
    @Value("${file.service.upload.region}")
    private String region;
    @Value("${cdn.domain}")
    private String domain;
    @Value("${file.token.type}")
    private String type;
    @Value("${file.token.deadline}")
    private Integer deadline;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("common/qnUploadToken")
    public UploadTokenResult generateUploadToken() {
        HashMap<String, Object> requestBody = new HashMap<>();
        //1.发送请求到文件服务、获得文件上传的凭证
        /**
         * Post请求参数
         * 1.访问路径url
         * 2.请求体的数据
         * 3.返回结果的数据
         * 4.QueryString的参数
         */
        //远程调用的数据封装
        requestBody.put("tokenType", type);
        requestBody.put("scope", bucket);
        requestBody.put("deadline", deadline);
        //文件名称需要确保唯一性
        String key = UUID.randomUUID().toString() + RandomStringUtils.randomAlphanumeric(12);
        requestBody.put("key", key);
        //远程调用
        ResponseEntity<Map> responseEntity = null;
        try {
            responseEntity = restTemplate.postForEntity(url, requestBody, Map.class);
        } catch (RestClientException e) {
            //e.printStackTrace();禁止抛出默认异常
            //记录异常信息：msg；抛出统一的异常信息
            log.error(ContentErrorCode.E_120022.getDesc() + "errorMsg:{}", e.getMessage());
            ExceptionCast.cast(ContentErrorCode.E_120022);
        }
        //2.封装前端需要的结果
        //获取状态码
        HttpStatus statusCode = responseEntity.getStatusCode();
        Map body = responseEntity.getBody();
        //判断状态码是否为成功
        if (HttpStatus.OK == statusCode) {
            //响应成功的结果—上传的凭证：result
            Object token = body.get("result");
            return UploadTokenResult.builder()
                    .tokenType(type)
                    .scope(bucket)
                    .key(key)
                    .qnToken(token.toString())
                    .up_region(region)
                    .domain(domain)
                    .build();
        } else {
            //响应失败获得错误数据
            Object code = body.get("code");
            Object msg = body.get("msg");
            ExceptionCast.castWithCodeAndDesc(new Integer(code.toString()), msg.toString());
            return null;
        }
    }
}
