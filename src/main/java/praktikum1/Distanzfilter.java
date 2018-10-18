package praktikum1;

import ij.ImagePlus;
import ij.gui.Line;
import ij.gui.Overlay;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;

import java.awt.*;

public class Distanzfilter implements PlugInFilter {

    public static final short H0x00 = 0x00;
    public static final short H0x01 = 0x01;
    public int setup(String arg, ImagePlus imp) {
        return DOES_8G+ DOES_16 + NO_UNDO + NO_CHANGES;

    }

    public void run(ImageProcessor ip) {


        int width = ip.getWidth();
        int height = ip.getHeight();
        ShortProcessor newPicProcess = new ShortProcessor(width, height);


        byte[] oldPicture = (byte[]) ip.getPixels();
        short[] newPicture = (short[]) newPicProcess.getPixels();




        int indexInArray=0;
        for(int row=0; row<height; row++){
            for(int col=0; col < width; col++){
                if(row==0||col==0){
                    newPicture[indexInArray] = oldPicture[indexInArray]==0?H0x00: H0x01;
                }else{
                    newPicture[indexInArray]=(short)((oldPicture[indexInArray]!=0)?0:(1+Math.min(newPicture[indexInArray-width], newPicture[indexInArray-1])));
                }
                indexInArray++;
            }
        }





        //2. Durchlauf



        //2.2 Rest des Bildes von Rechts unten nach links oben reihenweise setzen

        int index=width * height -1;
        for(int row=height-1; row >=0; row--){
            for(int col=width-1; col >=0; col--){
                if(row==height-1||col==width-1){
                    newPicture[index] = oldPicture[index]==0? H0x00 : H0x01;
                }else{
                    newPicture[indexInArray]=(short)Math.min(newPicture[indexInArray], ((oldPicture[indexInArray]!=0)?H0x00:(1+Math.min(newPicture[indexInArray+width], newPicture[indexInArray+1]))));
                }
                indexInArray--;
            }
        }





        ImagePlus grayImage = new ImagePlus("Distanzfilter", newPicProcess);
        grayImage.setDisplayRange(0, 50);
        grayImage.show();

        //#######################


        boolean[][] localMaxima = findMaxima(newPicture, width, height);
        // call sequence
        Overlay overMaxis = computeOverMaxis(localMaxima);
        showOverlay(ip, overMaxis, "maxima");


    }



    boolean[][] findMaxima(short[] newPixels, int width, int height){
        boolean[][] ret = new boolean[height][width];

        int indexInArray=0; //startpunkt ist der zweite Pixel der zweiten Zeile
        for(int row=0; row<height; row++){
            for(int col=0; col < width; col++){
                if(col==0||col==width-1||row==0||row ==height-1){

                }else{
                    ret[row][col]=(
                            newPixels[indexInArray]!=0 &&
                            newPixels[indexInArray]>=newPixels[indexInArray-1] && //linker nachbar
                            newPixels[indexInArray]>=newPixels[indexInArray+1] &&//rechter nachbar
                            newPixels[indexInArray]>=newPixels[indexInArray-width] && //oberer Nachbar
                            newPixels[indexInArray]>=newPixels[indexInArray+width] && //unterer Nachbar
                            newPixels[indexInArray]>=newPixels[indexInArray + width +1] && //
                            newPixels[indexInArray]>=newPixels[indexInArray +width-1] &&
                            newPixels[indexInArray]>=newPixels[indexInArray-width+1] &&
                            newPixels[indexInArray]>=newPixels[indexInArray-width-1]);
                }
                indexInArray++;
            }
        }
        return ret;
    }

    //**********************************************************************
    // given a 2-d boolean array, put points (= lines of length 1) into overlay where array == true.
    Overlay computeOverMaxis(boolean[][] maxis)
    {
        Overlay myOverlay = new Overlay();
        Color lineColor = Color.yellow;
        int lineWidth = 1;

        // insert a one-pixel line for each maximum
        for (int row=0; row<maxis.length; row++)
            for (int col=0; col<maxis[row].length; col++)
            {
                if (maxis[row][col] == true)
                {
                    Line ptLine = new Line(col, row, col, row);
                    ptLine.setStrokeColor(lineColor);
                    ptLine.setStrokeWidth(lineWidth);
                    myOverlay.add(ptLine);
                }
            }

        return myOverlay;
    }

    //**********************************************************************
    // show image ip with overlay
    static void showOverlay(ImageProcessor ip, Overlay myOverlay, String title)
    {
        ImagePlus impOverlay = new ImagePlus(title, ip);
        impOverlay.setOverlay(myOverlay);
        impOverlay.show();
    }



}
