package com.lyk.coursearrange.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyk.coursearrange.common.FileInfo;
import com.lyk.coursearrange.common.ServerResponse;
import com.lyk.coursearrange.dao.DocDao;
import com.lyk.coursearrange.entity.Doc;
import com.lyk.coursearrange.entity.request.DocsVO;
import com.lyk.coursearrange.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lequal
 * @since 2020-05-27
 */
@Service
public class DocServiceImpl extends ServiceImpl<DocDao, Doc> implements DocService {

  @Autowired
  private DocService docService;
  @Autowired
  private MinioUtil minioUtil;
  @Value("${minio.bucketName}")
  private String bucketName;

  @Override
  public ServerResponse uploadDocs(MultipartFile file) throws Exception {
    FileInfo fileInfo = minioUtil.uploadFile(file.getInputStream(), bucketName, file.getOriginalFilename());
    return ServerResponse.ofSuccess(fileInfo);
  }

  @Override
  public ServerResponse downloadDocs(Integer id) {
    return null;
  }

  @Override
  public ServerResponse addDcos(DocsVO d) {
    Doc doc = new Doc();
    doc.setDescription(d.getDescription());
    doc.setDocName(d.getDocName());
    doc.setFileName(d.getFileName());
    doc.setDocUrl(d.getDocUrl());
    doc.setExpire(d.getExpire());
    doc.setFromUserId(d.getFromUserId());
    doc.setFromUserName(d.getFromUserName());
    doc.setFromUserType(d.getFromUserType());
    doc.setToClassNo(d.getToClassNo());

    boolean b = docService.save(doc);

    if (b) {
      return ServerResponse.ofSuccess("添加文档成功");
    }
    return ServerResponse.ofError("添加文档失败");
  }
}
