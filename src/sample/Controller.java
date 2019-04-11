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
import static sample.PixelUtils.pixelRed;

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
        for (int i = 1; i < width - 1; i+=2) {
            for (int j = 1; j < height - 1; j+=2) {
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
                FiltrareDirectionala fd = new FiltrareDirectionala();
                fd.filteringDirection(img, i, j);
                break;
            }
            case IMAGINI_MEDICAE: {
                Laplacian l = new Laplacian();
                l.medicalImag(img,i,j);
                break;
            }
            case INVERSAREA_CONSTRASTULUI: {
                InversareContrast ic = new InversareContrast();
                ic.inversareContrast(img, i, j);
                break;
            }
            default:
                break;
        }

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
