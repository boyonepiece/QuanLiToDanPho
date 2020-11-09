package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Fxml/sample.fxml"));
        primaryStage.setTitle("Quản lý thông tin tổ dân phố");
        Scene scene = new Scene(root, 600, 400);
        //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setX(600);
        primaryStage.setY(300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
