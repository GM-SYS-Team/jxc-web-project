package com.gms.service.jxc;

import com.gms.util.Constants;

import java.util.Date;
import java.util.Map;

/**
 * @author wuchuantong
 * @version V1.0
 * @Title: todo
 * @Package com.gms.service.jxc
 * @Description: todo
 * @date 2017/12/28 23:09
 */
public interface PushService {

    String broadcastAll(String title, String content, Map<String, String> payload, Constants.PUSH_PLATFORM platform, Date sendTime) throws Exception;
}
