package com.lyk.coursearrange.controller;

import com.lyk.coursearrange.service.impl.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
public class MinioController {

  @Autowired
  private MinioUtil minioUtil;

  @Value("${minio.bucketName}")
  private String bucketName;


  @PostMapping(value = "/uploadFile",
      consumes = {"multipart/form-data"})
  public Integer fileupload(@RequestParam MultipartFile uploadFile) throws Exception {
    minioUtil.uploadFile(uploadFile.getInputStream(), bucketName, uploadFile.getOriginalFilename());
    return 1;
  }

  @GetMapping(value = "/downloadFile")
  public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) throws Exception {
    InputStream stream = minioUtil.download(bucketName, fileName);
    // 设置响应头
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName); // 设置下载文件的文件名

    // 返回响应实体
    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(new InputStreamResource(stream));
  }
}

