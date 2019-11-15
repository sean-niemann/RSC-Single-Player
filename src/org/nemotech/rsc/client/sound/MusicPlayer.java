package org.nemotech.rsc.client.sound;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.midi.InvalidMidiDataException;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

import org.nemotech.rsc.client.mudclient;
import org.nemotech.rsc.Constants;
import org.nemotech.rsc.util.Util;

public class MusicPlayer {
    
    private Sequencer sequencer;
    
    public MusicPlayer() {
        try {
            /* obtains the default sequencer connected to a default device */
            sequencer = MidiSystem.getSequencer();
            /* opens the device */
            sequencer.open();
            sequencer.addMetaEventListener((MetaMessage event) -> {
                /* done playing */
                if(event.getType() == 47) {
                    mudclient.getInstance().musicStopped = true;
                }
            });
        } catch(MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public void startNewPlayerSong() {
        new Thread(() -> {
            try {
                InputStream is = new BufferedInputStream(new FileInputStream(Constants.CACHE_DIRECTORY + "audio" + File.separator + "music" + File.separator + "book_of_spells.midi"));
                sequencer.setSequence(is);
            } catch(IOException | InvalidMidiDataException e) {
                e.printStackTrace();
            }
            sequencer.start();
        }).start();
    }
    
    public void start() {
        new Thread(() -> {
            try {
                File directory = new File(Constants.CACHE_DIRECTORY + "audio" + File.separator + "music" + File.separator);
                FilenameFilter filter = (File dir, String name) -> {
                    return name.endsWith(".midi");
                };
                File[] musicFiles = directory.listFiles(filter);
                String fileName = musicFiles[Util.random(musicFiles.length - 1)].getName();
                InputStream is = new BufferedInputStream(new FileInputStream(directory.getAbsolutePath() + File.separator + fileName));
                sequencer.setSequence(is);
            } catch(IOException | InvalidMidiDataException e) {
                e.printStackTrace();
            }
            sequencer.start();
        }).start();
    }

    public void stop() {
        sequencer.stop();
    }
    
    public boolean isRunning() {
        return sequencer.isRunning();
    }

}