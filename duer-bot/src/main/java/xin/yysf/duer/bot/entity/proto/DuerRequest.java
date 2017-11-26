package xin.yysf.duer.bot.entity.proto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Map;

/**
 * <a href="https://developer.dueros.baidu.com/didp/doc/dueros-bot-platform/dbp-custom/standard-request.md">标准请求</a>
 */
public class DuerRequest extends DuerProto{


    /**
     * 请求消息
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = RequestType.class, name = "LaunchRequest"),
            @JsonSubTypes.Type(value = IntentRequest.class, name = "IntentRequest"),
            @JsonSubTypes.Type(value = SessionEndedRequest.class, name = "SessionEndedRequest") })
    public static class RequestType{
        /**
         * 请求类型
         */
        public String type;
        /**
         * 标识本次请求的唯一ID。如果有问题反馈，可以附带提供给DuerOS开发人员。
         */
        public String requestId;
        /**
         * 标识本次请求的唯一ID。如果有问题反馈，可以附带提供给DuerOS开发人员。
         */
        public String timestamp;
    }


    public static class Intent{
        public String name;
        public Map<String,Slot> slots;
        public String confirmationStatus;
    }

    public enum DialogState{
        //开始，进行中，本轮对话的意图已经收集和确认了所有必要槽位，同时如果意图有需求也已经一并确认了。
        STARTED,IN_PROGRESS,COMPLETED
    }

    /**
     * 以下两种情况，请求会被DuerOS转换为IntentRequest。

     技能还没被调起，用户语音请求“打开技能，帮我查询天气” 此请求会被解释为查询天气的IntentRequest，session.new的值为true。
     技能已经被调起，用户与技能在多轮对话中，用户请求 “帮我查询天气” 此请求会被解释为查询天气的IntentRequest，session.new的值为false。
     */
    public static class IntentRequest extends RequestType{
        /**
         * 当前会话状态，取值如下：
         STARTED
         开始。
         IN_PROGRESS
         进行中。
         COMPLETED
         本轮对话的意图已经收集和确认了所有必要槽位，同时如果意图有需求也已经一并确认了。
         */
        public DialogState dialogState;
        /**
         * 本次请求信息。
         */
        public Query query;
        /**
         * 本次语音请求被DuerOS解析后生成的意图。DuerOS可能解析出一个或多个意图，这些意图按照概率可能性由大到小排序。
         */
        public List<Intent> intents;
    }
    public enum Reason{
//        USER_INITIATED：用户直接说“退出”。
//        EXCEEDED_MAX_REPROMPTS：用户无输入或多次输入无法理解。
//        ERROR：系统错误，详细信息见error字段。
        USER_INITIATED,EXCEEDED_MAX_REPROMPTS,ERROR
    }
    public enum ErrorType{
        INVALID_RESPONSE,DEVICE_COMMUNICATION_ERROR,INTERNAL_ERROR
    }
    public static class ErrorInfo{
        public ErrorType type;
        public String message;
    }
    public static class SessionEndedRequest extends RequestType{
        public Reason reason;
        public ErrorInfo error;
    }
    public static class Query{
        /**
         * 请求类型，取值如下：
         TEXT
         本次语音请求经过DuerOS理解后是一个文本请求。
         */
        public String type;
        /**
         * 本次语音请求经过DuerOS理解后生成的文本。
         */
        public String original;
    }
    public RequestType request;


}
