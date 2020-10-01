/**
 * Author: Alex Worland
 * Date: 2/26/16
 * Description: CS111 Project 2
 */
import javax.swing.*;
import java.io.*;
public class Converter { // Class to convert a file into byte data and write data to a txt file

    public static void getBytes(String fileName, JProgressBar progressBar) throws IOException {

        try {
            // Open file and write byte data to fileData byte array
            File file = new File(fileName);
            byte[] fileData = new byte[Math.abs((int) file.length())];
            FileInputStream in = new FileInputStream(file);
            in.read(fileData);
            in.close();
            writeFile(fileData, fileName, progressBar);
        } catch (FileNotFoundException e) {
            // In case file not found
            JOptionPane.showMessageDialog(null, "File Not Found! \nDid you enter the name right?\nAND made sure it" +
                    " is in the root directory?");
            e.printStackTrace();
        }
    }

    public static void writeFile(byte[] b, String fileName, JProgressBar progressBar) throws FileNotFoundException{
        // write byte array read from file, to .txt file
        PrintWriter writer = new PrintWriter(fileName + ".txt");
        int fileLength = b.length;
        for (int i = 0; i < fileLength; i++) {
            // Catch the event of byte value = -128. Since there is no midi note 128, would throw a midi error.
            if (Math.abs(b[i]) == 128) {
                writer.println(Math.abs(b[i]) - 1);
            } else {
                writer.println((b[i]));
            }
            // Update the progress bar
            progressBar.setValue(100 * (i+1)/b.length);
        }
        writer.close();
    }
}