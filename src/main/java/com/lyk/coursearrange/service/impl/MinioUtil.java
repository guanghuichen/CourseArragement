package com.lyk.coursearrange.service.impl;

import com.lyk.coursearrange.common.FileInfo;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static io.minio.GetPresignedObjectUrlArgs.DEFAULT_EXPIRY_TIME;

@Component
public class MinioUtil {

  @Autowired
  private MinioClient minioClient;

  @Value("${minio.url}")
  private String url;

  @Value("${minio.bucketName}")
  private String bucketName;

  /**
   * 创建一个桶
   */
  public void createBucket(String bucket) throws Exception {
    boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    if (!found) {
      minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
    }
  }

  /**
   * 上传一个文件
   */
  public FileInfo uploadFile(InputStream stream, String bucket, String objectName) throws Exception {
    minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(objectName)
        .stream(stream, -1, 10485760).build());
    return composeUrl(objectName);
  }

  /**
   * 列出所有的桶
   */
  public List<String> listBuckets() throws Exception {
    List<Bucket> list = minioClient.listBuckets();
    List<String> names = new ArrayList<>();
    list.forEach(b -> {
      names.add(b.name());
    });
    return names;
  }

  /**
   * 下载一个文件
   */
  public InputStream download(String bucket, String objectName) throws Exception {
    InputStream stream = minioClient.getObject(
        GetObjectArgs.builder().bucket(bucket).object(objectName).build());
    return stream;
  }

  /**
   * 删除一个桶
   */
  public void deleteBucket(String bucket) throws Exception {
    minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
  }

  /**
   * 删除一个对象
   */
  public void deleteObject(String bucket, String objectName) throws Exception {
    minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
  }

  public String preSignedGetObject(String bucketName, String objectName, Integer expires) throws Exception {
    String fileUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
        .method(Method.GET)
        .bucket(bucketName)
        .object(objectName)
        .expiry(Objects.isNull(expires) ? DEFAULT_EXPIRY_TIME : expires)
        .build()
    );
    return fileUrl;
  }

  private FileInfo composeUrl(String fileName) {
    return FileInfo.builder().url(url + "/" + bucketName + "/" + fileName).name(fileName).build();
  }
}
