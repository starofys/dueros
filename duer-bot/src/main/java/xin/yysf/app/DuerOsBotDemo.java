package xin.yysf.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import xin.yysf.duer.bot.DuerOsService;
import xin.yysf.duer.bot.config.DuerOsConfiguration;
import xin.yysf.duer.bot.entity.cus.IntentDesc;
import xin.yysf.duer.bot.entity.cus.SlotDesc;
import xin.yysf.duer.bot.impl.SwitchControlIntentHandler;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication

/**
 * 打开DuerOs 意图支持
 */
@Import(DuerOsConfiguration.class)


public class DuerOsBotDemo {
    @Autowired
    private DuerOsService duerOsService;

    @PostConstruct
    public void init(){
        /**
         * 定义槽
         */
        List<SlotDesc> slots=new ArrayList<>();
        slots.add(new SlotDesc("device_name",true,"什么设备？"));
        slots.add(new SlotDesc("switch_control",true,"打开还是关闭"));
        slots.add(new SlotDesc("home_loc",true,"哪里的？"));
        IntentDesc desc=new IntentDesc("switch_control",slots);

        /**
         * 定义处理类
         */
        SwitchControlIntentHandler handler=new SwitchControlIntentHandler();

        duerOsService.addIntentHandler(desc,handler);
    }



    public static void main(String[] args) {
        SpringApplication.run(DuerOsBotDemo.class);
    }
}
