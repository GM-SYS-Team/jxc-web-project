package com.gms.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gms.common.EncodeUtil;
import com.gms.conf.ImageServerProperties;
import com.gms.conf.QuickMarkProperties;
import com.gms.conf.ResultData;
import com.gms.util.Constant;
import com.gms.util.DateUtil;
import com.gms.util.StringUtil;

@Controller
public class FileUpController
{

  @Value("${picuploadPath}")
  private String picturePath;
  @Value("${quickuploadPath}")
  private String quickuploadPath;
  @Value("${couponuploadPath}")
  private String couponuploadPath;
  @Value("${customeruploadPath}")
  private String customeruploadPath;
  private String systemPath = System.getProperty("user.dir");

  @Autowired
  private ImageServerProperties imageServerProperties;
  @Autowired
  private QuickMarkProperties quickMarkProperties;
  private static final Logger logger = LoggerFactory.getLogger(FileUpController.class);

  @ResponseBody
  @RequestMapping(value={"picture/upload"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ResultData upload(@RequestParam("pictureFile") MultipartFile pictureFile) { if (pictureFile.isEmpty()) {
      return ResultData.forbidden().putDataValue("messageInfo", "文件不能为空");
    }

    String fileName = pictureFile.getOriginalFilename();
    logger.info("上传的文件名为：" + fileName);

    String suffixName = fileName.substring(fileName.lastIndexOf("."));
    logger.info("上传的后缀名为：" + suffixName);

    fileName = DateUtil.getCurrentTime() + suffixName;
    String realPath = systemPath.replaceAll("\\\\", "//");
    File dest = new File(realPath+this.picturePath + fileName);

    if (!dest.getParentFile().exists())
      dest.getParentFile().mkdirs();
    try
    {
      pictureFile.transferTo(dest);
      ResultData resultData = ResultData.ok().putDataValue("imageName", this.imageServerProperties.getSourcePath() + fileName);
      return resultData.putDataValue("messageInfo", "头像上传成功");
    } catch (IllegalStateException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ResultData.serverInternalError().putDataValue("messageInfo", "服务器请假了，请稍后再试"); 
}
  
  @ResponseBody
  @RequestMapping(value={"quickMark/upload"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  public ResultData quickMarkUpload(@RequestParam("quickMarkStr") String quickMarkStr,@RequestParam("markType") String markType) { if (!StringUtil.isValid(quickMarkStr)) {
      return ResultData.forbidden().putDataValue("messageInfo", "商铺ID不能为空");
    }
	String realPath = systemPath.replaceAll("\\\\", "//");
    String fileName = DateUtil.getCurrentTime() + quickMarkProperties.getType();
    byte[] quickMarkImage = EncodeUtil.encodeShop(quickMarkProperties.getRows(), quickMarkProperties.getCols(),
			quickMarkProperties.getModelSize(), quickMarkProperties.getQzsize(), quickMarkStr);
    String markRealFileName = null;
    File dest = null;
    if(markType.equals(Constant.QUICK_MARK_SHOP_TYPE)){
    	markRealFileName = realPath+this.quickuploadPath + fileName;
    }else if(markType.equals(Constant.QUICK_MARK_COUPON_TYPE)){
    	markRealFileName = realPath+this.couponuploadPath + fileName;
    }else if(markType.equals(Constant.QUICK_MARK_CUSTOMER_TYPE)){
    	markRealFileName = realPath+this.customeruploadPath + fileName;
    }
    dest = new File(markRealFileName);

    if (!dest.getParentFile().exists())
      dest.getParentFile().mkdirs();
    FileOutputStream out=null;
    try
    {
    	out = new FileOutputStream(dest,false);
		out.write(quickMarkImage);
		ResultData resultData = ResultData.ok().putDataValue("quickMark", this.imageServerProperties.getQuickMark() + fileName);
		return resultData.putDataValue("messageInfo", "二维码上传成功");
    }catch (FileNotFoundException e1) {
		e1.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (IllegalStateException e) {
      e.printStackTrace();
    }finally{
    	try {
			out.close();
		} catch (IOException e) {
			logger.info("商铺二维码生成数据流关闭异常，商铺ID为：" + quickMarkStr);
		}
    }
    return ResultData.serverInternalError().putDataValue("messageInfo", "服务器请假了，请稍后再试"); 
}

  @RequestMapping({"/download"})
  public String downloadFile(HttpServletRequest request, HttpServletResponse response)
  {
    String fileName = "FileUploadTests.java";
    if (fileName != null)
    {
      String realPath = request.getServletContext().getRealPath("//WEB-INF//");
      File file = new File(realPath, fileName);
      if (file.exists()) {
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", 
          "attachment;fileName=" + fileName);
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
          fis = new FileInputStream(file);
          bis = new BufferedInputStream(fis);
          OutputStream os = response.getOutputStream();
          int i = bis.read(buffer);
          while (i != -1) {
            os.write(buffer, 0, i);
            i = bis.read(buffer);
          }
          System.out.println("success");
        } catch (Exception e) {
          e.printStackTrace();

          if (bis != null) {
            try {
              bis.close();
            } catch (IOException ee) {
              ee.printStackTrace();
            }
          }
          if (fis != null)
            try {
              fis.close();
            } catch (IOException ee) {
              ee.printStackTrace();
            }
        }
        finally
        {
          if (bis != null) {
            try {
              bis.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          if (fis != null) {
            try {
              fis.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
    return null;
  }
  @RequestMapping(value={"/batch/upload"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public String handleFileUpload(HttpServletRequest request) { List files = ((MultipartHttpServletRequest)request)
      .getFiles("file");
    MultipartFile file = null;
    BufferedOutputStream stream = null;
    for (int i = 0; i < files.size(); i++) {
      file = (MultipartFile)files.get(i);
      if (!file.isEmpty())
        try {
          byte[] bytes = file.getBytes();
          stream = new BufferedOutputStream(
            new FileOutputStream(new File(file.getOriginalFilename())));
          stream.write(bytes);
          stream.close();
        }
        catch (Exception e) {
          stream = null;
          return "You failed to upload " + i + " => " + 
            e.getMessage();
        }
      else {
        return "You failed to upload " + i + 
          " because the file was empty.";
      }
    }
    return "upload successful";
  }
}