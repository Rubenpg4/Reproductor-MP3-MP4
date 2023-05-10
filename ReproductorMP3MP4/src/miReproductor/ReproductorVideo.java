package miReproductor;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * @author Pablo Valdivia
 */

public class ReproductorVideo {

    //La clase Video tiene una variable privada MediaPlayer para controlar el archivo de video.
   private MediaPlayer mediaPlayer;
   

   //El método start define la interfaz gráfica de usuario y los eventos de botón para abrir, reproducir, pausar, reiniciar y cerrar el archivo de video.
    public void start(Stage primaryStage) {
        
        //Se crean objetos Button para cada uno de los botones que se muestran en la interfaz gráfica. Se configura su estilo utilizando CSS.
        Button openButton = new Button("ABRIR VIDEO");
        Button playButton = new Button("PLAY");
        Button pauseButton = new Button("PAUSA");
        Button stopButton = new Button("REINICIAR");
        Button closeButton = new Button("CERRAR");

        openButton.setStyle("-fx-font-size: 15pt; -fx-background-color: #ff7f50; -fx-text-fill: white;");
        playButton.setStyle("-fx-font-size: 15pt; -fx-background-color: #32cd32; -fx-text-fill: white;");
        pauseButton.setStyle("-fx-font-size: 15pt; -fx-background-color: #ffd700; -fx-text-fill: black;");
        stopButton.setStyle("-fx-font-size: 15pt; -fx-background-color: #ff4500; -fx-text-fill: white;");
        closeButton.setStyle("-fx-font-size: 15pt; -fx-background-color: #dc143c; -fx-text-fill: white;");

        //Se crea un objeto HBox para contener los botones. Los botones se añaden al HBox y se establece su alineación y espaciado.
        HBox buttonBar = new HBox(openButton, playButton, pauseButton, stopButton, closeButton);
        buttonBar.setSpacing(10);
        buttonBar.setAlignment(Pos.CENTER);

        //Se crea una vista de medios MediaView para mostrar el archivo de video y se configura para ajustarse al tamaño de su contenedor.
        MediaView mediaView = new MediaView();
        mediaView.setPreserveRatio(true);

        //Se crea un objeto StackPane para contener la vista de medios y se configura su color de fondo.
        StackPane mediaContainer = new StackPane(mediaView);
        mediaContainer.setStyle("-fx-background-color: black");
        mediaContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(mediaContainer, Priority.ALWAYS);

        mediaView.fitWidthProperty().bind(mediaContainer.widthProperty());
        mediaView.fitHeightProperty().bind(mediaContainer.heightProperty());

        //Se crea un objeto Slider para el control de progreso y se configura para desactivarse inicialmente.
        Slider progressSlider = new Slider(0, 1, 0);
        progressSlider.setDisable(true);
        progressSlider.prefWidthProperty().bind(buttonBar.widthProperty());

   
        //Se crea un objeto VBox para contener el control de progreso y los botones. Se establece su alineación, espaciado y relleno.
        VBox controlBox = new VBox(progressSlider,buttonBar);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.setSpacing(13);
        controlBox.setPadding(new Insets(0, 0, 20, 0));

        //Se crea un objeto VBox para contener el contenedor de medios y el control de progreso y botones. Se establece su espaciado.
        VBox root = new VBox(mediaContainer,controlBox);
        root.setSpacing(0);
        controlBox.setStyle("-fx-background-color: black");
        
        //// Crear escena y establecer en el objeto Stage de JavaFX
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        
        

        //// Definir eventos de botón y de cierre de ventana
        openButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP4 Files", "*.mp4"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                Media media = new Media(selectedFile.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) ->
                        progressSlider.setValue(newValue.toSeconds() / media.getDuration().toSeconds()));
                progressSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (progressSlider.isValueChanging()) {
                        mediaPlayer.seek(media.getDuration().multiply(newValue.doubleValue()));
                    }
                });
                mediaPlayer.setOnReady(() -> progressSlider.setDisable(false));
                mediaView.setMediaPlayer(mediaPlayer);
            }
        });

        playButton.setOnAction(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.play();
            }
        });

        pauseButton.setOnAction(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        });

        stopButton.setOnAction(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        });

        closeButton.setOnAction(event -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            primaryStage.close();
        });
        
        primaryStage.setOnCloseRequest(event -> {
    if (mediaPlayer != null) {
        mediaPlayer.stop();
    }
});
        //Se le pone nombre a la ventana y se muestra esta
        primaryStage.setTitle("Video Player");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

