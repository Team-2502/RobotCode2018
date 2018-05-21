package com.team2502.splineviz;

import com.team2502.robot2018.pathplanning.purepursuit.SplinePathSegment;
import com.team2502.robot2018.pathplanning.purepursuit.SplineWaypoint;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.joml.ImmutableVector2f;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable
{
    public TableView<SplinePoint> tableControlPoints;

    public TableColumn<SplinePoint, ImmutableVector2f> colPosX, colPosY, colTanX, colTanY;

    public TextField txtPosX, txtPosY, txtTanX, txtTanY;

    public ObservableList<SplinePoint> waypoints = FXCollections.observableList(new ArrayList<>());

    public LineChart<Float, Float> graph;

    public XYChart.Series<Float, Float> splinePoints;

    public ObservableList<SplinePathSegment> segments = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {


        graph.setOnMouseClicked(this::addRow);

        colPosX.setCellValueFactory(
                new PropertyValueFactory<>("posX")
        );
        colPosY.setCellValueFactory(
                new PropertyValueFactory<>("posY")
        );
        colTanX.setCellValueFactory(
                new PropertyValueFactory<>("tanX")
        );
        colTanY.setCellValueFactory(
                new PropertyValueFactory<>("tanY")
        );

        tableControlPoints.setItems(waypoints);

        splinePoints = new XYChart.Series<>();

        graph.setData(FXCollections.observableArrayList(splinePoints));

        waypoints.add(new SplinePoint(0, 0, 1,  1));

    }

    public void addRow(Event e)
    {
        waypoints.add(new SplinePoint(1, 1, 1,  1));
        System.out.println(waypoints);

    }
}
