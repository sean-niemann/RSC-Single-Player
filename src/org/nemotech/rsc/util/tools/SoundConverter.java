//package org.nemotech.rsc.util.tools;
//
//import com.sun.media.sound.Toolkit;
//
//import java.io.*;
//import java.util.ArrayList;
//
//import javax.sound.sampled.AudioFileFormat;
//import javax.sound.sampled.AudioFormat;
//import javax.sound.sampled.AudioInputStream;
//import javax.sound.sampled.AudioSystem;
//
//import org.nemotech.rsc.client.BZLib;
//import org.nemotech.rsc.misc.Util;
//
///**
// * 
// * Converts JAG archived PCM sounds into WAV files
// * 
// */
//public class SoundConverter {
//    
//    public static void main(String[] argv) {
//        SoundConverter ripper = new SoundConverter();
//        byte[] audio = ripper.load("sounds.mem");
//        ArrayList<String> sounds = new ArrayList<>();
//        sounds.add("combat1a"); //miss
//        sounds.add("combat1b"); //hit
//        sounds.add("combat2a"); //armor miss
//        sounds.add("combat2b"); //armor hit
//        sounds.add("combat3a"); //undead miss
//        sounds.add("combat3b"); //undead hit
//        sounds.add("retreat");
//        sounds.add("shoot");
//        sounds.add("underattack");
//        sounds.add("click");
//        sounds.add("victory");
//        sounds.add("advance"); //levelup
//        sounds.add("dropobject");
//        sounds.add("eat");
//        sounds.add("spellok");
//        sounds.add("chisel");
//        sounds.add("opendoor");
//        sounds.add("filljug");
//        sounds.add("mechanical");
//        sounds.add("cooking");
//        sounds.add("anvil");
//        sounds.add("potato"); //pick item
//        sounds.add("recharge"); //prayer
//        sounds.add("fish");
//        sounds.add("closedoor");
//        sounds.add("takeobject");
//        sounds.add("coins");
//        sounds.add("spellfail");
//        sounds.add("secretdoor");
//        sounds.add("mine");
//        sounds.add("death");
//        sounds.add("foundgem");
//        sounds.add("prospect");
//        sounds.add("outofammo");
//        sounds.add("prayeroff");
//        sounds.add("prayeron");
//    
//
//        for(String sound : sounds) {
//            int offset = Util.getDataFileOffset(sound + ".pcm", audio);
//            int length = Util.getDataFileLength(sound + ".pcm", audio);
//            byte[] soundBytes = new byte[length];
//            System.arraycopy(audio, offset, soundBytes, 0, length);
//            save("sounds/" + sound + ".wav", soundBytes);
//        }
//
//    }
//
//    public static void save(String filename, byte[] input) {
//        AudioFormat format = new AudioFormat(AudioFormat.Encoding.ULAW, 8000, 8, 1, 1, 8000, true);
//        try {
//            ByteArrayInputStream bais = new ByteArrayInputStream(input);
//            AudioInputStream ais = new AudioInputStream(bais, format, AudioSystem.NOT_SPECIFIED);
//            ais = Toolkit.getPCMConvertedAudioInputStream(ais);
//            if (filename.endsWith(".wav") || filename.endsWith(".WAV")) {
//                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//            System.exit(1);
//        }
//    }
//
//    protected byte[] load(String filename) {
//        int j = 0;
//        int k = 0;
//        byte data[] = null;
//        try {
//            InputStream inputstream = Util.openFile(filename);
//            DataInputStream datainputstream = new DataInputStream(inputstream);
//            byte settings[] = new byte[6];
//            datainputstream.readFully(settings, 0, 6);
//            j = ((settings[0] & 0xff) << 16) + ((settings[1] & 0xff) << 8)
//                    + (settings[2] & 0xff);
//            k = ((settings[3] & 0xff) << 16) + ((settings[4] & 0xff) << 8)
//                    + (settings[5] & 0xff);
//            int l = 0;
//            data = new byte[k];
//            while (l < k) {
//                int i1 = k - l;
//                if (i1 > 1000) {
//                    i1 = 1000;
//                }
//                datainputstream.readFully(data, l, i1);
//                l += i1;
//            }
//            datainputstream.close();
//        } catch (IOException _ex) {
//            _ex.printStackTrace();
//        }
//        if (k != j) {
//            byte clip[] = new byte[j];
//            BZLib.decompress(clip, j, data, k, 0);
//            return clip;
//        } else {
//            return data;
//        }
//    }
//    
//}