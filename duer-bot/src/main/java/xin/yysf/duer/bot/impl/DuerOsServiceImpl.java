package xin.yysf.duer.bot.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xin.yysf.duer.bot.entity.cus.IntentDesc;
import xin.yysf.duer.bot.entity.cus.SlotDesc;
import xin.yysf.duer.bot.entity.proto.DuerRequest;
import xin.yysf.duer.bot.entity.proto.DuerResponse;
import xin.yysf.duer.bot.entity.proto.Slot;
import xin.yysf.duer.bot.DuerOsService;
import xin.yysf.duer.bot.IntentHandler;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuerOsServiceImpl implements DuerOsService{
    private static Logger log= LoggerFactory.getLogger(DuerOsServiceImpl.class);





    /**
     * 意图的定义
     */
    private Map<String,IntentDesc> intents=new HashMap<>();


    /**
     * 意图的处理类
     */
    private Map<String,IntentHandler> intentHandlerMap=new HashMap<>();






    @Autowired
    private ApplicationContext applicationContext;
    @Override
    public DuerResponse processRequest(DuerRequest request) {

        DuerResponse.Builder msgBuilder = DuerResponse.Builder.buildFromRequest(request);

        if(request==null||request.request==null){
            //response.setStatus(400);
            return msgBuilder.textMessage("我靠，数据好像有问题了").build();
        }

        log.debug("[dueros] context:{}",request.context);



        DuerRequest.RequestType duerReq = request.request;
        log.debug("[dueros] request type:{},requestId:{},timestamp：{}",duerReq.type,duerReq.requestId,duerReq.timestamp);
        if(duerReq.getClass()==DuerRequest.RequestType.class){
            msgBuilder.newSession(true);
            log.info("进入设备控制");
            msgBuilder.textMessage("我要做点什么");
        }else if(duerReq.getClass()==DuerRequest.IntentRequest.class){

            /**
             * 当前的用户id ,当前的设备id也是可以拿到的
             */
            long userId = request.context.path("System").path("user").path("userId").asLong();

            DuerRequest.IntentRequest intentRequest=(DuerRequest.IntentRequest)duerReq;

            for (DuerRequest.Intent intent : intentRequest.intents) {

                for (Map.Entry<String, Slot> stringSlotEntry : intent.slots.entrySet()) {
                    Slot slot=stringSlotEntry.getValue();
                    log.debug("\t\t\t{}->{name:{},value:{}},values:{},confirmationStatus{}"
                            ,stringSlotEntry.getKey(),slot.name,slot.value,slot.values,slot.confirmationStatus);
                }
                IntentDesc desc = intents.get(intent.name);
                if (desc != null) {
                    log.debug("[dueros] 开始处理意图:{}", intent.name);
                    List<SlotDesc> slotDescs = desc.getSlots();
                    if (slotDescs != null) {
                        boolean bComp = true;
                        for (SlotDesc slotDesc : slotDescs) {



                            DuerResponse.DirectiveBuilder directiveBuilder = DuerResponse.DirectiveBuilder.newBuilder()
                                    .updatedIntent(intent);


                            if (slotDesc.isRequired()) {
                                Slot slot = intent.slots.get(slotDesc.getName());
                                if (slot == null) {

                                    //向用户询问未知的槽值
//                                    directiveBuilder.toElicitSlot(slotDesc.getName());
//                                    msgBuilder.textMessage(slotDesc.getMsg());

                                    //或者直接交给百度自己处理
                                    directiveBuilder.toDelegate();
                                    msgBuilder.addDirective(directiveBuilder.build());
                                    bComp = false;
                                    break;
                                }
                            }
                        }
                        IntentHandler handler = intentHandlerMap.get(intent.name);
                        if(handler!=null){
                            if(bComp){
                                msgBuilder.shouldEndSession(handler.handleIntent(request, msgBuilder, intent));
                            }
                        }else{
                            msgBuilder.shouldEndSession(true).textMessage("我靠，还没有实现该意图！");
                        }

                    }

                } else {
                    log.warn("[dueros] 意图：{},没有找到合适大的意图处理类", intent.name);
                    msgBuilder.textMessage("我还不会").shouldEndSession(true);
                }
                if (intentRequest.query != null)
                    log.info("请求内容: {}", intentRequest.query.original);
                if (msgBuilder.build().response.outputSpeech != null) {
                    log.info("返回内容: {}", msgBuilder.build().response.outputSpeech.text);
                }
            }

        }else if(duerReq.getClass()==DuerRequest.SessionEndedRequest.class){
            DuerRequest.SessionEndedRequest sessionEndedRequest= (DuerRequest.SessionEndedRequest) duerReq;
            DuerRequest.ErrorInfo error = sessionEndedRequest.error;
            if(error!=null){
                log.debug("[dueros] 会话结束,状态：{},error:{},message:{}",sessionEndedRequest.reason,error.type,error.message);
            }else{
                log.debug("[dueros] 会话结束,状态：{},error:{}",sessionEndedRequest.reason,"未知错误");
            }
            msgBuilder.textMessage("不清楚要做什么").shouldEndSession(true);
        }
        return msgBuilder.build();
    }

    @Override
    public void addIntentHandler(IntentDesc intentDesc, IntentHandler handler) {

        if(StringUtils.isEmpty(intentDesc.getName())){
            log.warn("意图名为空：{}",intentDesc);
            return;
        }

        intents.put(intentDesc.getName(),intentDesc);
        intentHandlerMap.put(intentDesc.getName(),handler);

    }
}
