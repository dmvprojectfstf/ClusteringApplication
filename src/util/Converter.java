/**
 *
 */
package util;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * Class permet de transformer image vers data et data vers image
 *
 * @author dmvproject
 */
public class Converter {

    /**
     * creation d'une copie d'image
     *
     * @param im image d'entrée
     * @return image
     */
    public static BufferedImage copyImage(BufferedImage im) {
        BufferedImage cp = new BufferedImage(im.getWidth(), im.getHeight(), im.getType());
        System.out.println(" Coppy d image");
        Raster wrim = im.getRaster();
        WritableRaster wrcp = cp.getRaster();
        int[] pixel = new int[3];
        for (int i = 0; i < im.getWidth(); i++) {
            for (int j = 0; j < im.getHeight(); j++) {
                wrim.getPixel(i, j, pixel);
                wrcp.setPixel(i, j, pixel);
            }
        }
        return cp;
    }

    /**
     * convertion d'image vers données
     *
     * @param im image d'entrée
     * @return image de sortie
     */
    public static ArrayList<ArrayList<Object>> imageToData(BufferedImage im) {
        ArrayList<ArrayList<Object>> list = new ArrayList<>();
        Raster wrim = im.getRaster();
        int[] pixelin = new int[3];
        for (int i = 0; i < im.getWidth(); i++) {
            for (int j = 0; j < im.getHeight(); j++) {
                list.add(new ArrayList<>());
                int f = list.size() - 1;
                wrim.getPixel(i, j, pixelin);
                list.get(f).add((double) pixelin[0]);
                list.get(f).add((double) pixelin[1]);
                list.get(f).add((double) pixelin[2]);
            }
        }
        return list;
    }

    /**
     * convertir données vers image
     *
     * @param liste données
     * @param width width
     * @param height height
     * @param type type d'image
     * @return image de sortie
     */
    public static BufferedImage dataTOImage(ArrayList<ArrayList<Object>> liste, int width, int height, int type) {
        BufferedImage image = new BufferedImage(width, height, type);
        WritableRaster wrcp = image.getRaster();
        int[] pixelin = new int[3];
        int k = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixelin[0] = ((Double) liste.get(k).get(0)).intValue();
                pixelin[1] = ((Double) liste.get(k).get(1)).intValue();
                pixelin[2] = ((Double) liste.get(k).get(2)).intValue();
                wrcp.setPixel(i, j, pixelin);
                k++;
            }
        }
        return image;
    }
}
