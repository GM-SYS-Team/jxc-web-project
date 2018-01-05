package com.gms.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="image.server")
public class ImageServerProperties
{
  private String hostaddress;//
  private String headShot;//用户头像地址
  private String goodsPic;//商品头像地址
  private String shopMark;//商铺二维码地址
  private String couponMark;//优惠券地址
  private String customerMark;//客户领取卷二维码地址
  private String realMark;//合成的优惠券地址

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
public String getHeadShot() {
	return headShot;
}
public void setHeadShot(String headShot) {
	this.headShot = headShot;
}
public String getGoodsPic() {
	return goodsPic;
}
public void setGoodsPic(String goodsPic) {
	this.goodsPic = goodsPic;
}
public String getHostaddress() {
	return hostaddress;
}
public void setHostaddress(String hostaddress) {
	this.hostaddress = hostaddress;
}
public String getRealMark() {
	return realMark;
}
public void setRealMark(String realMark) {
	this.realMark = realMark;
}

}