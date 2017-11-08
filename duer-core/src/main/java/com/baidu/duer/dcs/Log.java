package com.baidu.duer.dcs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
    private static Logger logger= LoggerFactory.getLogger(Log.class);
    public static void d(String tag,String msg){
        d(tag,msg,null);
    }
    public static void d(String tag,String msg,Exception e){
        logger.debug("["+tag+"]"+msg);
        if(e!=null) {
            e.printStackTrace();
        }
    }
    public static void d(String tag,String msg,Throwable e){
        logger.debug("["+tag+"]"+msg,e);
    }

    public static void e(String tag, String msg) {
        logger.error("["+tag+"]"+msg);
    }

    public static void v(String tag, String msg) {
        logger.info("["+tag+"]"+msg);
    }

    public static void i(String tag, String msg) {
        logger.info("["+tag+"]"+msg);
    }

    public static void w(String tag, String msg) {
        logger.warn("["+tag+"]"+msg);
    }

    public static void w(String tag, String msg, Throwable e) {
        logger.warn("["+tag+"]"+msg,e);
    }

    public static void e(String tag, String msg, Throwable e) {
        logger.error("["+tag+"]"+msg,e);
    }

    public static char[] getStackTraceString(Throwable tr) {
        return tr.getMessage().toCharArray();
    }
}
