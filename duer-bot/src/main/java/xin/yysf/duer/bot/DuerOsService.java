package xin.yysf.duer.bot;


import xin.yysf.duer.bot.entity.proto.DuerRequest;
import xin.yysf.duer.bot.entity.proto.DuerResponse;

public interface DuerOsService {
    /**
     * 处理duer os 请求
     * @param request
     * @return
     */
    DuerResponse processRequest(DuerRequest request);
}
