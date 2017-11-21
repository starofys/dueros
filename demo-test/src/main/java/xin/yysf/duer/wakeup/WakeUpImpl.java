package xin.yysf.duer.wakeup;

import ai.kitt.snowboy.SnowboyDetect;
import com.baidu.duer.dcs.systeminterface.IHandler;
import com.baidu.duer.dcs.systeminterface.IWakeUp;
import com.baidu.duer.dcs.util.LogUtil;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class WakeUpImpl implements IWakeUp {
    private static final String TAG = WakeUpImpl.class.getSimpleName();
    private final LinkedBlockingDeque<byte[]> linkedBlockingDeque;
    private SnowboyDetect detector;
    private final IHandler handler;
    // Decode消费线程
    private WakeUpDecodeThread wakeUpDecodeThread;
    // callback
    private List<IWakeUpListener> wakeUpListeners;
    private boolean success;


    public WakeUpImpl(LinkedBlockingDeque<byte[]> linkedBlockingDeque, IHandler handler){
        this.linkedBlockingDeque=linkedBlockingDeque;
        this.wakeUpListeners = Collections.synchronizedList(new LinkedList<IWakeUpListener>());
        this.handler=handler;

        try {

            File file=new File("libsnowboy-detect-java.so");
            if(file.exists()) {
                System.load(file.getAbsolutePath());
                File file1=new File("resources/jarvis.pmdl");
                File file2=new File("resources/common.res");
                if(file1.exists()&&file2.exists()){
                    detector = new SnowboyDetect("resources/common.res",
                            "resources/jarvis.pmdl");
                    success=true;
                }else{
                    System.out.println("唤醒词不存在"+file1.getAbsolutePath());
                    System.out.println("唤醒词不存在"+file2.getAbsolutePath());
                }


            }else{
                System.out.println("唤醒开启失败,动态库不存在"+file.getAbsolutePath());
            }

        }catch (Exception e){
            LogUtil.e(TAG, "唤醒失败");
            e.printStackTrace();
        }


        //this.initWakeUp();
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public void startWakeUp() {
        if (wakeUpDecodeThread != null && wakeUpDecodeThread.isStart()) {
            LogUtil.d(TAG, "wakeup wakeUpDecodeThread  is Started !");
            return;
        }
        wakeUp();
    }

    @Override
    public void stopWakeUp() {
        if (wakeUpDecodeThread != null) {
            wakeUpDecodeThread.stopWakeUp();
        }
    }

    @Override
    public void releaseWakeUp() {

    }

    @Override
    public void addWakeUpListener(IWakeUpListener listener) {
        wakeUpListeners.add(listener);
    }

    /**
            * 开始音频解码进行唤醒操作
     */
    private void wakeUp() {
        if(detector!=null){
            wakeUpDecodeThread = new WakeUpDecodeThread(linkedBlockingDeque, detector, handler);
            wakeUpDecodeThread.setWakeUpListener(new WakeUpDecodeThread.IWakeUpListener() {
                @Override
                public void onWakeUpSucceed() {
                    // 唤醒成功
                    fireOnWakeUpSucceed();
                }
            });
            wakeUpDecodeThread.startWakeUp();
        }

    }

    private void fireOnWakeUpSucceed() {
        for (IWakeUpListener listener : wakeUpListeners) {
            listener.onWakeUpSucceed();
        }
    }
}
