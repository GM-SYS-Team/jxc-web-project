package com.gms.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="image.server")
public class ImageServerProperties
{
  private String sourcePath;
  private String quickMark;

  public String getQuickMark() {
	return quickMark;
}
public void setQuickMark(String quickMark) {
	this.quickMark = quickMark;
}
  public String getSourcePath() {
    return this.sourcePath;
  }
  public void setSourcePath(String sourcePath) {
    this.sourcePath = sourcePath;
  }
}