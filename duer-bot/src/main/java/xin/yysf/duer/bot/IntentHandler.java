package xin.yysf.duer.bot;


import xin.yysf.duer.bot.entity.proto.DuerRequest;
import xin.yysf.duer.bot.entity.proto.DuerResponse;

/**
 * 意图处理
 */
public interface IntentHandler {
    /**
     * 自定义处理意图
     * @param request dueros 请求
     * @param msgBuilder  消息构建辅助类
     * @param intent 当前意图
     * @return 是否完成本地会话。  如果意图完全符合要求，应该返回true
     */
    boolean handleIntent(DuerRequest request, DuerResponse.Builder msgBuilder, DuerRequest.Intent intent);
}
