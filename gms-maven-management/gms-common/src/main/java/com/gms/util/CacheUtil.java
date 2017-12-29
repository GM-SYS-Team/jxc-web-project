package com.gms.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class CacheUtil {

	private static Logger logger = LogManager.getLogger(CacheUtil.class);

	public static final String USER = "user";

	public static final String SMS_CODE = "smsCode";

	public static Map<String, Cache<String, Object>> cache = new HashMap<String, Cache<String, Object>>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put(USER, CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(30, TimeUnit.DAYS).build());
			put(SMS_CODE, CacheBuilder.newBuilder().maximumSize(10000).expireAfterWrite(30, TimeUnit.MINUTES)
					.removalListener(new RemovalListener<String, Object>() {
						@Override
						public void onRemoval(RemovalNotification<String, Object> rn) {
							logger.info(rn.getKey() + "被移除");
						}
					}).build());
		}
	};

	public static void put(String mapName, String key, Object value) {
		cache.get(mapName).put(key, value);
	}

	public static Object get(String mapName, String key) {
		Object value = cache.get(mapName).getIfPresent(key);
		if (SMS_CODE.equals(mapName) && value != null) {
			cache.get(mapName).invalidate(key);
		}
		return value;
	}
}