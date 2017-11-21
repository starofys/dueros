package xin.yysf.duer;

import com.baidu.duer.dcs.framework.message.DcsStreamRequestBody;
import com.baidu.duer.dcs.systeminterface.IAudioInput;
import com.baidu.duer.dcs.systeminterface.IAudioRecord;
import com.baidu.duer.dcs.systeminterface.IWakeUp;
import com.baidu.duer.dcs.util.LogUtil;

import javax.sound.sampled.*;
import java.util.concurrent.LinkedBlockingDeque;

public class AudioRecordThread extends Thread implements IAudioRecord, IAudioInput.IAudioInputListener {

    private static final String TAG = AudioRecordThread.class.getSimpleName();
    // 采样率
    private static final int SAMPLE_RATE_HZ = 16000;
    private final TargetDataLine audioRecord;
    private final AudioFormat af;
    private final IWakeUp iWakeUp;
    private int bufferSize=3200;
    private volatile boolean isStartRecord = false;
    private LinkedBlockingDeque<byte[]> linkedBlockingDeque;
    private boolean bStart=false;
    public AudioRecordThread(LinkedBlockingDeque<byte[]> linkedBlockingDeque, IWakeUp iWakeUp) {
        this.linkedBlockingDeque = linkedBlockingDeque;

        af = new AudioFormat(16000, 16, 1, true, false);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
        this.iWakeUp=iWakeUp;

        try {
            audioRecord = (TargetDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void startRecord() {
        if (isStartRecord) {
            return;
        }
        isStartRecord=true;
        this.start();
    }

    @Override
    public void stopRecord() {
        isStartRecord = false;
    }



    @Override
    public void run() {

        LogUtil.i(TAG, "audioRecord startRecording ");
        try {
            audioRecord.open(af);
            audioRecord.start();
            byte[] buffer = new byte[bufferSize];
            while (isStartRecord) {
                if(!iWakeUp.isSuccess()){
                    if(!bStart){
                        synchronized (af){
                            try {
                                af.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    }
                }
                int readBytes = audioRecord.read(buffer, 0, bufferSize);
                if (readBytes > 0) {
                    linkedBlockingDeque.add(buffer);
                }
            }
            // 清空数据
            linkedBlockingDeque.clear();
            LogUtil.i(TAG, "audioRecord release ");
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }finally {
            audioRecord.stop();
            audioRecord.close();
        }

    }

    @Override
    public void onStartRecord(DcsStreamRequestBody dcsStreamRequestBody) {
        bStart=true;
        synchronized (af){
            af.notify();
        }
    }

    @Override
    public void onStopRecord() {
        bStart=false;
    }
}
