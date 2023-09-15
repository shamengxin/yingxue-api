package com.shamengxin.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.extension.p6spy.P6SpyLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Component
public class OSSUtils {


    private static String ACCESSKEYID;

    private static String SECRET;

    private static String endpoint;

    private static String bucketName;

    private static String bucketIp;

    @Value("${oss.accessKeyId}")
    public static void setACCESSKEYID(String ACCESSKEYID) {
        OSSUtils.ACCESSKEYID = ACCESSKEYID;
    }
    @Value("${oss.secret}")
    public static void setSECRET(String SECRET) {
        OSSUtils.SECRET = SECRET;
    }
    @Value("${oss.endpoint}")
    public static void setEndpoint(String endpoint) {
        OSSUtils.endpoint = endpoint;
    }
    @Value("${oss.bucketName}")
    public static void setBucketName(String bucketName) {
        OSSUtils.bucketName = bucketName;
    }
    @Value("${oss.bucketIp}")
    public static void setBucketIp(String bucketIp) {
        OSSUtils.bucketIp = bucketIp;
    }

    public static String upload(File file, String path, String fileName) throws FileNotFoundException {
        return upload(new FileInputStream(file), path,fileName);
    }

    /**
     * 上传文件
     */
    public static String upload(InputStream inputStream,String path,String fileName){
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, ACCESSKEYID, SECRET);
        String key = path+ "/" +fileName;
        ossClient.putObject(bucketName,key,inputStream);
        ossClient.shutdown();
        return bucketIp;
    }

}
