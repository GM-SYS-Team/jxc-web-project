package com.gms.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="image.server")
public class ImageServerProperties
{
  private String sourcePath;
  private String quickMark;
  private String couponMark;
  private String customerMark;

  public String getCouponMark() {
	return couponMark;
}
public void setCouponMark(String couponMark) {
	this.couponMark = couponMark;
}
public String getCustomerMark() {
	return customerMark;
}
public void setCustomerMark(String customerMark) {
	this.customerMark = customerMark;
}
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