package xin.yysf.duer;

import com.baidu.duer.dcs.Log;
import com.baidu.duer.dcs.framework.DcsStream;
import com.baidu.duer.dcs.systeminterface.IMediaPlayer;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class SimpleMediaPlayer implements IMediaPlayer {
    boolean mute;
    private boolean active = false;
    private List<IMediaPlayerListener> mediaPlayerListeners;
    private PlayState mCurrentState;
    private float currentVolume=0.8f;
    private WriteWorkThread writeWorkThread;
    private SimpleMediaPlayer.WriteWorkThread thread;
    private Player player;
    private InputStream inStream;

    public SimpleMediaPlayer(){
        mediaPlayerListeners = Collections.synchronizedList(new LinkedList<IMediaPlayerListener>());
    }


    @Override
    public PlayState getPlayState() {
        return PlayState.STOPPED;
    }

    private SourceDataLine initAudioTrack(int sampleRate, int channels) {
        if (sampleRate <= 0) {
            sampleRate = 8000;
        }
        if (channels <= 0) {
            channels = 1;
        }
        AudioFormat af = new AudioFormat(sampleRate, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
        SourceDataLine line = null;
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(af, 1152);
            return line;
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;

    }
    private void prepared() {
        mCurrentState = PlayState.PREPARED;
        fireOnPrepared();
        //  一开始就说话让它静音了
        if (mute) {
            //mAudioTrack.setStereoVolume(0, 0);
        } else {
            setVolume(currentVolume);
        }
    }
    @Override
    public void play(MediaResource mediaResource) {

        fireOnInit();
        prepared();

        if(mediaResource.dcsStream!=null){
            play(mediaResource.dcsStream);
        }
        if (mediaResource.isStream) {
            play(mediaResource.stream);
        } else {
            play(mediaResource.url);
        }


    }

    private void play(String url) {
        try {
            if(url!=null&&url.startsWith("http")){
                URL murl =new URL(url);
                inStream = murl.openStream();
                play(inStream);
            }else{
                System.out.println("无法播放资源:"+url);
            }

        } catch (Exception e) {
            System.out.println(url);
            e.printStackTrace();
        }
    }

    private void play(InputStream in) {
//        if(inStream!=null){
//            try {
//                inStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        if(player!=null){
            player.close();
        }
        try {
            Player m=player=new Player(in);
            new Thread(() -> {
                try {
                    m.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
                m.close();
                stop();
            }).start();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }

    private void play(DcsStream dcsStream) {
        if (mCurrentState != PlayState.PLAYING) {
            mCurrentState = PlayState.PLAYING;
            firePlaying();
        }
        Log.i("simpleMediaPlayer", "Decoder-START WriteWorkThread");


        if (dcsStream != null) {

            thread=new WriteWorkThread(dcsStream);
            thread.start();
        }
    }

    private final class WriteWorkThread extends Thread {
        private DcsStream dcsStream;
        private LinkedBlockingDeque<byte[]> deque;
        private volatile boolean isStop;
        private int needWriteTotal;

        public WriteWorkThread(DcsStream dcsStream) {
            this.dcsStream = dcsStream;
            this.deque = this.dcsStream.dataQueue;
        }

        public void stopWrite() {
            isStop = true;
            this.deque.clear();
            this.interrupt();
        }

        @Override
        public void run() {
            SourceDataLine line = initAudioTrack(dcsStream.sampleRate, dcsStream.channels);
            if (line == null) {
                return;
            }
            int needWriteTotal = 1152 + 400;
            line.start();
            try {
                while (!isStop && !dcsStream.isFinished()) {
                    byte[] writeBytes = deque.take();
                    if (writeBytes != null && writeBytes.length > 0) {
                        // writeBytes.length always 1152 ( mp3 frame )
                        if (needWriteTotal <= 0) {
                            line.write(writeBytes, 0, writeBytes.length);
                        } else {
                            int ret = needWriteTotal - writeBytes.length;
                            if (ret <= 0) {
                                long start1 = System.currentTimeMillis();
                                byte[] buffer1 = new byte[needWriteTotal];
                                System.arraycopy(writeBytes, 0, buffer1, 0, buffer1.length);
                                int bytesWritten = line.write(buffer1, 0, buffer1.length);
                                long end1 = System.currentTimeMillis();
                                Log.i("aaaa", "Decoder-write  if," + bytesWritten + "," + (end1 - start1));
                                Log.i("Decoder", "满足有声音:" + System.currentTimeMillis());

                                byte[] buffer2 = new byte[writeBytes.length - buffer1.length];
                                System.arraycopy(writeBytes, buffer1.length, buffer2, 0, buffer2.length);
                                line.write(buffer2, 0, buffer2.length);
                            } else {
                                long start1 = System.currentTimeMillis();
                                int bytesWritten = line.write(writeBytes, 0, writeBytes.length);
                                long end1 = System.currentTimeMillis();
                                Log.i("aaaa", "Decoder-write else ," + bytesWritten + "," + (end1 - start1));
                            }
                            needWriteTotal -= writeBytes.length;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(line!=null) {
                    line.drain();
                    line.stop();
                    line.close();
                }
            }
            if (mCurrentState == PlayState.PLAYING) {
                mCurrentState = PlayState.COMPLETED;
                fireOnCompletion();
            } else {
                mCurrentState = PlayState.COMPLETED;
            }
            System.out.printf("结束+====================================");
        }
    }


    @Override
    public void pause() {
        if (mCurrentState == PlayState.PLAYING
                || mCurrentState == PlayState.PREPARED
                || mCurrentState == PlayState.PREPARING) {
            mCurrentState = PlayState.PAUSED;

            fireOnPaused();
            if(player!=null){
                player.close();
                player=null;
            }
        }
    }

    private void getAudioTrackCurrentPosition() {
    }

    @Override
    public void stop() {
        getAudioTrackCurrentPosition();
        mCurrentState = PlayState.STOPPED;
        if (writeWorkThread != null) {
            writeWorkThread.stopWrite();
        }
        fireStopped();
        if(player!=null){
            player.close();
            player=null;
        }
    }

    @Override
    public void resume() {
        if (mCurrentState == PlayState.PAUSED) {
            mCurrentState = PlayState.PLAYING;
            firePlaying();
        }
    }

    @Override
    public void seekTo(int milliseconds) {
        //throw new RuntimeException("unSupport seekTo.");
    }

    @Override
    public void release() {
        mCurrentState = PlayState.IDLE;
        if (writeWorkThread != null) {
            writeWorkThread.stopWrite();
        }
        if(player!=null){
            player.close();
            player=null;
        }
        fireOnRelease();
        mediaPlayerListeners.clear();
    }

    /**
     * 设置音量
     *
     * @param volume 0.0 -1.0
     */
    @Override
    public void setVolume(float volume) {
        // 设置音量就不再静音了，比如：说了调衡音量等操作
        currentVolume=volume;
    }

    @Override
    public float getVolume() {
        return currentVolume;
    }

    @Override
    public void setMute(boolean mute) {
        this.mute = mute;
    }

    @Override
    public boolean getMute() {
        return mute;
    }

    @Override
    public long getCurrentPosition() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public float getBufferPercentage() {
        return 0;
    }

    @Override
    public void addMediaPlayerListener(IMediaPlayerListener listener) {
        if (!mediaPlayerListeners.contains(listener)) {
            mediaPlayerListeners.add(listener);
        }
    }

    @Override
    public void removeMediaPlayerListener(IMediaPlayerListener listener) {
        if (mediaPlayerListeners.contains(listener)) {
            mediaPlayerListeners.remove(listener);
        }
    }

    @Override
    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    private void fireOnInit() {
        for (IMediaPlayerListener listener : mediaPlayerListeners) {
            if (listener != null) {
                listener.onInit();
            }
        }
    }

    private void fireOnPrepared() {
        for (IMediaPlayerListener listener : mediaPlayerListeners) {
            if (listener != null) {
                listener.onPrepared();
            }
        }
    }

    private void fireOnPaused() {
        for (IMediaPlayerListener listener : mediaPlayerListeners) {
            if (listener != null) {
                listener.onPaused();
            }
        }
    }

    private void fireStopped() {
        for (IMediaPlayerListener listener : mediaPlayerListeners) {
            if (listener != null) {
                listener.onStopped();
            }
        }
    }

    private void fireOnRelease() {
        for (IMediaPlayerListener listener : mediaPlayerListeners) {
            if (listener != null) {
                listener.onRelease();
            }
        }
    }

    private void firePlaying() {
        for (IMediaPlayerListener listener : mediaPlayerListeners) {
            if (listener != null) {
                listener.onPlaying();
            }
        }
    }

    private void fireOnCompletion() {
        for (IMediaPlayerListener listener : mediaPlayerListeners) {
            if (listener != null) {
                listener.onCompletion();
            }
        }
    }
}