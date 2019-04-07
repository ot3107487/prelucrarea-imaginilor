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
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

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
            case NOISE_REDUCTION: {
                this.noiseReduction(img, posX, posY);
                break;
            }
            case BIT_EXTRACTION_0: {
                this.bitExtraction(img, posX, posY, 0);
                break;
            }
            case BIT_EXTRACTION_1: {
                this.bitExtraction(img, posX, posY, 1);
                break;
            }
            case GAMMA_CORRECTION: {
                this.gammaCorrection(img, posX, posY, 1);
                break;
            }
            default:
                break;
        }
    }

    private void gammaCorrection(BufferedImage img, int posX, int posY, int constant) {
        int p = img.getRGB(posX, posY);
        //get red
        int r = (p >> 16) & 0xff;

        //get green
        int g = (p >> 8) & 0xff;

        //get blue
        int b = p & 0xff;

        //gray scale
        int u = (r + g + b) / 3;
        int a = 255;
        int L = 255;
        double gamma = this.gammaSlider.getValue();
        double baza = (double) u / 255;
        u = (int) (constant * 255 * pow((double) u / 255, gamma * constant));
        r = u;
        g = u;
        b = u;
        //set the pixel value
        p = (a << 24) | (r << 16) | (g << 8) | b;
        img.setRGB(posX, posY, p);
    }

    private void bitExtraction(BufferedImage img, int posX, int posY, int order) {
        // image should be gray scale
        int p = img.getRGB(posX, posY);
        //get red
        int r = (p >> 16) & 0xff;

        //get green
        int g = (p >> 8) & 0xff;

        //get blue
        int b = p & 0xff;

        //gray scale
        int u = (r + g + b) / 3;
        int a = 255;
        int L = 255;
        while (order != 0) {
            u = u >> 1;
            order--;
        }
        u = L * (u % 2);
        r = u;
        g = u;
        b = u;
        //set the pixel value
        p = (a << 24) | (r << 16) | (g << 8) | b;
        img.setRGB(posX, posY, p);
    }

    private void noiseReduction(BufferedImage img, int posX, int posY) {
        // image should be gray scale
        int p = img.getRGB(posX, posY);
        //get red
        int r = (p >> 16) & 0xff;

        //get green
        int g = (p >> 8) & 0xff;

        //get blue
        int b = p & 0xff;

        //gray scale
        int u = (r + g + b) / 3;

        int a = 255;


        int va = (int) this.aSlider.getValue();
        int vb = (int) this.bSlider.getValue();
        int L = 255;
        if (u <= va) {
            u = 0;
        }
        if (u > va && u <= vb) {
            u = (u - va) * L / (vb - va);
        }
        if (u > vb) {
            u = L;
        }
        r = u;
        g = u;
        b = u;
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

    private void SaveFile(String content, File file) {
        try {
            FileWriter fileWriter = null;

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private static String readAllBytesJava7(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
