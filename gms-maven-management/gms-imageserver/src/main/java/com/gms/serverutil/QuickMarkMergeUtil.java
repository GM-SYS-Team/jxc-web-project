package com.gms.serverutil;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;
public class QuickMarkMergeUtil {
    /**
     * 
     * @Title: 构造图片
     * @Description: 生成水印并返回java.awt.image.BufferedImage
     * @param file
     *            源文件(图片)
     * @param waterFile
     *            水印文件(图片)
     * @param x
     *            距离右下角的X偏移量
     * @param y
     *            距离右下角的Y偏移量
     * @param alpha
     *            透明度, 选择值从0.0~1.0: 完全透明~完全不透明
     * @return BufferedImage
     * @throws IOException
     */
    public static BufferedImage watermark(File file, File waterFile, int x, int y, float alpha) throws IOException {
        // 获取底图
        BufferedImage buffImg = ImageIO.read(file);
        // 获取层图
        BufferedImage waterImg = ImageIO.read(waterFile);
        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = buffImg.createGraphics();
        int waterImgWidth = 60;// 获取层图的宽度
        int waterImgHeight = 60;// 获取层图的高度
        // 在图形和图像中实现混合和透明效果
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        // 绘制
        g2d.drawImage(waterImg, x, y, waterImgWidth, waterImgHeight, null);
        g2d.dispose();// 释放图形上下文使用的系统资源
        return buffImg;
    }
    

    /**
     * 输出水印图片
     * 
     * @param buffImg
     *            图像加水印之后的BufferedImage对象
     * @param savePath
     *            图像加水印之后的保存路径
     */
    public static void generateWaterFile(BufferedImage buffImg, String savePath) {
        int temp = savePath.lastIndexOf(".") + 1;
        try {
            ImageIO.write(buffImg, savePath.substring(temp), new File(savePath));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
    /**
    * 根据设置的宽高等比例压缩图片文件<br>
    * 先保存原文件，再压缩、上传
    * 
    * @param oldFile
    * 要进行压缩的文件
    * @param newFile
    * 新文件
    * @param width
    * 宽度 //设置宽度时（高度传入0，等比例缩放）
    * @param height
    * 高度 //设置高度时（宽度传入0，等比例缩放）
    * @param quality
    * 质量
    * @return 返回压缩后的文件的全路径
    */
    public static String zipImageFile(File oldFile, File newFile, int width, int height, float quality) {
    if (oldFile == null) {
    return null;
    }
    try {
    /** 对服务器上的临时文件进行处理 */
    Image srcFile = ImageIO.read(oldFile);
    int w = srcFile.getWidth(null);
    int h = srcFile.getHeight(null);
    double bili;
    if (width > 0) {
    bili = width / (double) w;
    height = (int) (h * bili);
    } else {
    if (height > 0) {
    bili = height / (double) h;
    width = (int) (w * bili);
    }
    }

    String srcImgPath = newFile.getAbsoluteFile().toString();
    // System.out.println(srcImgPath);
    String subfix = "jpg";
    subfix = srcImgPath.substring(srcImgPath.lastIndexOf(".") + 1, srcImgPath.length());

    BufferedImage buffImg = null;
    if (subfix.equals("png")) {
    buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    } else {
    buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    Graphics2D graphics = buffImg.createGraphics();
    graphics.setBackground(new Color(255, 255, 255));
    graphics.setColor(new Color(255, 255, 255));
    graphics.fillRect(0, 0, width, height);
    graphics.drawImage(srcFile.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

    ImageIO.write(buffImg, subfix, new File(srcImgPath));

    } catch (Exception e) {
    e.printStackTrace();
    }
    return newFile.getAbsolutePath();
    }

    /**
    * 按设置的宽度高度压缩图片文件<br>
    * 先保存原文件，再压缩、上传
    * 
    * @param oldFile
    * 要进行压缩的文件全路径
    * @param newFile
    * 新文件
    * @param width
    * 宽度
    * @param height
    * 高度
    * @param quality
    * 质量
    * @return 返回压缩后的文件的全路径
    */
    public static BufferedImage zipWidthHeightImageFile(MultipartFile oldFile, int width, int height, float quality) {
	    if (oldFile == null) {
	    	return null;
	    }
	    Image srcFile = null;
	    BufferedImage buffImg = null;
	    try {
		    /** 对服务器上的临时文件进行处理 */
		    srcFile = ImageIO.read(oldFile.getInputStream());
		    String imgName = oldFile.getOriginalFilename();
		    String subfix = "jpg";
		    subfix = imgName.substring(imgName.lastIndexOf(".") + 1, imgName.length());
		    if (subfix.equals("png")) {
		    	buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		    } else {
		    	buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		    }
		
		    Graphics2D graphics = buffImg.createGraphics();
		    graphics.setBackground(new Color(255, 255, 255));
		    graphics.setColor(new Color(255, 255, 255));
		    graphics.fillRect(0, 0, width, height);
		    graphics.drawImage(srcFile.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
		
		    //ImageIO.write(buffImg, subfix, new File(imgName));
		    return buffImg;
	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return null;
	    } finally {
		    if (srcFile != null) {
		    	srcFile.flush();
		    }
		    if (buffImg != null) {
		    	buffImg.flush();
		    }
	
	    }

    }

    /**
     * 
     * @param args
     * @throws IOException
     *             IO异常直接抛出了
     * @author bls
     */
    public static void main(String[] args) throws IOException {
    	//zipWidthHeightImageFile(new File("D:\\3.png"), new File("D:\\3.png"), 375, 180, 0.8f);
        String sourceFilePath = "D://test//2.png";
        String waterFilePath = "D://test//4.jpg";
        String saveFilePath = "D://test//new.png";
        // 构建叠加层
        BufferedImage buffImg = QuickMarkMergeUtil.watermark(new File(sourceFilePath), new File(waterFilePath), 265, 75, 1.0f);
        // 输出水印图片
        QuickMarkMergeUtil.generateWaterFile(buffImg, saveFilePath);
    }
}