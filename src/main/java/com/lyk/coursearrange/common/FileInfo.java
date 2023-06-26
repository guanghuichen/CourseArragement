package com.lyk.coursearrange.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileInfo {
  private String url;
  private String name;
}
