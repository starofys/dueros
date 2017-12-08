package xin.yysf.duer.bot;

public interface DuerSession{
    long getCreationTime() ;
    String getId();
    Object getAttribute(String key);
    void setAttribute(String key, Object o);
    void removeAttribute(String key);

    /**
     * 刷新时间
     */
    void refreshTime();
}
