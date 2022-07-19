package com.xuecheng.api.content.model.file;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p></p>
 *
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("文件上传凭证封装类")
public class UploadTokenResult {
    private String tokenType;
    private String scope;
    private String key;
    private String qnToken;
    private String up_region;
    private String domain;
    private int deadline;

}