package xin.yysf.duer;

import com.baidu.duer.dcs.framework.message.DcsStreamRequestBody;
import com.baidu.duer.dcs.systeminterface.IAudioInput;
import com.baidu.duer.dcs.systeminterface.IHandler;
import com.baidu.duer.dcs.util.LogUtil;
import okio.BufferedSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class SimpleAudioInput implements IAudioInput {
    private static Logger log= LoggerFactory.getLogger(SimpleAudioInput.class);
    private final LinkedBlockingDeque<byte[]> linkedBlockingDeque;
    private final IHandler handler;

    private List<IAudioInputListener> listeners=new ArrayList<>();
    private AudioVoiceInputThread audioVoiceInputThread;

    public SimpleAudioInput(LinkedBlockingDeque<byte[]> linkedBlockingDeque,IHandler handler) {
        this.linkedBlockingDeque = linkedBlockingDeque;
        this.handler=handler;
    }
    @Override
    public void startRecord() {
        log.info("开始录音");


        DcsStreamRequestBody dcsStreamRequestBody = new DcsStreamRequestBody();
        for (IAudioInputListener listener : listeners) {
            listener.onStartRecord(dcsStreamRequestBody);
        }
        audioVoiceInputThread = new AudioVoiceInputThread(
                linkedBlockingDeque,
                dcsStreamRequestBody.sink(),
                handler);
        audioVoiceInputThread.setAudioInputListener(new AudioVoiceInputThread.IAudioInputListener() {
            @Override
            public void onWriteFinished() {
                for (IAudioInputListener listener : listeners) {
                    listener.onStopRecord();
                }
            }
        });
        audioVoiceInputThread.startWriteStream();



//        new Thread(()->{
//
//            AudioFormat af = new AudioFormat(16000, 16, 1, true, false);
//            DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
//            DcsStreamRequestBody dcsStreamRequestBody = new DcsStreamRequestBody();
//
//            for (IAudioInputListener listener : listeners) {
//                listener.onStartRecord(dcsStreamRequestBody);
//            }
//
//            BufferedSink sink = dcsStreamRequestBody.sink();
//            TargetDataLine targetDataLine = null;
//            try {
//                targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
//                targetDataLine.open(af);
//                targetDataLine.start();
//
//                long startTime = System.currentTimeMillis();
//                byte[] buff = new byte[4096];
//                int off = 0;
//                while (System.currentTimeMillis() - startTime < 50000&&bRec) {
//                    off = targetDataLine.read(buff, 0, buff.length);
//                    if (off > 0) {
//                        sink.write(buff, 0, off);
//                    }
//                }
//                sink.flush();
//                sink.close();
//                bRec=false;
//
//            }catch (Exception e){
//                e.printStackTrace();
//                bRec=false;
//            }finally {
//                if(targetDataLine!=null){
//                    targetDataLine.stop();
//                    targetDataLine.close();
//                }
//            }
//        }).start();
    }

    @Override
    public void stopRecord() {
        audioVoiceInputThread.stopWriteStream();
        log.info("结束录音");
        for (IAudioInputListener listener : listeners) {
            listener.onStopRecord();
        }
    }

    @Override
    public synchronized void registerAudioInputListener(IAudioInputListener audioInputListener) {
        if(!listeners.contains(audioInputListener)){
            listeners.add(audioInputListener);
        }
    }

    private static class AudioVoiceInputThread extends Thread{
        private static final String TAG = AudioVoiceInputThread.class.getSimpleName();
        private final IHandler handler;
        private volatile boolean isStart = false;
        private BufferedSink bufferedSink;
        // 音频数据
        private LinkedBlockingDeque<byte[]> linkedBlockingDeque;
        public AudioVoiceInputThread(LinkedBlockingDeque<byte[]> linkedBlockingDeque,
                                     BufferedSink bufferedSink,
                                     IHandler handler) {
            this.linkedBlockingDeque = linkedBlockingDeque;
            this.bufferedSink = bufferedSink;
            this.handler = handler;
        }
        /**
         * 开始写入数据
         */
        public void startWriteStream() {
            if (isStart) {
                return;
            }
            isStart = true;
            this.start();
        }
        /**
         * 停止写入数据
         */
        public void stopWriteStream() {
            isStart = false;
        }

        private boolean isfirst = true;

        @Override
        public void run() {
            super.run();
            while (isStart) {
                try {
                    byte[] recordAudioData = linkedBlockingDeque.pollFirst();
                    if (null != recordAudioData) {
                        if (isfirst) {
                            isfirst = false;
                        }
                        bufferedSink.write(recordAudioData);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtil.d(TAG, "writeTo IOException", e);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.d(TAG, "writeTo Exception", e);
                }
            }
            if (linkedBlockingDeque.size() > 0) {
                byte[] recordAudioData = linkedBlockingDeque.pollFirst();
                if (null != recordAudioData) {
                    LogUtil.d(TAG, "finally writeTo size:" + recordAudioData.length);
                    try {
                        bufferedSink.write(recordAudioData);
                    } catch (IOException e) {
                        e.printStackTrace();
                        LogUtil.d(TAG, " >0 writeTo IOException", e);
                    }
                }
            }
            try {
                bufferedSink.flush();
                bufferedSink.close();
                LogUtil.d(TAG, "closed");
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.d(TAG, "IOException ", e);
            }
            LogUtil.d(TAG, "onWriteFinished ");
            // 写入完成
            if (listener != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onWriteFinished();
                    }
                });
            }
        }

        private IAudioInputListener listener;

        public void setAudioInputListener(IAudioInputListener listener) {
            this.listener = listener;
        }

        /**
         * 写入完成的回调接口，比如当接收到StopListen指令后回触发
         */
        public interface IAudioInputListener {
            /**
             * 写入完成后回调此方法
             */
            void onWriteFinished();
        }
    }
}