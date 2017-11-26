package xin.yysf.duer.bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xin.yysf.duer.bot.DuerOsService;
import xin.yysf.duer.bot.impl.DuerOsServiceImpl;

@Configuration
public class DuerOsConfiguration {
    /**
     * duerOsService 服务
     * @return 服务
     */
    @Bean
    public DuerOsService duerOsService(){
        return new DuerOsServiceImpl();
    }
}
