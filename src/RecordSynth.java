/**
 * Author: Alex Worland
 * Date: 2/26/16
 * Description: CS111 Project 2
 */
import javax.sound.midi.*;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RecordSynth {

    public static void Synthesizer(String fileName, JProgressBar progressBar) throws InvalidMidiDataException,
            IOException, MidiUnavailableException {

        File file = new File(fileName);

        ArrayList<Integer> noteList = new ArrayList();
        ArrayList<Integer> intensityList = new ArrayList();
        ArrayList<Integer> durationList = new ArrayList();

        try {
            Scanner fileScan = new Scanner(file);
            // default is note, intensity, duration
            for (int i = 0; fileScan.hasNext(); i++) {
                if (fileScan.hasNext()) {
                    noteList.add(i, Math.abs(fileScan.nextInt()));
                }

                if (fileScan.hasNext()) {
                    intensityList.add(i, Math.abs(fileScan.nextInt()));
                }

                if (fileScan.hasNext()) {
                    durationList.add(i, Math.abs(fileScan.nextInt()));
                }
            }
            fileScan.close();
        } catch (NoSuchElementException e) {
            // optionpane window
            JOptionPane.showMessageDialog(null, "Error! NoSuchElementException!");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // optionpane window
            JOptionPane.showMessageDialog(null, "Error! File not found!");
            e.printStackTrace();
        }


        // set midi instrument
        int channel = 0;

        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();

        Sequence sequence = new Sequence(javax.sound.midi.Sequence.PPQ, 200);
        Track track1 = sequence.createTrack();

        ShortMessage sm = new ShortMessage();
        sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, channel, 0);
        track1.add(new MidiEvent(sm, 0));

        long tick = 0;
        
        int noteListLength = noteList.size();
        int intensityListLength = intensityList.size();
        int durationListLength = durationList.size();
        int loopLength;
        
        loopLength = (Math.min(noteListLength,Math.min(intensityListLength, durationListLength)));
            

        try {
            // Loop to set midi on/off message locations in the track
            for (int i = 0; i < loopLength; i++) {

                ShortMessage on = new ShortMessage();
                on.setMessage(ShortMessage.NOTE_ON, 0, noteList.get(i), intensityList.get(i));
                MidiEvent me1 = new MidiEvent(on, tick);
                track1.add(me1);

                ShortMessage off = new ShortMessage();
                tick += (long) durationList.get(i);
                off.setMessage(ShortMessage.NOTE_OFF, 0, noteList.get(i), intensityList.get(i));
                me1 = new MidiEvent(off, tick);
                track1.add(me1);

                progressBar.setValue(100 * (i+1)/loopLength);
            }
        } catch ( InvalidMidiDataException e) {
            // JOption Pane
            JOptionPane.showMessageDialog(null, "Error! Invalid Midi Data Exception!");
            e.printStackTrace();
        }

        int[] allowedTypes = MidiSystem.getMidiFileTypes(sequence);

        MidiSystem.write(sequence, allowedTypes[0], new File(fileName + ".mid"));
    }
}