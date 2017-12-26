package com.gms.util;

/**
 * @author wuchuantong
 * @version V1.0
 * @Title: todo
 * @Package com.gms.util
 * @Description: todo
 * @date 2017/11/17 23:36
 */
public class Constants {

    /**
     * 推送的设备平台
     */
    public static enum PUSH_PLATFORM {
        IPHONE("iPhone平台"),
        IPAD("ipad平台"),
        ANDROID("安卓手机平台"),
        ALL("全部平台");

        private String cnName;

        PUSH_PLATFORM(String name) {
            this.cnName = name;
        }

        public String getCode() {
            return this.name();
        }

        public String getCnName() {
            return this.cnName;
        }

        public static String getCnName(String code) {
            for (PUSH_PLATFORM item : PUSH_PLATFORM.values()) {
                if (item.getCode().equals(code)) {
                    return item.getCnName();
                }
            }
            return code;
        }

        public String toString() {
            return "code:" + this.name() + ",cnName:" + this.cnName;
        }
    }
}
