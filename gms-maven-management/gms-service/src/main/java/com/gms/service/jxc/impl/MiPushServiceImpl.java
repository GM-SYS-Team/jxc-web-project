package com.gms.service.jxc.impl;

import com.alibaba.fastjson.JSON;
import com.gms.service.jxc.PushService;
import com.gms.util.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author wuchuantong
 * @version V1.0
 * @Title: todo
 * @Package com.gms.service.jxc.impl
 * @Description: todo
 * @date 2017/12/28 23:07
 */
@Service("miPushService")
public class MiPushServiceImpl implements PushService {

    @Value("${mipush.appSecretKey.android}")
    private String appSecretKeyAndroid;

    @Value("${mipush.appSecretKey.iphone}")
    private String appSecretKeyIphone;

    @Value("${mipush.appSecretKey.ipad}")
    private String appSecretKeyIpad;

    @Value("${mipush.packageName.android}")
    private String packageNameAndroid;

    @Value("${mipush.packageName.iphone}")
    private String bundleIdIphone;

    @Value("${mipush.packageName.ipad}")
    private String bundleIdIpad;

    @Value("${mipush.production}")
    private Boolean production;

    @Value("${mipush.defaultRetryNum}")
    private Integer defaultRetryNum;

    //流速限制,控制消息是否需要进行平缓发送（如果开启平滑推送，则qps 默认为3000/s）。默认不支持。
    // 也可以直接通过extra字段自定义设置平滑推送的速度（如果自定义了平滑推送的extra字段，那么不再需要调用enableFlowControl()方法）。
    // key是flow_control，value代表平滑推送的速度。
    // 注：服务端支持最低1000/s的qps，最高100000/s。也就是说，如果将平滑推送设置为500，那么真实的推送速度是3000/s，如果大于1000小于100000，则以设置的qps为准。
    private static final String FLOW_CONTROL_NUM = "1000";

    private static Logger logger = LoggerFactory.getLogger(MiPushServiceImpl.class);


    /**
     * 推送给全部设备
     *
     * @param title    显示标题
     * @param content  显示内容
     * @param payload  负载内容
     * @param platform 设备平台
     * @throws Exception
     */
    public String broadcastAll(String title, String content, Map<String, String> payload, Constants.PUSH_PLATFORM platform, Date sendTime) throws Exception {

        Message message = buildMessage(title, content, payload, platform, sendTime);
        setPushMode(platform);

        Sender sender = buildSender(platform);
        // 推送给全部设备
        Result result = sender.broadcastAll(message, defaultRetryNum);

        logger.info("broadcast to all devices...params:{title:" + title + ",content:" + content + ",platform:" + platform.name() + ",sendTime:" + date2string(sendTime) + "}");
        logger.info("broadcast to all devices...result:" + JSON.toJSONString(result));
        return result.getMessageId();
    }


    private static String date2string(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 构建推送消息
     *
     * @param title    显示标题
     * @param content  显示内容
     * @param payload  负载内容MAP
     * @param platform 设备平台
     * @param sendTime 发送时间
     * @return 小米消息对象
     */
    private Message buildMessage(String title, String content, Map<String, String> payload, Constants.PUSH_PLATFORM platform, Date sendTime) {
        switch (platform) {
            case ANDROID:
                return buildAndroidMessage(title, content, payload, packageNameAndroid, sendTime);
            case IPHONE:
                return buildIosMessage(title, content, payload, bundleIdIphone, sendTime);
            case IPAD:
                return buildIosMessage(title, content, payload, bundleIdIpad, sendTime);
            default:
                throw new RuntimeException("push platform must be specified!");
        }
    }


    private static Message buildAndroidMessage(String title, String content, Map<String, String> payload, String packageName, Date sendTime) {
        Message.Builder builder = new Message.Builder().title(title) // 通知栏展示的通知的标题
                .description(content) // 通知栏展示的通知的描述
                .payload(JSON.toJSONString(payload)) // 消息的内容
                .restrictedPackageName(packageName) // app的包名
                .notifyType(1); // 使用默认提示音提示
        if (sendTime != null) {
            builder.timeToSend(sendTime.getTime()); // 定时发送时间
        }
        builder.extra("flow_control", FLOW_CONTROL_NUM);
        // buildJobKeyExtra(title, content, builder);
        return builder.build();
    }

    private static Message buildIosMessage(String title, String content, Map<String, String> payload, String packageName, Date sendTime) {
        Message.IOSBuilder iOSBuilder = new Message.IOSBuilder()
              //  .title(title)
                .description(content) // 通知栏展示的通知的描述
                .soundURL("default") // 消息铃声
                .badge(1) // 数字角标
                .mutableContent("1");
        for (String key : payload.keySet()) {
            iOSBuilder.extra(key, payload.get(key));
        }
        if (sendTime != null) {
            iOSBuilder.timeToSend(sendTime.getTime()); // 定时发送时间
        }
//        buildIOSJobKeyExtra(title, content, iOSBuilder);
        return iOSBuilder.build();
    }


    /**
     * 创建不同平台的推送者
     *
     * @param platform 推送平台
     * @return
     */
    private Sender buildSender(Constants.PUSH_PLATFORM platform) {

        String appKey = getAppSecretKey(platform);
        return new Sender(appKey);
    }

    /**
     * 获取APP密钥
     *
     * @param platform
     * @return
     */
    private String getAppSecretKey(Constants.PUSH_PLATFORM platform) {
        String appKey;
        switch (platform) {
            case ANDROID:
                appKey = appSecretKeyAndroid;
                break;
            case IPHONE:
                appKey = appSecretKeyIphone;
                break;
            case IPAD:
                appKey = appSecretKeyIpad;
                break;
            default:
                throw new RuntimeException("push platform must be specified!");
        }
        return appKey;
    }

    /**
     * 设置推送模式
     *
     * @param platform 设备平台
     */
    private void setPushMode(Constants.PUSH_PLATFORM platform) {
        if (Constants.PUSH_PLATFORM.ANDROID.equals(platform)) {
            com.xiaomi.xmpush.server.Constants.useOfficial(); // 安卓使用正式模式
        } else {
            if (production) {
                com.xiaomi.xmpush.server.Constants.useOfficial(); // 正式
            } else {
                com.xiaomi.xmpush.server.Constants.useSandbox(); // 沙盒(for IOS)
            }
        }
    }

}
