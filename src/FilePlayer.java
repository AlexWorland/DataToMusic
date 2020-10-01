/**
 * Author: Alex Worland
 * Date: 2/26/16
 * Description: CS111 Project 2
 */

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;

public class FilePlayer extends JFrame {

    // Declare references to all GUI components and the different worker threads
	private JPanel panel;
	private JLabel fileToConvert;
	private JTextField fileNameField;
	private JLabel fileToPlay;
	private JTextField fileToPlayField;
	private JLabel durationMultiplier;
	private JTextField durationMultiplierField;
	private JTextField wordToMidiField;
	private JLabel playLabel;
	private JLabel midiLabel;
	private JLabel byteLabel;
	private JLabel imageLabel;
	private JLabel wordToMidiLabel;
	private JButton bytesButton;
	private JButton playButton;
	private JButton pauseResumeButton;
	private JButton stopButton;
    private JButton toMidiButton;
    private JButton toImageButton;
	private JButton wordToMidiButton;
	private JProgressBar playProgressBar;
	private JProgressBar byteProgressBar;
	private JProgressBar midiProgressBar;
	private JProgressBar imageProgressBar;
	private final int WINDOW_HEIGHT = 690;
	private final int WINDOW_WIDTH = 190;


	// Worker threads for time intensive tasks
	private playWorker pWorker;
	private pictureWorker picWorker;
	private ToMidiWorker toMidiWorker;
	private ToBytesWorker bytesWorker;

	// constructor
	public FilePlayer() {

		// set window title
		setTitle("File Player");

		// set size of window
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

		// exit behaviour
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// build the panel and add it to frame
		buildPanel();

		// add panel to frame
		add(panel);

		// set resize to false
		setResizable(false);

		// display the window
		setVisible(true);
	}

	// buildPanel method
	private void buildPanel() {

		// create a label to display fileName
		fileToConvert = new JLabel("Filename to convert:");

		// create a label to display file to play
		fileToPlay = new JLabel("Filename to play/export:");

		// create a label to display note duration multiplier
		durationMultiplier = new JLabel("Note duration multiplier: ");

		// create labels for progress bars
		playLabel = new JLabel("Play Progress:");
		midiLabel = new JLabel("Midi Conversion Progress:");
		byteLabel = new JLabel("Byte Conversion Progress:");
		imageLabel = new JLabel("Image Conversion Progress:");

		// Create stuff for word to midi
		wordToMidiButton = new JButton("Convert");
		wordToMidiField = new JTextField(10);
		wordToMidiLabel = new JLabel("Convert Word To Midi");

		// create a text field 10 characters wide
		durationMultiplierField = new JTextField(10);
		durationMultiplierField.setText("1");

		// create a text field 10 characters wide
		fileNameField = new JTextField(10);

		// create a text field 10 characters long
		fileToPlayField = new JTextField(10);

		// create a new button
		bytesButton = new JButton("Convert to Bytes");

		// create new button
		playButton = new JButton("Play");
		
		// create pause button
		pauseResumeButton = new JButton("Pause/Resume");
		
		// create stop button
		stopButton = new JButton("Stop");

        // create to midi button
        toMidiButton = new JButton("Export To Midi File");

        // to image button
        toImageButton = new JButton("Export as Image");

		// add an action listener to the button
		bytesButton.addActionListener(new BytesButtonActionListener());

		// add an action listener to the button
		playButton.addActionListener(new PlayButtonActionListener());
		
		// pauseButton action listener
		pauseResumeButton.addActionListener(new PauseResumeButtonActionListener());

		//stopButton action listener
		stopButton.addActionListener(new StopButtonActionListener());

        // to midi action listener
        toMidiButton.addActionListener(new ToMidiButtonActionListener());

        // to image action listener
		toImageButton.addActionListener(new ToImageButtonActionListener());

		// word to midi action listener
		wordToMidiButton.addActionListener(new WordToMidiActionListener());

		// create play progress bar
		playProgressBar = new JProgressBar();

		// create midi progressbar
		midiProgressBar = new JProgressBar();

		// create byte progressBar
		byteProgressBar = new JProgressBar();

		// create image Progress bar
		imageProgressBar = new JProgressBar();

		// Set layout of frame to FlowLayout
		FlowLayout experimentLayout = new FlowLayout();

		// create a JPanel object
		panel = new JPanel();

		// assign the flow layout to the panel
		panel.setLayout(experimentLayout);

		// add components to panel
		panel.add(wordToMidiLabel);
		panel.add(wordToMidiField);
		panel.add(wordToMidiButton);
		panel.add(fileToConvert);
		panel.add(fileNameField);
		panel.add(fileToPlay);
		panel.add(fileToPlayField);
		panel.add(durationMultiplier);
		panel.add(durationMultiplierField);
		panel.add(bytesButton);
		panel.add(playButton);
		panel.add(pauseResumeButton);
		panel.add(stopButton);
        panel.add(toMidiButton);
        panel.add(toImageButton);
		panel.add(playLabel);
		panel.add(playProgressBar);
		panel.add(byteLabel);
		panel.add(byteProgressBar);
		panel.add(midiLabel);
		panel.add(midiProgressBar);
		panel.add(imageLabel);
		panel.add(imageProgressBar);
	}

