package com.team2502.ppsimulator;

import javafx.animation.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    @FXML
    Rectangle robot;

    private FileHandler csv = new FileHandler("outPaths/centerToLeftSwitch.csv");

    private double[][] robotTraj;
    private double[][] waypoints;

    @FXML
    private Path robotPath;

    @FXML
    private Path waypointPath;

    @FXML
    private Circle lookahead;


    public Controller() throws Exception
    {
        String contents = csv.readWholeFile();
        String[] rows = contents.split("\n");
        int numDefinedWaypoints = Integer.valueOf(rows[0]);
        waypoints = new double[numDefinedWaypoints][2];
        int startOfWaypoints = 2;
        robotTraj = new double[rows.length - startOfWaypoints - numDefinedWaypoints + 1][4];
        int i = startOfWaypoints; // 0th row has num waypoints; 1st has column headers for humans
        for(; i < numDefinedWaypoints + 2; i++)
        {
            String row = rows[i];
            String[] data = row.split(", ");

            for(int j = 0; j < data.length; j++)
            {
                waypoints[i - startOfWaypoints][j] = Double.valueOf(data[j]);
            }
        }


        int rowsForWaypoints = startOfWaypoints + numDefinedWaypoints;
        i++;

        for(; i < rows.length; i++)
        {
            String row = rows[i];
            String[] data = row.split(", ");

            for(int j = 0; j < data.length; j++)
            {
                robotTraj[i - rowsForWaypoints][j] = Double.valueOf(data[j]);
            }
        }

//        printWaypointsNicely(robotTraj);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        robot.xProperty().addListener((propertyX, oldX, newX) -> {
            lookahead.setCenterX(newX.doubleValue() + robot.getWidth() / 2);
        });

        robot.yProperty().addListener((propertyY, oldY, newY) -> {
            lookahead.setCenterY(newY.doubleValue() + robot.getHeight() / 2);
        });

        animateSquareKeyframe(null);
    }


    @FXML
    private void animateSquareKeyframe(Event event)
    {
        List<KeyFrame> keyFrames = new ArrayList<>();

        double spatialScaleFactor = 10;
        double h = 100;
        double k = 400;


        robot.setWidth(robot.getWidth() * spatialScaleFactor);
        robot.setHeight(robot.getHeight() * spatialScaleFactor);

        double pathOffsetX = robot.getWidth() / 2;
        double pathOffsetY = robot.getHeight() / 2;
        keyFrames.add(new KeyFrame(Duration.ZERO, new KeyValue(robot.xProperty(), h),
                                   new KeyValue(robot.yProperty(), k)));
        MoveTo initialOffset = new MoveTo(h + pathOffsetX, k + pathOffsetY);

        robotPath.getElements().add(initialOffset);
        waypointPath.getElements().add(initialOffset);

        for(double[] waypoint : waypoints)
        {
            double y = waypoint[0] * spatialScaleFactor + h;
            double x = -waypoint[1] * spatialScaleFactor + k;

            waypointPath.getElements().add(new LineTo(y + pathOffsetX, x + pathOffsetY));
        }
        for(int i = 1; i < robotTraj.length; i++)
        {
            double[] waypoint = robotTraj[i - 1];
            double[] nextWaypoint = robotTraj[i];

            double dx = nextWaypoint[1] - waypoint[1];
            double dy = nextWaypoint[2] - waypoint[2];

            double targetAngle = Math.toDegrees(Math.atan2(-dy, dx));

            double y = waypoint[1] * spatialScaleFactor + h;
            double x = -waypoint[2] * spatialScaleFactor + k;

            double lookaheadDist = waypoint[3] * spatialScaleFactor;

//            System.out.println(x + ", " + y);
            keyFrames.add(new KeyFrame(Duration.seconds(waypoint[0]), new KeyValue(robot.xProperty(), y),
                                       new KeyValue(robot.yProperty(), x),
                                       new KeyValue(robot.rotateProperty(), targetAngle),
                                       new KeyValue(lookahead.radiusProperty(), lookaheadDist)
            ));

            robotPath.getElements().add(new LineTo(y + pathOffsetX, x + pathOffsetY));
        }

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);

        keyFrames.forEach((KeyFrame kf) -> timeline.getKeyFrames().add(kf));

        timeline.play();
    }


    private static void printWaypointsNicely(double[][] waypoints)
    {
        System.out.println("time, x, y");
        for(double[] row : waypoints)
        {
//            System.out.println(row[0] + ", " + row[1] + ", " + row[2]);
        }
    }
}
