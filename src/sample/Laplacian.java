package sample;

import java.awt.image.BufferedImage;

import static java.lang.Math.abs;
import static sample.PixelUtils.pixelBlue;
import static sample.PixelUtils.pixelGreen;
import static sample.PixelUtils.pixelRed;

public class Laplacian {
    public void medicalImag(BufferedImage img, BufferedImage imgCopy, int i, int j) {
        int r = this.maskRed(imgCopy, i, j);
        int g = this.maskGreen(imgCopy, i, j);
        int b = this.maskBlue(imgCopy, i, j);
        int val = (255 << 24) | (r << 16) | (g << 8) | b;
        img.setRGB(i, j, val);

    }

    private int maskRed(BufferedImage img, int i, int j) {
        int[][] mask = new int[3][3];
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                mask[m][n] = -pixelRed(img, i + m - 1, j + n - 1);
            }
        }
        mask[1][1] *= -9;
        int suma = 0;
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                suma += mask[m][n];
            }
        }
//        suma /= 9;
        if (suma < 0) {
            return 0;
        }
        if (suma > 255) {
            return 255;
        }
        return suma;
    }

    private int maskGreen(BufferedImage img, int i, int j) {
        int[][] mask = new int[3][3];
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                mask[m][n] = -pixelGreen(img, i + m - 1, j + n - 1);
            }
        }
        mask[1][1] *= -9;
        int suma = 0;
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                suma += mask[m][n];
            }
        }
//        suma /= 9;
        if (suma < 0) {
            return 0;
        }
        if (suma > 255) {
            return 255;
        }
        return suma;
    }

    private int maskBlue(BufferedImage img, int i, int j) {
        int[][] mask = new int[3][3];
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                mask[m][n] = -pixelBlue(img, i + m - 1, j + n - 1);
            }
        }
        mask[1][1] *= -9;
        int suma = 0;
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                suma += mask[m][n];
            }
        }
//        suma /= 9;
        if (suma < 0) {
            return 0;
        }
        if (suma > 255) {
            return 255;
        }
        return suma;

    }
}
