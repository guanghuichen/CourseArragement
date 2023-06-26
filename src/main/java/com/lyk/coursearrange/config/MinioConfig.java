package com.lyk.coursearrange.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
public class MinioConfig {
  @Value("${minio.url}")
  private  String url;
  @Value("${minio.accessKey}")
  private String accessKey;
  @Value("${minio.secretKey}")
  private String secretKey;


  @Bean
  public MinioClient getMinioClient() {
    MinioClient minioClient = MinioClient
        .builder()
        .endpoint(url)
        .credentials(accessKey, secretKey)
        .build();
    return minioClient;
  }
}

