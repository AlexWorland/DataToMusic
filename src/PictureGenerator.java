/**
 * Author: Alex Worland
 * Date: 2/29/16
 * Description: CS111 Project 2
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
public class PictureGenerator {

    public PictureGenerator(String fileName, JProgressBar progressBar) {
        File file = new File(fileName);
        ArrayList<Integer> noteList = new ArrayList();
        ArrayList<Integer> intensityList = new ArrayList();
        ArrayList<Integer> durationList = new ArrayList();

        try{
            Scanner fileScan = new Scanner(file);
            // default is note, intensity, duration
            for (int i = 0; fileScan.hasNext(); i++) {
                if (fileScan.hasNext()) {
                    noteList.add(i, fileScan.nextInt() + 127);
                }

                if (fileScan.hasNext()) {
                    intensityList.add(i, fileScan.nextInt() + 127);
                }

                if (fileScan.hasNext()) {
                    durationList.add(i, fileScan.nextInt() + 127);
                }
            }
            fileScan.close();
        } catch (NoSuchElementException e) {
            // optionpane window
            JOptionPane.showMessageDialog(null, "Error! NoSuchElement exception!");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // optionpane window
            JOptionPane.showMessageDialog(null, "Error! File not found!");
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            JOptionPane.showMessageDialog(null, "Sorry! The Java Virtual Machine is out of memory." +
                    "\n" + e.getMessage());
            e.printStackTrace();
        }

        // Create new image that's dimensions are the square root of the length of the arraylist
        BufferedImage image = new BufferedImage((int)Math.sqrt(noteList.size()), (int)Math.sqrt(noteList.size()),
                BufferedImage.TYPE_INT_RGB);

        // loop to assign byte data to RGB color data
        int start = 0;
        int end = (int)Math.sqrt(noteList.size());
        int loopLength = (int)Math.sqrt(noteList.size());

        for (int q = 0; q < loopLength; q++) {
            for (int i = start, e = 0; i < end; i++, e++) {
                int r = noteList.get(i); // red component 0...255
                int g = intensityList.get(i); // green component 0...255
                int b = durationList.get(i); // blue component 0...255
                int col = (r << 16) | (g << 8) | b;
                image.setRGB(e, q, col);
            }
            start = end;
            end = end + loopLength;
            progressBar.setValue(100 * (q+1)/loopLength);
        }

        try {
            // retrieve image
            File outputFile = new File(fileName + ".png");
            ImageIO.write(image, "png", outputFile);
            ImageIcon icon = new ImageIcon(fileName + ".png");
            JOptionPane.showMessageDialog(null, "Image Generated", null, 0, icon);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error! IOException!");
            e.printStackTrace();
        }


    }
}
