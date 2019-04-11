package sample;

import java.awt.image.BufferedImage;

import static java.lang.Math.abs;
import static sample.PixelUtils.pixelBlue;
import static sample.PixelUtils.pixelGreen;
import static sample.PixelUtils.pixelRed;

public class FiltrareDirectionala {
    public void filteringDirection(BufferedImage img, int i, int j) {
        int r = this.maskRed(img, i, j);
        int g = this.maskGreen(img, i, j);
        int b = this.maskBlue(img, i, j);
        int val = (255 << 24) | (r << 16) | (g << 8) | b;
        img.setRGB(i, j, val);

    }
    private int maskRed(BufferedImage img, int i, int j) {
        int[][] mask = new int[3][3];
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                mask[m][n] = pixelRed(img, i + m - 1, j + n - 1);
            }
        }
        int medieOrizontala = (mask[1][0] + mask[1][1] + mask[1][2]) / 3;
        int medieVerticala = (mask[1][1] + mask[0][1] + mask[0][2]) / 3;
        int medieDiagonalaPrincipala = (mask[0][0] + mask[1][1] + mask[2][2]) / 3;
        int medieDiagonalaSecundara = (mask[2][0] + mask[1][1] + mask[0][2]) / 3;
        int minim = -1;
        int diferentaMinima = 256;
        int valoareMijloc = mask[1][1];
        if (abs(valoareMijloc - medieDiagonalaPrincipala) < diferentaMinima) {
            diferentaMinima = abs(valoareMijloc - medieDiagonalaPrincipala);
            minim = medieDiagonalaPrincipala;
        }
        if (abs(valoareMijloc - medieDiagonalaSecundara) < diferentaMinima) {
            diferentaMinima = abs(valoareMijloc - medieDiagonalaSecundara);
            minim = medieDiagonalaSecundara;
        }
        if (abs(valoareMijloc - medieVerticala) < diferentaMinima) {
            diferentaMinima = abs(valoareMijloc - medieVerticala);
            minim = medieVerticala;
        }
        if (abs(valoareMijloc - medieOrizontala) < diferentaMinima) {
            minim = medieOrizontala;
        }
        return minim;
    }

    private int maskGreen(BufferedImage img, int i, int j) {
        int[][] mask = new int[3][3];
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                mask[m][n] = pixelGreen(img, i + m - 1, j + n - 1);
            }
        }
        int medieOrizontala = (mask[1][0] + mask[1][1] + mask[1][2]) / 3;
        int medieVerticala = (mask[1][1] + mask[0][1] + mask[0][2]) / 3;
        int medieDiagonalaPrincipala = (mask[0][0] + mask[1][1] + mask[2][2]) / 3;
        int medieDiagonalaSecundara = (mask[2][0] + mask[1][1] + mask[0][2]) / 3;
        int minim = -1;
        int diferentaMinima = 256;
        int valoareMijloc = mask[1][1];
        if (abs(valoareMijloc - medieDiagonalaPrincipala) < diferentaMinima) {
            diferentaMinima = abs(valoareMijloc - medieDiagonalaPrincipala);
            minim = medieDiagonalaPrincipala;
        }
        if (abs(valoareMijloc - medieDiagonalaSecundara) < diferentaMinima) {
            diferentaMinima = abs(valoareMijloc - medieDiagonalaSecundara);
            minim = medieDiagonalaSecundara;
        }
        if (abs(valoareMijloc - medieVerticala) < diferentaMinima) {
            diferentaMinima = abs(valoareMijloc - medieVerticala);
            minim = medieVerticala;
        }
        if (abs(valoareMijloc - medieOrizontala) < diferentaMinima) {
            minim = medieOrizontala;
        }
        return minim;
    }
    private int maskBlue(BufferedImage img, int i, int j) {
        int[][] mask = new int[3][3];
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                mask[m][n] = pixelBlue(img, i + m - 1, j + n - 1);
            }
        }
        int medieOrizontala = (mask[1][0] + mask[1][1] + mask[1][2]) / 3;
        int medieVerticala = (mask[1][1] + mask[0][1] + mask[0][2]) / 3;
        int medieDiagonalaPrincipala = (mask[0][0] + mask[1][1] + mask[2][2]) / 3;
        int medieDiagonalaSecundara = (mask[2][0] + mask[1][1] + mask[0][2]) / 3;
        int minim = -1;
        int diferentaMinima = 256;
        int valoareMijloc = mask[1][1];
        if (abs(valoareMijloc - medieDiagonalaPrincipala) < diferentaMinima) {
            diferentaMinima = abs(valoareMijloc - medieDiagonalaPrincipala);
            minim = medieDiagonalaPrincipala;
        }
        if (abs(valoareMijloc - medieDiagonalaSecundara) < diferentaMinima) {
            diferentaMinima = abs(valoareMijloc - medieDiagonalaSecundara);
            minim = medieDiagonalaSecundara;
        }
        if (abs(valoareMijloc - medieVerticala) < diferentaMinima) {
            diferentaMinima = abs(valoareMijloc - medieVerticala);
            minim = medieVerticala;
        }
        if (abs(valoareMijloc - medieOrizontala) < diferentaMinima) {
            minim = medieOrizontala;
        }
        return minim;

    }

}
