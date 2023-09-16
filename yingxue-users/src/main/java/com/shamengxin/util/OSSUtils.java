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


    private static String ACCESSKEYID="LTAI5t5fVrZJgkr9EAR415bF";

    private static String SECRET="nQ8MpFhXiCPISzAGBRqZemVJkoBq5t";


    /**
     * 上传文件
     */
    public static String upload(InputStream inputStream,String path,String fileName){
        // 创建OSSClient实例
        String endpoint="http://oss-cn-wuhan-lr.aliyuncs.com";
        String bucketName="yingxue-shamengxin";
        OSS ossClient = new OSSClientBuilder().build(endpoint, ACCESSKEYID, SECRET);
        String key = path+ "/" +fileName;
        ossClient.putObject(bucketName,key,inputStream);
        ossClient.shutdown();
        return "https://"+bucketName+".oss-cn-wuhan-lr.aliyuncs.com";
    }

}
