package sample;

import java.awt.image.BufferedImage;

public class PixelUtils {
    public static int pixelRed(BufferedImage img, int i, int j) {
        int p = img.getRGB(i, j);
        return (p >> 16) & 0xff;
    }

    public static int pixelBlue(BufferedImage img, int i, int j) {
        int p = img.getRGB(i, j);
        return p & 0xff;
    }

    public static int pixelGreen(BufferedImage img, int i, int j) {
        int p = img.getRGB(i, j);
        return (p >> 8) & 0xff;
    }
    public static int pixelGrayScale(BufferedImage img, int i, int j) {
        int p = img.getRGB(i, j);
        //get red
        int r = (p >> 16) & 0xff;

        //get green
        int g = (p >> 8) & 0xff;

        //get blue
        int b = p & 0xff;

        //gray scale
        int u = (r + g + b) / 3;
        int a = 255;
        r = u;
        g = u;
        b = u;
        //set the pixel value
        p = (a << 24) | (r << 16) | (g << 8) | b;
        img.setRGB(i, j, p);
        return u;
    }
}
