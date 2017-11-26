package xin.yysf.duer.bot.impl;

import xin.yysf.duer.bot.entity.proto.DuerRequest;
import xin.yysf.duer.bot.entity.proto.DuerResponse;
import xin.yysf.duer.bot.entity.proto.Slot;
import xin.yysf.duer.bot.IntentHandler;

/**
 * Demo 开关控制的实现
 */
public class SwitchControlIntentHandler implements IntentHandler {


    @Override
    public boolean handleIntent(DuerRequest request, DuerResponse.Builder msgBuilder, DuerRequest.Intent intent) {
//        DuerResponse.DirectiveBuilder directiveBuilder= DuerResponse.DirectiveBuilder.newBuilder()
//                .updatedIntent(intent);

        long userId = request.context.path("System").path("user").path("userId").asLong();

        Slot deviceName=intent.slots.get("device_name");
        Slot s=intent.slots.get("switch_control");
        Slot homeLoc=intent.slots.get("home_loc");
        System.out.println("userId="+userId);

        msgBuilder.textMessage("意图识别完成：设备名："+deviceName.value+",位置:"+homeLoc.value+",动作："+s.value);



        //根据userId调用mqtt等相关制定完成后续的开发工作


        return true;
    }
}
