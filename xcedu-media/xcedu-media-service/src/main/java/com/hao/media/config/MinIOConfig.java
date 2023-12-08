package com.hao.media.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description MinIO配置
 * @author hao
 */

@Configuration
public class MinIOConfig {
    @Value("${minIO.endpoint}")
    private String endPoint;
    @Value("${minIO.accessKey}")
    private String accessKey;
    @Value("${minIO.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minIOClient(){
        MinioClient minioclient = MinioClient
                .builder()
                .endpoint(endPoint)
                .credentials(accessKey,secretKey)
                .build();

        return minioclient;
    }
}
