package xin.yysf.duer.bot.impl;

import xin.yysf.duer.bot.DuerContext;
import xin.yysf.duer.bot.DuerSession;
import xin.yysf.duer.bot.entity.proto.DuerRequest;
import xin.yysf.duer.bot.entity.proto.DuerResponse;
import xin.yysf.duer.bot.entity.proto.Slot;
import xin.yysf.duer.bot.IntentHandler;

/**
 * Demo 开关控制的实现
 */
public class SwitchControlIntentHandler implements IntentHandler {


    @Override
    public boolean handleIntent(DuerContext duerContext, DuerRequest.Intent intent) {
//        DuerResponse.DirectiveBuilder directiveBuilder= DuerResponse.DirectiveBuilder.newBuilder()
//                .updatedIntent(intent);


        Slot deviceName=intent.slots.get("device_name");
        Slot s=intent.slots.get("switch_control");
        Slot homeLoc=intent.slots.get("home_loc");

        String homeLocStr;
        if(homeLoc!=null) {
            homeLocStr = homeLoc.value;
            duerContext.session.setAttribute("home_loc", homeLocStr);
        }else{
            /**
             * 如果位置没有找到，再次询问位置
             */
            if((homeLocStr=(String)duerContext.session.getAttribute("home_loc"))==null){
                DuerResponse.DirectiveBuilder directiveBuilder= DuerResponse.DirectiveBuilder.newBuilder()
                        .updatedIntent(intent);
                directiveBuilder.toElicitSlot("home_loc");
                duerContext.msgBuilder.addDirective(directiveBuilder.build());
                duerContext.msgBuilder.textMessage("哪里的？");
                return false;
            }
        }


        System.out.println("userId="+duerContext.userId);

        duerContext.msgBuilder.textMessage("意图识别完成：设备名："+deviceName.value+",位置:"+homeLocStr+",动作："+s.value);


        //根据userId调用mqtt等相关制定完成后续的开发工作


        return true;
    }
}
