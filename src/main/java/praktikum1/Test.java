package praktikum1;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;

public class Test {


    public static final String PICTURE = "BinaryObjects.png";

    public static void main(String[] args) {
        ImageJ myImageJ = new ImageJ(); // launch ImageJ GUI
        myImageJ.exitWhenQuitting(true); // close windows when quitting ImageJ
        String inputDir = "pictures/"; // in Windows
        String inputIma = PICTURE;
        ImagePlus image = IJ.openImage(inputDir + inputIma);
        if (image == null)
            IJ.error("Couldn't open image " + inputIma);
      //  image.show(); // display input image
      //  IJ.runPlugIn(image, "praktikum1.Color_2_Gray_Slow", ""); // run my_Plugin on input image
        IJ.runPlugIn(image, "praktikum1.Distanzfilter", ""); // run my_Plugin on input image

    }


}
