package com.baidu.duer.dcs;

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

import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.Closeable;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * 辅助工具类
 * <p>
 * Created by guxiuzhong@baidu.com on 2017/5/17.
 */
public class CommonUtil {
    private static final int JSON_INDENT = 4;
    public static String deviceId;

    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINESE);
        return format.format(new Date());
    }

    /**
     * 将毫秒格式转化为yyyy-MM-dd HH:mm:ss
     *
     * @param milliSeconds 毫秒
     * @return 格式化后的字符串结果
     */
    public static String formatToDataTime(long milliSeconds) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sd.format(milliSeconds);
    }

    /**
     * 将key1=value1&key2=value2格式的query转换成key-value形式的参数串
     *
     * @param query key1=value1&key2=value2格式的query
     * @return key-value形式的bundle
     */
//    public static Bundle decodeUrl(String query) {
//        Bundle ret = new Bundle();
//        if (query != null) {
//            String[] pairs = query.split("&");
//            for (String pair : pairs) {
//                String[] keyAndValues = pair.split("=");
//                if (keyAndValues != null && keyAndValues.length == 2) {
//                    String key = keyAndValues[0];
//                    String value = keyAndValues[1];
//                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
//                        ret.putString(URLDecoder.decode(key), URLDecoder.decode(value));
//                    }
//                }
//            }
//        }
//        return ret;
//    }

    /**
     * 将key-value形式的参数串，转换成key1=value1&key2=value2格式的query
     *
     * @param params key-value参数
     * @return key1=value1&key2=value2格式的query
     */
//    public static String encodeUrl(Bundle params) {
//        if (params == null || params.isEmpty()) {
//            return null;
//        }
//        boolean first = true;
//        StringBuilder sb = new StringBuilder();
//        for (String key : params.keySet()) {
//            String paramValue = params.getString(key);
//            if (paramValue == null) {
//                continue;
//            }
//            if (first) {
//                first = false;
//            } else {
//                sb.append("&");
//            }
//            sb.append(URLEncoder.encode(key)).append("=").append(URLEncoder.encode(paramValue));
//        }
//        return sb.toString();
//    }

    /**
     * 展示一个通用的弹出框UI
     *
     * @param context 展示弹出框的上下文环境
     * @param title   警告的title信息
     * @param text    警告信息
     */
//    public static void showAlert(Context context, String title, String text) {
//        AlertDialog alertDialog = new Builder(context).create();
//        alertDialog.setTitle(title);
//        alertDialog.setMessage(text);
//        alertDialog.setCanceledOnTouchOutside(true);
//        alertDialog.show();
//    }

    private static long lastClickTime;

    /**
     * 是否是双击
     *
     * @return true 是，false 否
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * json 格式化输出
     *
     * @param json json字符串
     * @return 用四个空格缩进后的json字符串结果
     */
//    public static String formatJson(String json) {
//        String formatted = "";
//        if (json == null || json.length() == 0) {
//            return formatted;
//        }
//        try {
//            if (json.startsWith("{")) {
//                JSONObject jo = new JSONObject(json);
//                formatted = jo.toString(JSON_INDENT);
//            } else if (json.startsWith("[")) {
//                JSONArray ja = new JSONArray(json);
//                formatted = ja.toString(JSON_INDENT);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return formatted;
//    }

    /**
     * 关闭流
     *
     * @param closeables closeables
     */
    public static void closeQuietly(Closeable... closeables) {
        for (Closeable c : closeables) {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Android设备物理唯一标识符
     *
     * @return String 设备唯一标识
     */
    public static String getDeviceUniqueID() {
//        String devIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            devIDShort += (Build.SUPPORTED_ABIS[0].length() % 10);
//        } else {
//            devIDShort += (Build.CPU_ABI.length() % 10);
//        }
//        devIDShort += (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10)
//                + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
//        String serial;
//        try {
//            serial = Build.class.getField("SERIAL").get(null).toString();
//            return new UUID(devIDShort.hashCode(), serial.hashCode()).toString();
//        } catch (Exception e) {
//            serial = "Dueros000";
//        }
//        return new UUID(devIDShort.hashCode(), serial.hashCode()).toString();

        if(deviceId==null){
            String mac=findMAC();
            if(mac==null){
                System.err.println("获取getDeviceUniqueID 失败");
                mac="1111111";
            }
            deviceId=mac;
        }


        return deviceId;
    }
    public static String findMAC(){
        String[] cardNames={"eth0","eth1","wlan0","wlan1","wlan2"};
        for (String cardName : cardNames) {
            NetworkInterface card=findNetworkInterface(cardName);
            if(card!=null){
                String mac = readMac(card);
                if(mac!=null){
                    return mac;
                }
            }
        }


        try {
            Enumeration<NetworkInterface> cards = NetworkInterface.getNetworkInterfaces();
            while (cards.hasMoreElements()){
                String mac=readMac(cards.nextElement());
                if(mac!=null){
                    return mac;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }


        return null;
    }
    private static String readMac(NetworkInterface card){
        try {
            byte[] addr = card.getHardwareAddress();
            if(addr!=null){
                String mac=new BigInteger(addr).toString(16);
                if(mac.length()==11){
                    mac="0"+mac;
                }
                return mac;
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static NetworkInterface findNetworkInterface(String name){
        try {
            Enumeration<NetworkInterface> cards = NetworkInterface.getNetworkInterfaces();
            while (cards.hasMoreElements()){
                NetworkInterface card = cards.nextElement();
                if(name.equals(card.getName())){
                    return card;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}