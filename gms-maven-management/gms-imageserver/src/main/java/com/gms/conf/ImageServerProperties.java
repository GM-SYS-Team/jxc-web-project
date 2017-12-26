package com.gms.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="image.server")
public class ImageServerProperties
{
  private String sourcePath;
  private String shopMark;
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
  public String getShopMark() {
	return shopMark;
}
public void setShopMark(String shopMark) {
	this.shopMark = shopMark;
}
public String getSourcePath() {
    return this.sourcePath;
  }
  public void setSourcePath(String sourcePath) {
    this.sourcePath = sourcePath;
  }
}