package xin.yysf.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import xin.yysf.duer.bot.config.DuerOsConfiguration;

@SpringBootApplication

/**
 * 打开DuerOs 意图支持
 */
@Import(DuerOsConfiguration.class)


public class DuerOsBotDemo {
    public static void main(String[] args) {
        SpringApplication.run(DuerOsBotDemo.class);
    }
}
