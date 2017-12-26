package com.gms.util;


import org.apache.commons.lang3.StringUtils;


public class PhoneUtil {

    /**
     * 中国移动电话号码正则表达式
     */
    public static String cmccNumReg = "(134|135|136|137|138|139|150|151|152|157|158|159|182|183|184|187|188|147|178)[0-9]{8}";

    /**
     * 中国联通电话号码正则表达式
     */
    public static String cuccNumReg = "(130|131|132|155|156|185|186|145|175|176)[0-9]{8}";

    /**
     * 中国电信电话号码正则表达式
     */
    public static String ctccNumReg = "(133|153|180|181|189|177)[0-9]{8}";

    /**
     * 手机号正则表达
     */
    private static String phoneReg = "^1[3|4|5|7|8]\\d{9}$";


    /**
     * 验证号码是否符合三大运营商
     * 
     * @param phoneNum
     * @return
     */
    public static boolean isValide(String phoneNum) {
        if (StringUtils.isBlank(phoneNum))
            return false;

        if (phoneNum.matches(cmccNumReg) || phoneNum.matches(cuccNumReg) || phoneNum.matches(ctccNumReg))
            return true;

        return false;
    }

    /**
     * 验证号码是否为手机号
     * @param phoneNum
     * @return boolean
     */
    public static boolean isPhoneNum(String phoneNum) {
        if (StringUtils.isBlank(phoneNum)) {
            return false;
        }
        if (phoneNum.matches(phoneReg)) {
            return true;
        }
        return false;
    }
}
