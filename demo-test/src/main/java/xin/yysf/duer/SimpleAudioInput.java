package xin.yysf.duer;

import com.baidu.duer.dcs.framework.message.DcsStreamRequestBody;
import com.baidu.duer.dcs.systeminterface.IAudioInput;
import okio.BufferedSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.util.ArrayList;
import java.util.List;

public class SimpleAudioInput implements IAudioInput {
    private static Logger log= LoggerFactory.getLogger(SimpleAudioInput.class);

    public boolean bRec;
    private List<IAudioInputListener> listeners=new ArrayList<>();
    @Override
    public void startRecord() {
        log.info("开始录音");
        bRec = true;

        new Thread(()->{

            AudioFormat af = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
            DcsStreamRequestBody dcsStreamRequestBody = new DcsStreamRequestBody();

            for (IAudioInputListener listener : listeners) {
                listener.onStartRecord(dcsStreamRequestBody);
            }

            BufferedSink sink = dcsStreamRequestBody.sink();
            TargetDataLine targetDataLine = null;
            try {
                targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
                targetDataLine.open(af);
                targetDataLine.start();

                long startTime = System.currentTimeMillis();
                byte[] buff = new byte[4096];
                int off = 0;
                while (System.currentTimeMillis() - startTime < 50000&&bRec) {
                    off = targetDataLine.read(buff, 0, buff.length);
                    if (off > 0) {
                        sink.write(buff, 0, off);
                    }
                }
                sink.flush();
                sink.close();
                bRec=false;

            }catch (Exception e){
                e.printStackTrace();
                bRec=false;
            }finally {
                if(targetDataLine!=null){
                    targetDataLine.stop();
                    targetDataLine.close();
                }
            }
        }).start();
    }

    @Override
    public void stopRecord() {
        bRec=false;
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
}