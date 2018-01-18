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
  private String ios;//ios app路径
  private String android;//app路径 app路径
  private int imageWidth = 200;//图片宽度 默认200px
  private int imageHight = 200;//图片高度 默认200px
  private int imageDefaultMaxSize = 1;//图片默认不能大于1M
  private boolean compressImage = false;//图片压缩默认不开启

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
public int getImageWidth() {
	return imageWidth;
}
public void setImageWidth(int imageWidth) {
	this.imageWidth = imageWidth;
}
public int getImageHight() {
	return imageHight;
}
public void setImageHight(int imageHight) {
	this.imageHight = imageHight;
}
public int getImageDefaultMaxSize() {
	return imageDefaultMaxSize;
}
public void setImageDefaultMaxSize(int imageDefaultMaxSize) {
	this.imageDefaultMaxSize = imageDefaultMaxSize;
}
public boolean isCompressImage() {
	return compressImage;
}
public void setCompressImage(boolean compressImage) {
	this.compressImage = compressImage;
}
public String getIos() {
	return ios;
}
public void setIos(String ios) {
	this.ios = ios;
}
public String getAndroid() {
	return android;
}
public void setAndroid(String android) {
	this.android = android;
}

}