package com.team2502.guitools.splineviz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application
{
    private Scene mainScene;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
//        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));


        primaryStage.setTitle("Spline Visualizer");

        Parent mainRoot = FXMLLoader.load(getClass().getResource("main.fxml"));
//        Parent mainRoot = new AnchorPane();

        // Display the window
        mainScene = new Scene(mainRoot);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
