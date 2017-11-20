package xin.yysf.duer;

import com.baidu.duer.dcs.systeminterface.*;
import xin.yysf.duer.wakeup.WakeUpImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class WindowsPlatformFactory implements IPlatformFactory {
    private SimpleMediaPlayer simpleMediaPlayer=new SimpleMediaPlayer();
    private SimpleAudioInput audioInput=new SimpleAudioInput();
    private IWebView webView;
    private ExecutorService service= Executors.newFixedThreadPool(2);


    private LinkedBlockingDeque<byte[]> linkedBlockingDeque = new LinkedBlockingDeque<>();

    private IHandler handler=new IHandler() {
        @Override
        public boolean post(Runnable runnable) {
            service.execute(runnable);
            return true;
        }
    };

    @Override
    public IHandler createHandler() {
        return handler;
    }

    @Override
    public IHandler getMainHandler() {
        return handler;
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
        return new WakeUpImpl(linkedBlockingDeque,handler);
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