	// Action Listeners for each button

	private class BytesButtonActionListener implements ActionListener {
		// executes when button is pressed

        // Converts a user defined file into a text file containing the absolute value of Byte data (0-127)
		@Override
		public void actionPerformed(ActionEvent e) {
			String fileName = fileNameField.getText();

			bytesWorker = new ToBytesWorker(fileName, byteProgressBar);
			bytesWorker.execute();
		}
	}

	private class PauseResumeButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			// Check to see if thread is cancelled
			if (pWorker.isCancelled()) {
				// If it is, inform user
				JOptionPane.showMessageDialog(null, "Nothing is Playing");
			} else {
				// if it is not, pause/resume play
				if (SynthComponent.getPaused()) {
					SynthComponent.setPaused(false);
				} else {
					// if unpaused, pause
					SynthComponent.setPaused(true);
				}
			}
		}
	}

	private class StopButtonActionListener implements ActionListener {

        // stop the myWorker thread
		@Override
		public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            // stop thread
			// if swing workers exist, cancel them.
			if (pWorker != null) {
				if (SynthComponent.getPaused()) {
					SynthComponent.setIsCancelled(true);
					SynthComponent.setPaused(false);
				}
				pWorker.cancel(true);
			}
			if (picWorker != null) {
				picWorker.cancel(true);
			}
			if (toMidiWorker != null) {
				toMidiWorker.cancel(true);
			}
        }
	}

	private class PlayButtonActionListener implements ActionListener {
		// executes when button is pressed

        // plays the file the user specifies
		@Override
		public void actionPerformed(ActionEvent e) {
            // declare basic variables
            String fileName = fileToPlayField.getText();
            Scanner noteDurationScanner = new Scanner(durationMultiplierField.getText());
            double noteDurationMultiplier = noteDurationScanner.nextDouble();
            durationMultiplierField.getText();

            // check to see if the pWorker is running
            if (pWorker == null) {
                // if not, execute it
                pWorker = new playWorker(fileName, noteDurationMultiplier, playProgressBar);
                pWorker.execute();
            } else if (pWorker.isCancelled()) {
                // if it is running, but was canceled, restart it
                pWorker = new playWorker(fileName, noteDurationMultiplier, playProgressBar);
				SynthComponent.setIsCancelled(false);
                pWorker.execute();
            } else {
                // If it is already running and not paused or cancelled, display the
                JOptionPane.showMessageDialog(null, "Already Playing");
            }
        }
	}

    private class ToMidiButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {


            // Converts a user defined text file to a MIDI file
            String fileName = fileToPlayField.getText();
			toMidiWorker = new ToMidiWorker(fileName, midiProgressBar);
			toMidiWorker.execute();
        }
    }

    private class ToImageButtonActionListener implements ActionListener {
        // Converts a given txt file to .png image
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
			String fileName = fileToPlayField.getText();
			picWorker = new pictureWorker(fileName, imageProgressBar);
			picWorker.execute();
        }
    }

	private class WordToMidiActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String wordToConvert = wordToMidiField.getText();
			try {
				WordToMidi.Synthesizer(wordToConvert, midiProgressBar);
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (MidiUnavailableException e1) {
				e1.printStackTrace();
			}
		}
	}

    // Main Method
	public static void main(String[] args) {
		// main method to instantiate a new GIU Window
        new FilePlayer();
    }
}