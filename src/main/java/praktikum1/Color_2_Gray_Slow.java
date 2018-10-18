package praktikum1;

import ij.*; // for ImagePlus
import ij.process.*; // for ImageProcessor
import ij.plugin.filter.PlugInFilter; // for PlugInFilter

public class Color_2_Gray_Slow implements PlugInFilter {
    public int setup(String arg, ImagePlus imp) {
        return DOES_RGB + NO_UNDO + NO_CHANGES;
    }

    public void run(ImageProcessor ip) {
        ImageProcessor ip_gray = new ByteProcessor(ip.getWidth(), ip.getHeight());
        for (int row = 0; row < ip.getHeight(); row++)
            for (int col = 0; col < ip.getWidth(); col++) {
                int r = (ip.getPixel(col, row) & 0xff0000) >> 16; // not efficient
                int g = (ip.getPixel(col, row) & 0x00ff00) >> 8; // not efficient
                int b = (ip.getPixel(col, row) & 0x0000ff); // not efficient
                int gray = (int) Math.round(0.299 * r + 0.587 * g + 0.114 * b); // not efficient
                ip_gray.putPixel(col, row, gray); // not efficient
            }
        ImagePlus grayImage = new ImagePlus("Gray Image", ip_gray);
        grayImage.show();
    }
}