package xin.yysf.gui;

import com.baidu.duer.dcs.framework.message.DcsStreamRequestBody;
import com.baidu.duer.dcs.systeminterface.IAudioInput;
import com.baidu.duer.dcs.systeminterface.IWebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xin.yysf.duer.DuerSdkApp;

import java.util.Scanner;


public class DuerOsCmd implements IWebView, IAudioInput.IAudioInputListener {
    private static Logger logger= LoggerFactory.getLogger(DuerOsCmd.class);
    private DuerSdkApp sdkApp;
    private boolean record;
    private boolean recordimg;
    public static void main(String[] args) {

        if(args.length!=0){
            if("gui".equals(args[0])){
                DuerOsSwtGui.main1(args);
                return;
            }
        }

        DuerOsCmd duerOsCmd=new DuerOsCmd();
        duerOsCmd.initDuerOs();

    }
    private void initDuerOs() {
        sdkApp= DuerSdkApp.createDuerOs();
        sdkApp.setWebView(this);
        sdkApp.factory.getVoiceInput().registerAudioInputListener(this);

        loadContent("<h1>HelloDuerOS</h1>");
        if(sdkApp.token==null){
            loadContent("Oauth2添加回调<b>http://127.0.0.1:8080/auth</b>然后访问<p>"+sdkApp.url+"</p>");
        }

        Scanner scanner=new Scanner(System.in);
        while (true){
            if("exit".equals(scanner.nextLine())){
                sdkApp.shutdown();
                System.exit(1);
            }
            if(recordimg){
                continue;
            }
            if(record){
                sdkApp.factory.getVoiceInput().stopRecord();
            }else{
                sdkApp.factory.getVoiceInput().startRecord();
            }

        }

    }

    @Override
    public void onStartRecord(DcsStreamRequestBody dcsStreamRequestBody) {
        record=true;
    }

    @Override
    public void onStopRecord() {
        record=false;
    }

    @Override
    public void loadUrl(String url) {
        logger.info("返回网页数据:{}",url);
    }

    @Override
    public void loadContent(String s) {
        logger.info(s);
    }

    @Override
    public void linkClicked(String url) {

    }

    @Override
    public void addWebViewListener(IWebViewListener listener) {

    }

    @Override
    public void setText(String text) {
        logger.info("text:",text);
    }
    
}
