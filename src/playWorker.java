/**
 * Author: Alex Worland
 * Date: 2/26/16
 * Description: CS111 Project 2
 */
import javax.swing.*;

public class playWorker extends SwingWorker<Void, String> {
	
	private String fileName;
	private double noteDurationMultiplier;
	private JProgressBar progressBar;
//	private SynthComponent synth = new SynthComponent();

	public playWorker(String fileName, double noteDurationMultiplier, JProgressBar progressBar) {
		this.fileName = fileName;
		this.noteDurationMultiplier = noteDurationMultiplier;
		this.progressBar = progressBar;
	}

	@Override
	protected Void doInBackground() throws Exception {
        // TODO Auto-generated method stub
        // Star

        SynthComponent.Synthesizer(fileName, noteDurationMultiplier, progressBar);
		return null;
	}

	protected void done() {
		JOptionPane.showMessageDialog(null, "Performance Concluded.");
		progressBar.setValue(0);
		this.cancel(true);
	}
}
