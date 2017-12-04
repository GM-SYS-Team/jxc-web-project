package com.gms.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="image.server")
public class ImageServerProperties
{
  private String url;
  private String action;
  private String sourcePath;

  public String getUrl()
  {
    return this.url;
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public String getAction() {
    return this.action;
  }
  public void setAction(String action) {
    this.action = action;
  }
  public String getSourcePath() {
    return this.sourcePath;
  }
  public void setSourcePath(String sourcePath) {
    this.sourcePath = sourcePath;
  }
}