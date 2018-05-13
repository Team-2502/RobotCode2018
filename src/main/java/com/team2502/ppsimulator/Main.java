package com.team2502.ppsimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application
{
    private Stage window;
    private Scene mainScene;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // Keep a reference to the window
        window = primaryStage;

        com.apple.eawt.Application application = com.apple.eawt.Application.getApplication();
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png"));
        application.setDockIconImage(image);

        primaryStage.setTitle("PP Player");

        Parent mainRoot = FXMLLoader.load(getClass().getResource("main.fxml"));


        mainScene = new Scene(mainRoot);
        // Display the window
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
