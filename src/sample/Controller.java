package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller {
    @FXML
    ImageView input;
    @FXML
    ImageView output;
    @FXML
    ComboBox transformation;

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
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.pixelTransform(img, i, j, this.selectedTransformationType);
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

    private void pixelTransform(BufferedImage img, int posX, int posY, TransformationType transformationType) {
        switch (transformationType) {
            case GRAY_SCALE: {
                this.grayScale(img, posX, posY);
                break;
            }
            case WHITENING: {
                this.whitening(img, posX, posY);
                break;
            }
            case BLACK_N_WHITE: {
                this.blacknwhite(img, posX, posY);
                break;
            }
            case BLACKING: {
                this.blacking(img, posX, posY);
            }
            default:
                break;
        }
    }

    private void blacking(BufferedImage img, int posX, int posY) {
        int p = img.getRGB(posX, posY);
        //get red
        int r = (p >> 16) & 0xff;

        //get green
        int g = (p >> 8) & 0xff;

        //get blue
        int b = p & 0xff;
        int a = 255;
        double luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b;
        if (luminance < 28) {
            r += 200;
            g = 0;
            b = 0;
        } else {
            r = 255;
            g = 255;
            b = 255;
        }
        //set the pixel value
        p = (a << 24) | (r << 16) | (g << 8) | b;
        img.setRGB(posX, posY, p);
    }

    private void whitening(BufferedImage img, int posX, int posY) {
        int p = img.getRGB(posX, posY);
        //get red
        int r = (p >> 16) & 0xff;

        //get green
        int g = (p >> 8) & 0xff;

        //get blue
        int b = p & 0xff;
        int a = 255;
        double luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b;
        if (luminance < 128) {
            r = 0;
            g = 0;
            b = 0;
        }
        //set the pixel value
        p = (a << 24) | (r << 16) | (g << 8) | b;
        img.setRGB(posX, posY, p);
    }

    private void blacknwhite(BufferedImage img, int posX, int posY) {
        int p = img.getRGB(posX, posY);
        //get red
        int r = (p >> 16) & 0xff;

        //get green
        int g = (p >> 8) & 0xff;

        //get blue
        int b = p & 0xff;
        int a = 255;
        double luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b;
        if (luminance < 128) {
            r = 0;
            g = 0;
            b = 0;
        } else {
            r = 255;
            g = 255;
            b = 255;
        }
        //set the pixel value
        p = (a << 24) | (r << 16) | (g << 8) | b;
        img.setRGB(posX, posY, p);
    }

    private void grayScale(BufferedImage img, int posX, int posY) {
        int p = img.getRGB(posX, posY);
        //get red
        int r = (p >> 16) & 0xff;

        //get green
        int g = (p >> 8) & 0xff;

        //get blue
        int b = p & 0xff;
        int a = 255;
        int medie = (r + g + b) / 3;
        r = medie;
        g = medie;
        b = medie;
        //set the pixel value
        p = (a << 24) | (r << 16) | (g << 8) | b;
        img.setRGB(posX, posY, p);
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
}
