package test;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Mixer;

public class JavaSoundTest {
    public static void main(String[] args) throws JavaLayerException {

        Mixer.Info[] arrMixerInfo = AudioSystem.getMixerInfo();

        for (Mixer.Info info : arrMixerInfo) {
            System.out.println("===========================================");
            System.out.println("Name:"+info.getName());
            System.out.println("Description:"+info.getDescription());
            System.out.println("Vendor:"+info.getVendor());
            System.out.println("Version:"+info.getVersion());
        }
        Player player=new Player(JavaSoundTest.class.getResourceAsStream("/vad_end.mp3"));
        player.play();
    }
}
