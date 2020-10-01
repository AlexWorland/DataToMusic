/**
 * Author: Alex Worland
 * Date: 2/26/16
 * Description: CS111 Project 2
 */
import javax.swing.*;

public class ToBytesWorker extends SwingWorker<Void, String> {

    private String fileName;
    private JProgressBar progressBar;

    public ToBytesWorker(String fileName, JProgressBar progressBar) {
        this.fileName = fileName;
        this.progressBar = progressBar;
    }

    @Override
    protected Void doInBackground() throws Exception {
        // TODO Auto-generated method stub
        // Start
        Converter.getBytes(fileName, progressBar);
        return null;
    }

    protected void done() {
        JOptionPane.showMessageDialog(null, "Task Complete.");
        progressBar.setValue(0);
    }
}
