package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.pow;

public class Controller {
    @FXML
    ImageView input;
    @FXML
    ImageView output;
    @FXML
    ComboBox transformation;
    @FXML
    Slider aSlider;
    @FXML
    Slider bSlider;
    @FXML
    Slider gammaSlider;
    @FXML
    Label aValue;
    @FXML
    Label bValue;
    @FXML
    Label gammaValue;

    TransformationType selectedTransformationType;

    ObservableList<TransformationType> transformations;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    String inputFileUrl = "src/in.png";
    Stage stage;

    @FXML
    public void initialize() {
        this.transformations = FXCollections.observableArrayList();
        this.transformations.addAll(Arrays.asList(TransformationType.values()));
        this.transformation.setItems(this.transformations);
        this.transformation.setValue(this.transformations.get(0));
        this.selectedTransformationType = this.transformations.get(0);
    }

    public void convert(ActionEvent actionEvent) {
        BufferedImage img = null;
        File f = null;
        try {

            f = new File("in.png");
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e);
        }
        int width = img.getWidth();
        int height = img.getHeight();
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                this.areaTransform(img, i, j, this.selectedTransformationType);
            }
        }

        //write image
        try {
            f = new File("./out.png");
            ImageIO.write(img, "png", f);
        } catch (IOException e) {
            System.out.println(e);
        }
        this.output.setImage(new Image("file:out.png"));
    }

    /***
     *
     * @param img
     * @param i position x in image of the centered element in the mask
     * @param j position x in image of the centered element in the mask
     * @param selectedTransformationType
     */
    private void areaTransform(BufferedImage img, int i, int j, TransformationType selectedTransformationType) {
        switch (selectedTransformationType) {
            case FILTRARE_DIRECTIONALA: {
                this.filteringDirection(img, i, j);
                break;
            }
            case IMAGINI_MEDICAE: {
                this.medicalImages(img, i, j);
                break;
            }
            case INVERSAREA_CONSTRASTULUI: {
                this.constrastInversion(img, i, j);
                break;
            }
            default:
                break;
        }

    }

    private void constrastInversion(BufferedImage img, int i, int j) {
        int[][] mask = new int[3][3];
        mask[1][1] = this.pixelGrayScale(img, i, j);

        int x = mask[0][0];
    }

    private void medicalImages(BufferedImage img, int i, int j) {
    }

    private void filteringDirection(BufferedImage img, int i, int j) {
        int r = this.maskRed(img, i, j);
        int g = this.maskGreen(img, i, j);
        int b = this.maskBlue(img, i, j);
        int val = (255 << 24) | (r << 16) | (g << 8) | b;
        img.setRGB(i, j, val);

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

    private int pixelBlue(BufferedImage img, int i, int j) {
        int p = img.getRGB(i, j);
        return p & 0xff;
    }

    private int pixelGreen(BufferedImage img, int i, int j) {
        int p = img.getRGB(i, j);
        return (p >> 8) & 0xff;
    }

    private int pixelRed(BufferedImage img, int i, int j) {
        int p = img.getRGB(i, j);
        return (p >> 16) & 0xff;
    }

    private void grayScale() {
        BufferedImage img = null;
        File f = null;
        try {

            f = new File("in.png");
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e);
        }
        int width = img.getWidth();
        int height = img.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.pixelGrayScale(img, i, j);
            }
        }

        //write image
        try {
            ImageIO.write(img, "png", f);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private int pixelGrayScale(BufferedImage img, int i, int j) {
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


    public void chooseFile() throws IOException {
        this.output.setImage(null);
        FileChooser fileChooser = new FileChooser();
        BufferedImage img = null;
        File selectedFile = fileChooser.showOpenDialog(stage);
        try {
            img = ImageIO.read(selectedFile);
        } catch (IOException e) {
            System.out.println(e);
        }
        File newInput = new File("./in.png");
        ImageIO.write(img, "png", newInput);
        // this.grayScale();
        this.input.setImage(new Image("file:in.png"));
    }

    public void selectTransformationType(ActionEvent actionEvent) {
        this.selectedTransformationType = (TransformationType) this.transformation.getValue();
    }

    public void redirect(ActionEvent actionEvent) {
        BufferedImage out = null;
        File fin = null;
        File fout = null;
        try {

            fin = new File("in.png");
            fout = new File("out.png");
            out = ImageIO.read(fout);
            ImageIO.write(out, "png", fin);

        } catch (IOException e) {
            System.out.println(e);
        }
        this.input.setImage(new Image("file:in.png"));
        this.output.setImage(null);
    }

    public void updateValueForA(MouseEvent dragEvent) {
        this.aValue.setText(String.valueOf((int) this.aSlider.getValue()));
    }

    public void updateValueForB(MouseEvent dragEvent) {
        this.bValue.setText(String.valueOf((int) this.bSlider.getValue()));
    }

    public void updateValueForGamma(MouseEvent mouseEvent) {
//        double gamma = (double) ((int) (this.gammaSlider.getValue() * 10) / 10);
        double gamma = this.gammaSlider.getValue();
        this.gammaValue.setText(String.valueOf(gamma));
    }

    public void saveFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG file (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(this.stage);

        if (file != null) {
            BufferedImage out = null;
            File fout = null;
            try {
                fout = new File("out.png");
                out = ImageIO.read(fout);
                ImageIO.write(out, "png", file);

            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
