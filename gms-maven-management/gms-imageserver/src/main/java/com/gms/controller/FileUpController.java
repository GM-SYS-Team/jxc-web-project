package com.gms.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gms.common.EncodeUtil;
import com.gms.conf.ImageServerProperties;
import com.gms.conf.QuickMarkProperties;
import com.gms.conf.ResultData;
import com.gms.util.Constant;
import com.gms.util.DateUtil;
import com.gms.util.StringUtil;
import com.gms.util.UUIDUtil;

@Controller
public class FileUpController
{

  @Value("${headuploadPath}")
  private String headPicturePath;
  @Value("${goodsUploadPath}")
  private String goodsUploadPath;
  @Value("${quickuploadPath}")
  private String quickuploadPath;
  @Value("${couponuploadPath}")
  private String couponuploadPath;
  @Value("${customeruploadPath}")
  private String customeruploadPath;
/*  private String systemPath = System.getProperty("user.dir");*/

  @Autowired
  private ImageServerProperties imageServerProperties;
  @Autowired
  private QuickMarkProperties quickMarkProperties;
  private static final Logger logger = LoggerFactory.getLogger(FileUpController.class);
  /**
	* @author zhoutianqi
	* @date 2017年12月27日 下午2:17:46
	* @param pictureFile 图片文件名称MultipartFile
	* @param picType 图片类别   HEAD_SHOT(头像图片)  GOODS_PIC(商品图片)  类别在com.common.Constant
	* @description 处理二维码定制参数
	* @return 
	*/
  @ResponseBody
  @RequestMapping(value={"picture/upload"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ResultData upload(@RequestParam("pictureFile") MultipartFile pictureFile,@RequestParam("picType") String picType){ 
	if (pictureFile.isEmpty()) {
      return ResultData.forbidden().putDataValue("messageInfo", "文件不能为空");
    }
	String realImagePath = null;
	String realUrlPath = null;
	if(picType.equals(Constant.HEAD_SHOT)){
		//用户头像
		realImagePath = headPicturePath;
		realUrlPath = imageServerProperties.getHeadShot();
	}else if(picType.equals(Constant.GOODS_PIC)){
		//商品头像
		realImagePath = goodsUploadPath;
		realUrlPath = imageServerProperties.getGoodsPic();
	}else {
		return ResultData.forbidden().putDataValue("messageInfo", "图片类型格式不正确");
	}
    String fileName = pictureFile.getOriginalFilename();
    logger.info("上传的文件名为：" + fileName);

    String suffixName = fileName.substring(fileName.lastIndexOf("."));
    logger.info("上传的后缀名为：" + suffixName);
    //yyyyMMddhhmmss+32位uuid
    fileName = DateUtil.getCurrentTime()+ UUIDUtil.getUUIDKey() + suffixName;
    File dest = new File(realImagePath + fileName);
    if (!dest.getParentFile().exists())
      dest.getParentFile().mkdirs();
    try
    {
      pictureFile.transferTo(dest);
      ResultData resultData = ResultData.ok().putDataValue("imageName", fileName);
      resultData.putDataValue("url", imageServerProperties.getHostaddress()+realUrlPath+fileName);
      return resultData.putDataValue("messageInfo", "头像上传成功");
    } catch (IllegalStateException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ResultData.serverInternalError().putDataValue("messageInfo", "服务器请假了，请稍后再试"); 
}
  /**
	* @author zhoutianqi
	* @date 2017年12月27日 下午2:17:46
	* @param quickMarkStr 生成二维码的key
	* @param markType 区别二维码类别   SHOP(商铺二维码)  COUPON(优惠券二维码) CUSTOMER(用户领取的卷对应的二维码-如果需要的话)
	* @param quickMarkRows 二维码宽度，服务器端默认值：13
	* @param quickMarkCols 二维码长度，服务器端默认值：41
	* @param quickMarkModelSize 单元模块大小，服务器端默认值：2
	* @param quickMarkQzsize 空白区16像素，服务器端默认值：1
	* @param quickMarkType 文件后缀，服务器端默认值：.png
	* @description 处理二维码定制参数
	*/
  @ResponseBody
  @RequestMapping(value={"quickMark/upload"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ResultData quickMarkUpload(@RequestParam("quickMarkStr") String quickMarkStr,@RequestParam("markType") String markType,
		  @RequestParam("quickMarkRows") String quickMarkRows,@RequestParam("quickMarkCols") String quickMarkCols,@RequestParam("quickMarkModelSize") String quickMarkModelSize,
		  @RequestParam("quickMarkQzsize") String quickMarkQzsize,@RequestParam("quickMarkType") String quickMarkType) { if (!StringUtil.isValid(quickMarkStr)) {
      return ResultData.forbidden().putDataValue("messageInfo", "商铺ID不能为空");
    }
		  FileOutputStream out=null;
    try
    {
		//处理二维码定制参数
		dealQuickMarkProperties(quickMarkRows,quickMarkCols,quickMarkModelSize,quickMarkQzsize,quickMarkType);
		/*String realPath = systemPath.replaceAll("\\\\", "//");*/
	    String fileName = DateUtil.getCurrentTime()+ UUIDUtil.getUUIDKey() + quickMarkProperties.getType();
	    byte[] quickMarkImage = EncodeUtil.encodeShop(quickMarkProperties.getRows(), quickMarkProperties.getCols(),
				quickMarkProperties.getModelSize(), quickMarkProperties.getQzsize(), quickMarkStr,quickMarkProperties.getType());
	    String markRealFileName = null;
	    String successMsg = null;//json回传结果
	    String realUrlPath = null;//图片url包路径
	    File dest = null;
	    if(markType.equals(Constant.QUICK_MARK_SHOP_TYPE)){
	    	markRealFileName = this.quickuploadPath + fileName;
	    	successMsg = this.imageServerProperties.getShopMark() + fileName;
	    	realUrlPath = this.imageServerProperties.getShopMark();
	    }else if(markType.equals(Constant.QUICK_MARK_COUPON_TYPE)){
	    	markRealFileName = this.couponuploadPath + fileName;
	    	successMsg = this.imageServerProperties.getCouponMark() + fileName;
	    	realUrlPath = this.imageServerProperties.getCouponMark();
	    }else if(markType.equals(Constant.QUICK_MARK_CUSTOMER_TYPE)){
	    	markRealFileName = this.customeruploadPath + fileName;
	    	successMsg = this.imageServerProperties.getCustomerMark() + fileName;
	    	realUrlPath = this.imageServerProperties.getCustomerMark();
	    }
	    dest = new File(markRealFileName);
	
	    if (!dest.getParentFile().exists())
	      dest.getParentFile().mkdirs();
    	out = new FileOutputStream(dest,false);
		out.write(quickMarkImage);
		ResultData resultData = ResultData.ok().putDataValue("quickMark", successMsg);
		resultData.putDataValue("url", imageServerProperties.getHostaddress()+realUrlPath+fileName);
		logger.info("商铺二维码生成成功，商铺ID为：" + quickMarkStr);
		return resultData.putDataValue("messageInfo", "二维码上传成功");
    }catch (FileNotFoundException e1) {
		e1.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (IllegalStateException e) {
      e.printStackTrace();
    }catch (Exception e) {
        e.printStackTrace();
      }finally{
    	try {
    		if(out!=null){
    			out.close();
    		}
		} catch (IOException e) {
			logger.info("商铺二维码生成数据流关闭异常，商铺ID为：" + quickMarkStr);
		}
    }
    return ResultData.serverInternalError().putDataValue("messageInfo", "服务器请假了，请稍后再试"); 
}
  /**
	* @author zhoutianqi
	* @date 2017年12月27日 下午2:17:46
	* @description 处理二维码定制参数
	*/
  private void dealQuickMarkProperties(String quickMarkRows,
		String quickMarkCols, String quickMarkModelSize,
		String quickMarkQzsize, String quickMarkType) {
	if(!StringUtil.isEmpty(quickMarkRows)){
		quickMarkProperties.setRows(Integer.parseInt(quickMarkRows));
	}
	if(!StringUtil.isEmpty(quickMarkCols)){
		quickMarkProperties.setCols(Integer.parseInt(quickMarkCols));
	}
	if(!StringUtil.isEmpty(quickMarkModelSize)){
		quickMarkProperties.setModelSize(Integer.parseInt(quickMarkModelSize));
	}
	if(!StringUtil.isEmpty(quickMarkQzsize)){
		quickMarkProperties.setQzsize(Integer.parseInt(quickMarkQzsize));
	}
	if(!StringUtil.isEmpty(quickMarkType)){
		quickMarkProperties.setType(quickMarkType);
	}
}
}