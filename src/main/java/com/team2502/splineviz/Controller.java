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
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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

    public ScatterChart<Float, Float> graph;

    public XYChart.Series<Float, Float> splinePoints = new XYChart.Series<>();

    public ObservableList<SplinePathSegment> segments = FXCollections.observableArrayList();

    public Button btnAddRow;
    public Button btnClearAll;

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

        Rectangle rect = new Rectangle(0, 0);
        rect.setVisible(false);
        splinePoints.setNode(rect);

        tableControlPoints.setItems(waypoints);

        splinePoints = new XYChart.Series<>();

        graph.setData(FXCollections.observableArrayList(splinePoints));

        waypoints.add(new SplinePoint(0, 0, 1,  1));
    }

    public void addRow(Event e)
    {
        float posX = Float.valueOf(txtPosX.getCharacters().toString());
        float posY = Float.valueOf(txtPosY.getCharacters().toString());
        float tanX = Float.valueOf(txtTanX.getCharacters().toString());
        float tanY = Float.valueOf(txtTanY.getCharacters().toString());

        waypoints.add(new SplinePoint(posX, posY, tanX, tanY));
        segments.add(new SplinePathSegment(waypoints.get(waypoints.size() - 2), waypoints.get(waypoints.size() - 1), false, false, 1, 1, 1));

        splinePoints.getData().clear();
        for(SplinePathSegment segment : segments)
        {
            for(double t = 0; t < 1; t += 1E-2)
            {
                final XYChart.Data<Float, Float> toAdd = new XYChart.Data<>((float) segment.getX(t), (float) segment.getY(t));
                splinePoints.getData().add(toAdd);
            }
        }

        txtPosX.clear();
        txtPosY.clear();
        txtTanX.clear();
        txtTanY.clear();
    }

    public void clearAll(Event e)
    {
        segments.clear();
        waypoints.remove(1, waypoints.size());
        splinePoints.getData().remove(1, splinePoints.getData().size());
    }

    private static Circle getCircle(double x, double y)
    {
        return new Circle(x, y, 4, Color.CHARTREUSE);
    }
}
