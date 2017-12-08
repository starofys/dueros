package xin.yysf.duer.bot.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import xin.yysf.duer.bot.DuerContext;
import xin.yysf.duer.bot.IntentHandler;
import xin.yysf.duer.bot.entity.proto.DuerRequest;
import xin.yysf.duer.bot.entity.proto.DuerResponse;
import xin.yysf.duer.bot.entity.proto.Slot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ysf
 * @date 2017-12-08 17:44
 **/
public class DefaultColorControlIntentHandler implements IntentHandler {
    @Autowired
    private ObjectMapper objectMapper;
    private Map<String,Integer> colorMap=new HashMap<>();
    public DefaultColorControlIntentHandler(){
        colorMap.put("红色",0xff0000);
        colorMap.put("绿色",0x00ff00);
        colorMap.put("蓝色",0x0000ff);
    }
    @Override
    public boolean handleIntent(DuerContext duerContext, DuerRequest.Intent intent) {
        Slot homeLoc=intent.slots.get("home_loc");
        String homeLocStr;

        DuerResponse.DirectiveBuilder directiveBuilder= DuerResponse.DirectiveBuilder.newBuilder()
                .updatedIntent(intent).toElicitSlot("home_loc");

        if(homeLoc!=null){
            homeLocStr=homeLoc.value;
            duerContext.session.setAttribute("home_loc",homeLocStr);
        }else if((homeLocStr=(String)duerContext.session.getAttribute("home_loc"))==null){
            /**
             * 如果位置没有找到，再次询问位置
             */
            duerContext.msgBuilder.addDirective(directiveBuilder.build());
            duerContext.msgBuilder.textMessage("哪里的？");
            return false;
        }
        try {
            String color = objectMapper.readTree(intent.slots.get("sys.color").value).path("color").asText();
            Integer colorVal=colorMap.get(color);
            duerContext.msgBuilder.textMessage("已经将"+homeLocStr+"灯的颜色设置为"+color);
            if(colorVal!=null){
                //mqtt 或者其他方式实现
                //sendColorMsg(homeLocStr,duerContext,colorVal);
            }else{
                /*
                  如果颜色不支持则再次询问颜色
                 */
                duerContext.msgBuilder.addDirective(directiveBuilder.toElicitSlot("sys.color").build());
                duerContext.msgBuilder.textMessage("目前还不支持"+color+"的颜色设置，换个颜色，比如红色");
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
