package test;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.sound.sampled.*;
import java.net.URL;
import java.util.Scanner;

public class JavaSoundTest {
    public static void main(String[] args) throws Exception {



//
//
//
//
//
//
//
//        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new URL("http://yinyueshiting.baidu.com/data2/music/205470cfbebc31fc2626b8db87423392/541990313/541990313.mp3?xcode=cc2dc8065d38981d223d24c5de246011"));
//
//        Mixer.Info[] arrMixerInfo = AudioSystem.getMixerInfo();
//
//        AudioFormat af = new AudioFormat(16000, 16, 1, true, false);
//        DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
//
//
//        mixer.getLine(info);
//
//
//// Get a sound clip resource.
//        Clip clip = AudioSystem.getClip(arrMixerInfo[1]);
//
//// Open audio clip and load samples from the audio input stream.
//        clip.open(audioIn);
//        clip.start();
//        clip.drain();
//        clip.close();

        Scanner scanner=new Scanner(System.in);
        Mixer.Info[] arrMixerInfo = AudioSystem.getMixerInfo();
        AudioFormat af = new AudioFormat(24000, 16, 1, true, false);
        int i=0;
        for (Mixer.Info info : arrMixerInfo) {
            System.out.println("==========================================="+i);
            i++;
            System.out.println("Name:"+info.getName());
            System.out.println("Description:"+info.getDescription());
            System.out.println("Vendor:"+info.getVendor());
            System.out.println("Version:"+info.getVersion());

            DataLine.Info infoa = new DataLine.Info(SourceDataLine.class, af);
            Mixer mixer = AudioSystem.getMixer(info);
            try {
                Line linxe = mixer.getLine(infoa);
                if(linxe instanceof SourceDataLine ){
                    test((SourceDataLine)linxe,af);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            scanner.nextLine();

        }
    }

    public static void test(SourceDataLine line,AudioFormat af) throws LineUnavailableException {
        // generate some PCM data (a sine wave for simplicity)
        byte[] buffer = new byte[64];
        double step = Math.PI / buffer.length;
        double angle = Math.PI * 2;
        int i = buffer.length;
        while (i > 0) {
            double sine = Math.sin(angle);
            int sample = (int) Math.round(sine * 32767);
            buffer[--i] = (byte) (sample >> 8);
            buffer[--i] = (byte) sample;
            angle -= step;
        }

        // prepare audio output
        line.open(af, 4096);
        line.start();
        // output wave form repeatedly
        for (int n=0; n<500; ++n) {
            line.write(buffer, 0, buffer.length);
        }
        // shut down audio
        line.drain();
        line.stop();
        line.close();
    }
}
