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
package com.baidu.duer.dcs.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 提供统一的Preference util方法
 * <p>
 * Created by zhangyan42@baidu.com on 2017/5/24.
 */
public class PreferenceUtil {
    private static final String APP_SHARD = "com.baidu.duer.dcs";
    private static Map<String,Object> objectMap=new HashMap<>();

    /**
     * 保存数据的方法，拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key     key
     * @param object  value
     */
    public static void put(String key, Object object) {
        put(APP_SHARD, key, object);
    }

    public static void put(String spName, String key, Object object) {
        objectMap.put(key,object);
        //editSubmit(editor);
    }

//    private static void editSubmit(Editor editor) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//            editor.apply();
//        } else {
//            editor.commit();
//        }
//    }

    /**
     * 得到保存数据的方法，
     * 根据默认值得到保存的数据的具体类型，
     * 然后调用相对于的方法获取值
     *
     * @param key           key
     * @param defaultObject default-value
     */
    public static Object get(String key, Object defaultObject) {
        return get(APP_SHARD, key, defaultObject);
    }

    public static Object get(String spName, String key, Object defaultObject) {
//        if (defaultObject instanceof String) {
//            return sp.getString(key, (String) defaultObject);
//        } else if (defaultObject instanceof Integer) {
//            return sp.getInt(key, (Integer) defaultObject);
//        } else if (defaultObject instanceof Boolean) {
//            return sp.getBoolean(key, (Boolean) defaultObject);
//        } else if (defaultObject instanceof Float) {
//            return sp.getFloat(key, (Float) defaultObject);
//        } else if (defaultObject instanceof Long) {
//            return sp.getLong(key, (Long) defaultObject);
//        }

        Object obj=objectMap.get(key);
        if(obj==null){
            obj=defaultObject;
        }
        return obj;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key     key
     */
    public static void remove(String key) {
        remove(APP_SHARD, key);
    }

    public static void remove( String spName, String key) {
        objectMap.remove(key);
    }

    /**
     * 清除所有数据
     *
     */
    public static void clear() {
        clear( APP_SHARD);
    }

    public static void clear(String spName) {
        objectMap.clear();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key     key
     */
    public static boolean contains(String key) {
        return contains(APP_SHARD, key);
    }

    public static boolean contains(String spName, String key) {
        return objectMap.containsKey(key);
    }

    /**
     * 返回所有的键值对
     *
     */
    public static Map<String, ?> getAll() {
        return getAll( APP_SHARD);
    }

    public static Map<String, ?> getAll(String spName) {
        return objectMap;
    }
}
