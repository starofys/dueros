package xin.yysf.duer.bot;

import xin.yysf.duer.bot.entity.proto.DuerRequest;
import xin.yysf.duer.bot.entity.proto.DuerResponse;

/**
 * @author ysf
 * @date 2017-12-08 16:25
 **/
public class DuerContext {
    /**
     * dueros 请求
     */
    public final DuerRequest duerRequest;
    /**
     * 消息构建辅助类
     */
    public final DuerResponse.Builder msgBuilder;
    /**
     * 当前的会话
     */
    public final DuerSession session;

    /**
     * 当前用户id
     */
    public final Long userId;

    public DuerContext(DuerRequest duerRequest, DuerResponse.Builder msgBuilder, DuerSession session) {
        this.duerRequest = duerRequest;
        this.msgBuilder = msgBuilder;
        this.session = session;
        userId=duerRequest.context.path("System").path("user").path("userId").asLong();
    }
}
