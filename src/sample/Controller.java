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
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Date;

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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    String inputFileUrl = "src/in.png";
    Stage stage;

    GifSequenceWriter writer;
    ImageOutputStream outputStream;

    @FXML
    public void initialize() {
        File out = new File("./out.gif");
        try {
            out.delete();
            out.createNewFile();
            outputStream = new FileImageOutputStream(out);
            writer = new GifSequenceWriter(outputStream, 5, 700, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void convert(ActionEvent actionEvent) throws IOException {
        BufferedImage next = ImageIO.read(new File("./in.png"));
        writer.writeToSequence(next);
        this.output.setImage(new Image("file:out.gif"));
    }

    public void chooseFile() throws IOException {
        this.output.setImage(new Image("file:in.png"));
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
        writer.writeToSequence(img);
        // this.grayScale();
        this.input.setImage(new Image("file:in.png"));
    }

    public void reset(ActionEvent actionEvent) {
        this.output.setImage(null);
        try {
            outputStream.close();
            File out = new File("./out.gif");
            out.delete();
            out.createNewFile();

        } catch (IOException e) {

        }

    }


    public void saveFile(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GIF file (*.gif)", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(this.stage);
        InputStream is = null;
        OutputStream os = null;
        if (file != null) {
            File fout = null;
            try {
                fout = new File("out.gif");
                is = new FileInputStream(fout);
                os = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                is.close();
                os.close();

            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
