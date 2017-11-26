package xin.yysf.duer.bot.entity.proto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public class DuerProto {
    public static class Session{
        @JsonProperty("new")
        public boolean bNew;
        public String sessionId;
        public Map<String,String> attributes;
    }

    /**
     * 版本
     */
    public String version;
    /**
     * 本次交互会话id
     */
    public Session session;
    /**
     * 上下文
     */
    public JsonNode context;
}
