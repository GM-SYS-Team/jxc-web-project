package com.gms.serverutil;

import com.syscan.sdk.common.util.CKMEncodeUtil;

public class EncodeUtil {
	/**
	 * @author zhoutianqi
	 * @className EncodeUtil.java
	 * @date 2017年12月12日 下午1:39:06
	 * @description 
	 */
	public static byte[] encodeShop(int rows,int cols,int modelSize,int qzsize,String encodeContent,String picType){
		CKMEncodeUtil util = new CKMEncodeUtil();
		util.set_encode_rows(rows); //5至99之间的奇数
		util.set_encode_cols(cols); //5至99之间的奇数
		util.set_module_size(modelSize);  //单元模块大小5(像素)
		util.set_qz_size(qzsize);	  //空白区16像素
		util.set_image_format(picType);//图像格式，注意必须带.号
		util.set_dark_color(0x2A12E2);	//设置深色模块颜色(前景色), 0xBBGGRR
		//util.set_light_color(0x00FF00); //设置浅色模块颜色(背景色), 0xBBGGRR
		byte[] image = util.encode(encodeContent);//编码生成图像
		return image;
	} 
}
