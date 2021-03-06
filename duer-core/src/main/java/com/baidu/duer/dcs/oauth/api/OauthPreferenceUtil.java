/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baidu.duer.dcs.oauth.api;


import com.baidu.duer.dcs.util.PreferenceUtil;

/**
 * 用户认证保存Preference工具类
 * <p>
 * Created by zhangyan42@baidu.com on 2017/6/3.
 */
public class OauthPreferenceUtil extends PreferenceUtil {
    public static final String BAIDU_OAUTH_CONFIG = "baidu_oauth_config";

    /**
     * 保存数据的方法，拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context 上下文
     * @param key     key
     * @param object  value
     */
    public static void put(String key, Object object) {
        put( BAIDU_OAUTH_CONFIG, key, object);
    }

    /**
     * 得到保存数据的方法，
     * 根据默认值得到保存的数据的具体类型，
     * 然后调用相对于的方法获取值
     *
     * @param context       上下文
     * @param key           key
     * @param defaultObject default-value
     */
    public static Object get(String key, Object defaultObject) {
        return get(BAIDU_OAUTH_CONFIG, key, defaultObject);
    }
    
    public static void clear() {
        clear(BAIDU_OAUTH_CONFIG);
    }

    public static void setAccessToken(String value) {
        put(OauthConfig.PrefenenceKey.SP_ACCESS_TOKEN, value);
    }

    public static String getAccessToken() {
        return (String) get(OauthConfig.PrefenenceKey.SP_ACCESS_TOKEN, "");
    }

    public static void setExpires(long value) {
        put(OauthConfig.PrefenenceKey.SP_EXPIRE_SECONDS, value);
    }

    public static long getExpires() {
        return (long) get( OauthConfig.PrefenenceKey.SP_EXPIRE_SECONDS, 0L);
    }

    public static void setCreateTime( long value) {
        put( OauthConfig.PrefenenceKey.SP_CREATE_TIME, value);
    }

    public static long getCreateTime() {
        return (long) get(OauthConfig.PrefenenceKey.SP_CREATE_TIME, 0L);
    }

    public static void clearAllOauth() {
        clear();
    }
}