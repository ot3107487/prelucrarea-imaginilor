package sample;

import java.awt.image.BufferedImage;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static sample.PixelUtils.pixelBlue;
import static sample.PixelUtils.pixelGreen;
import static sample.PixelUtils.pixelRed;

public class InversareContrast {
    public void inversareContrast(BufferedImage img,BufferedImage imgCopy, int i, int j) {
        int r = this.maskRed(imgCopy, i, j);
        int g = this.maskGreen(imgCopy, i, j);
        int b = this.maskBlue(imgCopy, i, j);
        int val = (255 << 24) | (r << 16) | (g << 8) | b;
        img.setRGB(i, j, val);

    }

    private int maskRed(BufferedImage img, int i, int j) {
        int[][] mask = new int[3][3];
        int miu = 0;
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                mask[m][n] = pixelRed(img, i + m - 1, j + n - 1);
                miu += mask[m][n];
            }
        }
        miu = miu / 9;
        int sumaRadical = 0;
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                sumaRadical += ((mask[m][n] - miu) * (mask[m][n] - miu));
            }
        }
        int sigma = (int) sqrt(sumaRadical/9);
        if(sigma == 0)
            return mask[1][1];
        return miu / sigma;
    }

    private int maskGreen(BufferedImage img, int i, int j) {
        int[][] mask = new int[3][3];
        int miu = 0;
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                mask[m][n] = pixelGreen(img, i + m - 1, j + n - 1);
                miu += mask[m][n];
            }
        }
        miu = miu / 9;
        int sumaRadical = 0;
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                sumaRadical += pow((mask[m][n] - miu),2);
            }
        }
        int sigma = (int) sqrt(sumaRadical/9);
        if(sigma == 0)
            return mask[1][1];
        return miu / sigma;
    }

    private int maskBlue(BufferedImage img, int i, int j) {
        int[][] mask = new int[3][3];
        int miu = 0;
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                mask[m][n] = pixelBlue(img, i + m - 1, j + n - 1);
                miu += mask[m][n];
            }
        }
        miu = miu / 9;
        int sumaRadical = 0;
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                sumaRadical += ((mask[m][n] - miu) * (mask[m][n] - miu));
            }
        }
        int sigma = (int) sqrt(sumaRadical/9);
        if(sigma == 0)
            return mask[1][1];
        return miu / sigma;

    }
}
