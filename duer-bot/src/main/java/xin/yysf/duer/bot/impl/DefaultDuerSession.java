package xin.yysf.duer.bot.impl;

import xin.yysf.duer.bot.DuerSession;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class DefaultDuerSession implements DuerSession {
    private final String id;
    private Map<String,Object> sessionMap=new HashMap<>();
    private Long creationTime;
    public DefaultDuerSession(String id){
        this.id=id;
        creationTime=System.currentTimeMillis();
    }
    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Object getAttribute(String key) {
        return sessionMap.get(key);
    }

    @Override
    public void setAttribute(String key, Object o) {
        sessionMap.put(key,o);
    }

    @Override
    public void removeAttribute(String key) {
        sessionMap.remove(key);
    }

    @Override
    public void refreshTime() {
        creationTime=System.currentTimeMillis();
    }


}
