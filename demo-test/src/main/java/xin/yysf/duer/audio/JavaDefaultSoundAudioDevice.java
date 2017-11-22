package xin.yysf.duer.audio;

import javazoom.jl.player.JavaSoundAudioDevice;

import javax.sound.sampled.*;

/**
 * 支持声卡选择的,修复树莓派没有声音的问题
 */
public class JavaDefaultSoundAudioDevice extends JavaSoundAudioDevice {
    protected Line createLine() throws LineUnavailableException {
        return createSourceDataLine(getSourceLineInfo());
    }
    public static SourceDataLine createSourceDataLine(DataLine.Info info){
        Mixer.Info[] arrMixerInfo = AudioSystem.getMixerInfo();
        if(arrMixerInfo!=null&&arrMixerInfo.length>0){
            //Mixer.Info ainfo = arrMixerInfo[0];
            Mixer mixer = AudioSystem.getMixer(arrMixerInfo[0]);
            try {
                Line finfo = mixer.getLine(info);
                if(finfo instanceof SourceDataLine){
                    SourceDataLine sinfo = (SourceDataLine) finfo;
                    return sinfo;
                }
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
