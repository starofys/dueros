package test;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Mixer;

public class JavaSoundTest {
    public static void main(String[] args) {

        Mixer.Info[] arrMixerInfo = AudioSystem.getMixerInfo();

        for (Mixer.Info info : arrMixerInfo) {
            System.out.println("===========================================");
            System.out.println("Name:"+info.getName());
            System.out.println("Description:"+info.getDescription());
            System.out.println("Vendor:"+info.getVendor());
            System.out.println("Version:"+info.getVersion());
        }
    }
}
