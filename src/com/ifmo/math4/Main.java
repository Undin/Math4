package com.ifmo.math4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main_window.fxml"));
        Scene scene = new Scene(root);
        ImageView pdeImage = (ImageView) scene.lookup("#pde_image");
        Image image = new Image(new File("PDE.png").toURI().toString());
        pdeImage.setImage(image);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("PDE");
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
