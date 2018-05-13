package com.team2502.ppsimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application
{
    private Stage window;
    private Scene mainScene;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Keep a reference to the window
        window = primaryStage;

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));


        primaryStage.setTitle("PP Player");

        Parent mainRoot = FXMLLoader.load(getClass().getResource("main.fxml"));


        mainScene = new Scene(mainRoot);
        // Display the window
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
