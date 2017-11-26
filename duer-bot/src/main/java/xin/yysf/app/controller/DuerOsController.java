package xin.yysf.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xin.yysf.duer.bot.DuerOsService;
import xin.yysf.duer.bot.entity.proto.DuerRequest;
import xin.yysf.duer.bot.entity.proto.DuerResponse;

@Controller
@RequestMapping("pub/bd/")
public class DuerOsController {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DuerOsService duerOsService;


    @RequestMapping("service")
    @ResponseBody
    public DuerResponse service(@RequestBody(required = false) DuerRequest request){
        return duerOsService.processRequest(request);
    }
}
