package com.gms.controller;


import java.awt.image.BufferedImage;
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

import com.gms.conf.ImageServerProperties;
import com.gms.conf.QuickMarkProperties;
import com.gms.conf.ResultData;
import com.gms.serverutil.EncodeUtil;
import com.gms.serverutil.QuickMarkMergeUtil;
import com.gms.util.Constant;
import com.gms.util.DateUtil;
import com.gms.util.StringUtil;
import com.gms.util.UUIDUtil;

@Controller
public class FileUpController
{

  @Value("${nikehomedir}")
  private String nikehomedir;
  @Value("${quickhomedir}")
  private String quickhomedir;
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
		realImagePath = nikehomedir + imageServerProperties.getHeadShot();
		realUrlPath = imageServerProperties.getHeadShot();
	}else if(picType.equals(Constant.GOODS_PIC)){
		//商品头像
		realImagePath = nikehomedir + imageServerProperties.getGoodsPic();
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
      return resultData.putDataValue("messageInfo", "上传成功");
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
  public ResultData quickMarkUpload(@RequestParam(value="quickMarkStr",required=true) String quickMarkStr,@RequestParam(value="markType",required=true) String markType,
		  @RequestParam(value="quickMarkRows",required=false) String quickMarkRows,@RequestParam(value="quickMarkCols",required=false) String quickMarkCols
		  ,@RequestParam(value="quickMarkModelSize",required=false) String quickMarkModelSize,@RequestParam(value="quickMarkQzsize",required=false) String quickMarkQzsize,
		  @RequestParam(value="quickMarkType",required=false) String quickMarkType){ 
	  if (!StringUtil.isValid(quickMarkStr)) {
      return ResultData.forbidden().putDataValue("messageInfo", "商铺uuid不能为空");
    }
		  FileOutputStream out=null;
		  /* test str here start  */
		  String testStr = null;
		  String testsuccessMsg = null;
		  /* test str here end  */
    try
    {
		//处理二维码定制参数
		dealQuickMarkProperties(quickMarkRows,quickMarkCols,quickMarkModelSize,quickMarkQzsize,quickMarkType);
		/*String realPath = systemPath.replaceAll("\\\\", "//");*/
	    String fileName = DateUtil.getCurrentTime()+ UUIDUtil.getUUIDKey() + quickMarkProperties.getType();
	    //logger.info("二维码属性quickMarkProperties：" + quickMarkProperties.toString());
	    logger.info("开始生成二维码，quickMarkStr为：" + quickMarkStr);
	    byte[] quickMarkImage = EncodeUtil.encodeShop(quickMarkProperties.getRows(), quickMarkProperties.getCols(),
				quickMarkProperties.getModelSize(), quickMarkProperties.getQzsize(), quickMarkStr,quickMarkProperties.getType());
	    if(quickMarkImage==null){
	    	logger.info("byte[] quickMarkImage对象为null");
	    }else{
	    	 logger.info("生成二维码，byte[] quickMarkImage长度为：" + quickMarkImage.length);
	    }
	    String markRealFileName = null;
	    String successMsg = null;//json回传结果
	    String realUrlPath = null;//图片url包路径
	    File dest = null;
	    if(markType.equals(Constant.QUICK_MARK_SHOP_TYPE)){
	    	markRealFileName = quickhomedir + imageServerProperties.getShopMark() + fileName;
	    	successMsg = this.imageServerProperties.getShopMark() + fileName;
	    	realUrlPath = this.imageServerProperties.getShopMark();
	    }else if(markType.equals(Constant.QUICK_MARK_COUPON_TYPE)){
	    	markRealFileName = quickhomedir + imageServerProperties.getCouponMark() + fileName;
	    	successMsg = this.imageServerProperties.getCouponMark() + fileName;
	    	realUrlPath = this.imageServerProperties.getCouponMark();
	    }else if(markType.equals(Constant.QUICK_MARK_CUSTOMER_TYPE)){
	    	markRealFileName = quickhomedir + imageServerProperties.getCustomerMark() + fileName;
	    	successMsg = this.imageServerProperties.getCustomerMark() + fileName;
	    	realUrlPath = this.imageServerProperties.getCustomerMark();
	    }else{
	    	return ResultData.forbidden().putDataValue("messageInfo", "markType参数异常");
	    }
	    dest = new File(markRealFileName);
	    logger.info("开始生成商铺二维码名称为：" + markRealFileName);
	    if (!dest.getParentFile().exists())
	      dest.getParentFile().mkdirs();
    	out = new FileOutputStream(dest,false);
    	/* test str here start  */
    	testStr = imageServerProperties.getHostaddress()+realUrlPath+fileName;
    	testsuccessMsg = successMsg;
    	/* test str here end  */
		out.write(quickMarkImage);
		ResultData resultData = ResultData.ok().putDataValue("quickMark", successMsg);
		resultData.putDataValue("url", imageServerProperties.getHostaddress()+realUrlPath+fileName);
		logger.info("商铺二维码生成成功，商铺ID为：" + quickMarkStr);
		return resultData.putDataValue("messageInfo", "上传成功");
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
    ResultData resultData = ResultData.ok().putDataValue("quickMark", testsuccessMsg);
	resultData.putDataValue("url", testStr);
	logger.info("商铺二维码生成成功，商铺ID为：" + quickMarkStr);
	return resultData.putDataValue("messageInfo", "二维码上传成功");
    //return ResultData.serverInternalError().putDataValue("messageInfo", "服务器请假了，请稍后再试"); 
}
  /**
	* @author zhoutianqi
	* @date 2017年12月27日 下午2:17:46
	* @param picAddress 图片地址
	* @param type= NICK_PATH_TYPE(删除商品图片、用户头像)  QUICK_PATH_TYPE(删除其他图片) 
	* @description 处理二维码定制参数
	*/
  	@ResponseBody
	@RequestMapping(value={"static/pic/delete"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public ResultData deleteStaticPic(@RequestParam(value="picAddress",required=true) String picAddress,
			@RequestParam(value="type",required=true) String type){
  		if (!StringUtil.isValid(picAddress) || !picAddress.contains("static")) {
  	      return ResultData.forbidden().putDataValue("messageInfo", "资源地址为空或者资源不存在");
  	    }
  		try {
  			String realPicDir = null;
  			//图片文件存在两个路径下面 哎
  			if(type.equals(Constant.NICK_PATH_TYPE)){
  				realPicDir = nikehomedir + picAddress.split("static/")[1];
  			}else if(type.equals(Constant.QUICK_PATH_TYPE)){
  				realPicDir = quickhomedir + picAddress.split("static/")[1];
  			}
  	  		File file=new File(realPicDir);
  	        if(file.exists()&&file.isFile())
  	        file.delete();
		} catch (Exception e) {
			logger.info("文件删除失败");
			ResultData.serverInternalError().putDataValue("messageInfo", "删除失败");
		}
  		logger.info("文件删除成功");
  		return ResultData.ok().putDataValue("messageInfo", "删除成功");
	}
  	
  	/**
	* @author zhoutianqi
	* @date 2017年12月27日 下午2:17:46
	* @param picAddress 优惠卷二维码框与商品图片合成
	* @param quickAddress 二维码框图片HTTP地址
	* 		 goodsAddress 商品图片地址
	* @description 处理二维码定制参数
	*/
  	@ResponseBody
	@RequestMapping(value={"quick/mark/merge"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public ResultData mergeFile(@RequestParam(value="quickAddress",required=true) String quickAddress, 
			@RequestParam(value="goodsAddress",required=true) String goodsAddress){
  		String fileName = null;
  		try {
  			if (!StringUtil.isValid(quickAddress) || !quickAddress.contains("static")
  					|| !StringUtil.isValid(goodsAddress) || !goodsAddress.contains("static")) {
  	  	      return ResultData.forbidden().putDataValue("messageInfo", "资源地址为空或者资源不存在");
  	  	    }
  			//图片文件名
  			fileName = DateUtil.getCurrentTime()+ UUIDUtil.getUUIDKey() + quickMarkProperties.getType();
  			String goodsRealDir = nikehomedir + goodsAddress.split("static/")[1];
  			String quickRealDir = quickhomedir + quickAddress.split("static/")[1];
  			logger.info("文件地址1"+goodsRealDir);
  			logger.info("文件地址2"+quickRealDir);
  			//合成图片的存储地址
  			String realMarkDir = quickhomedir + imageServerProperties.getRealMark() + fileName;
  			logger.info("文件地址"+realMarkDir);
  			// 构建叠加层
  			BufferedImage buffImg = QuickMarkMergeUtil.watermark( new File(quickRealDir), new File(goodsRealDir), 265, 75, 1.0f);
  	        // 输出水印图片
  	        QuickMarkMergeUtil.generateWaterFile(buffImg, realMarkDir);
		} catch (Exception e) {
			ResultData.serverInternalError().putDataValue("messageInfo", "合成文件失败");
		}
  		ResultData result = ResultData.ok();
  		result.putDataValue("url", imageServerProperties.getHostaddress()+imageServerProperties.getRealMark()+fileName);
  		return result.putDataValue("messageInfo", "合成文件成功");
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