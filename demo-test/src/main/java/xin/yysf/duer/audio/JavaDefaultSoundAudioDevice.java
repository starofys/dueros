package xin.yysf.duer.audio;

import javazoom.jl.player.JavaSoundAudioDevice;

import javax.sound.sampled.*;

/**
 * 支持声卡选择的,修复树莓派没有声音的问题  plughw:2,0
 */
public class JavaDefaultSoundAudioDevice extends JavaSoundAudioDevice {
    private static  Mixer mixer;
    static {
        Mixer.Info[] arrMixerInfo = AudioSystem.getMixerInfo();
        if(arrMixerInfo!=null&&arrMixerInfo.length>0){
            for (Mixer.Info info1 : arrMixerInfo) {
                if(info1.getName().contains("plughw:2,0")){
                    mixer = AudioSystem.getMixer(info1);
                }
            }
        }
    }
    protected Line createLine() throws LineUnavailableException {
        return createSourceDataLine(getSourceLineInfo());
    }
    public static SourceDataLine createSourceDataLine(DataLine.Info info){
        if(mixer!=null){
            try {
                SourceDataLine line= (SourceDataLine) mixer.getLine(info);
                return line;
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
        SourceDataLine line = null;
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return line;
    }
}
