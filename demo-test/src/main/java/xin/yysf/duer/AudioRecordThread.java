package xin.yysf.duer;

import com.baidu.duer.dcs.systeminterface.IAudioRecord;
import com.baidu.duer.dcs.util.LogUtil;

import javax.sound.sampled.*;
import java.util.concurrent.LinkedBlockingDeque;

public class AudioRecordThread extends Thread implements IAudioRecord {

    private static final String TAG = AudioRecordThread.class.getSimpleName();
    // 采样率
    private static final int SAMPLE_RATE_HZ = 16000;
    private final TargetDataLine audioRecord;
    private final AudioFormat af;
    private int bufferSize=3200;
    private volatile boolean isStartRecord = false;
    private LinkedBlockingDeque<byte[]> linkedBlockingDeque;
    public AudioRecordThread(LinkedBlockingDeque<byte[]> linkedBlockingDeque) {
        this.linkedBlockingDeque = linkedBlockingDeque;

        af = new AudioFormat(16000, 16, 1, true, false);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);

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
        isStartRecord = true;
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
}
