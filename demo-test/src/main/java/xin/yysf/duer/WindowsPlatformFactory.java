package xin.yysf.duer;

import com.baidu.duer.dcs.systeminterface.*;
import xin.yysf.gui.DuerOSGui;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WindowsPlatformFactory implements IPlatformFactory {
    private SimpleMediaPlayer simpleMediaPlayer=new SimpleMediaPlayer();
    private SimpleAudioInput audioInput=new SimpleAudioInput();
    private IWebView webView;
    private ExecutorService service= Executors.newFixedThreadPool(2);
    @Override
    public IHandler createHandler() {
        return null;
    }

    @Override
    public IHandler getMainHandler() {
        return new IHandler() {
            @Override
            public boolean post(Runnable runnable) {
                service.execute(runnable);
//                try {
//                    runnable.run();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                return true;
            }
        };
    }
    public void shutdown(){
        service.shutdownNow();
    }

    @Override
    public IAudioRecord getAudioRecord() {
        return null;
    }

    @Override
    public IWakeUp getWakeUp() {
        return null;
    }

    @Override
    public IAudioInput getVoiceInput() {
        return audioInput;
    }

    @Override
    public IMediaPlayer createMediaPlayer() {
        return new SimpleMediaPlayer();
    }

    @Override
    public IMediaPlayer createAudioTrackPlayer() {
        return simpleMediaPlayer;
    }

    @Override
    public IAlertsDataStore createAlertsDataStore() {
        return null;
    }

    @Override
    public IWebView getWebView() {
        return webView;
    }

    @Override
    public void setWebView(IWebView webView) {
        this.webView=webView;
    }

    @Override
    public IPlaybackController getPlayback() {
        return null;
    }
}
