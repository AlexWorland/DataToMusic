/**
 * Author: Alex Worland
 * Date: 2/26/16
 * Description: CS111 Project 2
 */
import javax.swing.*;

public class pictureWorker extends SwingWorker<Void, String> {

    private String fileName;
    private JProgressBar progressBar;

    public pictureWorker(String fileName, JProgressBar progressBar) {
        this.fileName = fileName;
        this.progressBar = progressBar;
    }

    @Override
    protected Void doInBackground() throws Exception {
        // TODO Auto-generated method stub
        // Start
        new PictureGenerator(fileName, progressBar);
        return null;
    }

    protected void done() {
        progressBar.setValue(0);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
