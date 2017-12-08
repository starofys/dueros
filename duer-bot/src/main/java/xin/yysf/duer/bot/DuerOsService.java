package xin.yysf.duer.bot;


import xin.yysf.duer.bot.entity.cus.IntentDesc;
import xin.yysf.duer.bot.entity.proto.DuerRequest;
import xin.yysf.duer.bot.entity.proto.DuerResponse;

public interface DuerOsService {
    /**
     * 处理duer os 请求
     * @param request
     * @return
     */
    DuerResponse processRequest(DuerRequest request);

    /**
     * 添加意图处理类
     * @param intentDesc
     * @param handler  实现类可以自动实现依赖注入。
     */
    void addIntentHandler(IntentDesc intentDesc,IntentHandler handler);
}
