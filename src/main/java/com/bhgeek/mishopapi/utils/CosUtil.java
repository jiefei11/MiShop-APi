package com.bhgeek.mishopapi.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@ConfigurationProperties(prefix = "tencent",ignoreInvalidFields = true,ignoreUnknownFields = true)
public class CosUtil {
    private static String appId;

    private static String secretId;

    private static String secretKey;

    private static String region;

    private static String photoBucket;

    //注入值
    @Value("${tencent.appId}")
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Value("${tencent.secretId}")
    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    @Value("${tencent.secretKey}")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Value("${tencent.region}")
    public void setRegion(String region) {
        this.region = region;
    }

    @Value("${tencent.photoBucket}")
    public void setPhotoBucket(String photoBucket) {
        this.photoBucket = photoBucket;
    }


    /**
     * 初始化CosClient相关配置， appid、accessKey、secretKey、region
     *
     * @return
     */
    public COSClient getCosClient() {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }

    /**
     * 上传文件
     *@Param //MultipartFile:前台传入的文件 FileType:枚举类 根据值不同，实现的功能不同
     * @return //绝对路径和相对路径都OK
     */
    public String uploadFile(MultipartFile file) throws IOException {
        //上传至存储桶的名字
        String KEY = "/" + file.getOriginalFilename();
        String bucket = photoBucket;
        //根据业务模块，上传至其他存储桶

        //获得文件名
        String fileName = file.getOriginalFilename();
        //将图片的具体信息传入ObjectMetadate类
        ObjectMetadata meta=new ObjectMetadata();
        //必须设置该属性
        meta.setContentLength(file.getSize());
        //设置字符编码格式
        meta.setContentEncoding("UTF-8");
        //获得文件后缀名并根据传入的图片格式设置ContentType
        if (".png".equals(fileName.lastIndexOf("."))){
            meta.setContentType("image/png");
        }else if (".jpg".equals(fileName.lastIndexOf("."))){
            meta.setContentType("image/jpeg");
        }
        //SDK构造方法,具体参造[SDKAPI](https://help.aliyun.com/document_detail/32008.htm?spm=a2c4g.11186623.2.3.65ac605fhxBPgG)
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, KEY, file.getInputStream(),meta);
        putObjectRequest.setStorageClass(StorageClass.Standard);
        COSClient client = getCosClient();
        try {
            PutObjectResult putObjectResult = client.putObject(putObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 关闭客户端
        client.shutdown();
        //拼接获得存储桶中可访问的地址
        return "https://"+bucket+".cos."+region+".myqcloud.com"+KEY;
    }

}