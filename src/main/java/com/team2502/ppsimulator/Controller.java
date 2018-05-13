package com.team2502.ppsimulator;

import com.team2502.robot2018.utils.MathUtils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    private final ConfigManager configManager;
    @FXML
    public Circle constantCurvature;

    @FXML
    public Line constantCurvatureLine;

    @FXML
    public Line currentPathLine;
    /**
     * The blue rectangle that represents the robot
     */
    @FXML
    Rectangle robot;

    @FXML
    Circle goalPoint;

    @FXML
    AnchorPane backdrop;

    /**
     * The {@code n x 5} 2D array that represents where the robot went
     */
    private double[][] robotTraj;

    /**
     * The {@code n x 2} @D array that represents where the robot was told to go
     * <br>
     * This comes from {@link com.team2502.robot2018.command.autonomous.ingredients.PathConfig}
     */
    private double[][] waypoints;

    /**
     * The path that shows you where the robot went
     */
    @FXML
    private Path robotPath;

    /**
     * The path that shows you where the robot was told to go
     */
    @FXML
    private Path waypointPath;

    /**
     * This is the lookahead circle. It is centered on the {@link Controller#robot}'s center and its diameter
     * is the lookahead distance PP decided we should use
     */
    @FXML
    private Circle lookahead;

    /**
     * The coordinate (0, 0) is in the top left corner of the screen. Since driving forwards = up, this is bad.
     * The coordinate pair (originX, originY) dictates the absolute starting position of everything.
     * <p>
     * This way, we can make originY non zero allowing us to actually see the path
     */
    private double originX;
    private double originY;

    /**
     * By default, the {@link Controller#robot} is 2-3 pixels tall. This is much too small to learn anything.
     * This scales everything up so that everything is still proportional to each other but you can at least see it
     */
    private double spatialScaleFactor;

    /**
     * Read the points from the CSV and put them into {@link Controller#robotTraj} and {@link Controller#waypoints} as needed
     */
    public Controller()
    {
        // Read the config file in the resources folder and initialize values appropriately
        configManager = new ConfigManager("src/main/resources/com/team2502/ppsimulator/config");
        configManager.load();

        // Load csv
        try
        {
            String fileName = configManager.getString("fileName");
            System.out.println("fileName = " + fileName);
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            // Find out how many waypoints from PathConfig there are
            int numDefinedWaypoints = Integer.valueOf(lines.get(0));

            // Initialize the array for waypoints
            waypoints = new double[numDefinedWaypoints][2];

            // Remember that the waypoints begin on the row at the second index
            int startOfWaypoints = 2;

            // Knowing the total amount of rows and rows for PathConfig waypoints, we make a new array for the robot's movement
            robotTraj = new double[lines.size() - startOfWaypoints - numDefinedWaypoints + 1][11];
            int i = startOfWaypoints; // 0th row has num waypoints; 1st has column headers for humans

            // Process the PathConfig waypoints
            for(; i < numDefinedWaypoints + 2; i++)
            {
                String row = lines.get(i);
                String[] data = row.split(", ");

                for(int j = 0; j < data.length; j++)
                {
                    waypoints[i - startOfWaypoints][j] = Double.valueOf(data[j]);
                }
            }

            // Remember where the robot movement data begins
            int rowsForRobotMovement = startOfWaypoints + numDefinedWaypoints;
            i++; // Skip header row

            // Fill robotTraj
            for(; i < lines.size(); i++)
            {
                String row = lines.get(i);
                String[] data = row.split(", ");

                for(int j = 0; j < data.length; j++)
                {
                    robotTraj[i - rowsForRobotMovement][j] = Double.valueOf(data[j]);
                }
            }
        }
        catch(IOException e1)
        {
            System.out.println("Try the following: ");
            System.out.println("1. Create a directory called outPaths in the root folder of the RobotCode2018 project");
            System.out.println("2. Run the unit tests for RobotCode2018 on this computer");
            System.out.println("Then it should work.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        backdrop.heightProperty().addListener((heightProp, oldHeight, newHeight) -> {
                                                  spatialScaleFactor = newHeight.doubleValue() / 30.0156D;
                                                  System.out.println("spatialScaleFactor = " + spatialScaleFactor);
                                              }
                                             );

        backdrop.widthProperty().addListener((widthProp, oldWidth, newWidth) ->
                                             {
                                                 try
                                                 {
                                                     originX = StartPos.fromString(configManager.getString("startPos")).getXPos(newWidth.doubleValue());
                                                 }
                                                 catch(NullPointerException e)
                                                 {
                                                     System.out.println("Check the config file to ensure that the starting position is valid.");
                                                     throw e;
                                                 }
                                                 System.out.println("originX = " + originX);
                                             }
                                            );


        // Make sure the circle always stays with the robot
        robot.xProperty().addListener((propertyX, oldX, newX) -> {
            lookahead.setCenterX(newX.doubleValue() + robot.getWidth() / 2);
        });

        robot.yProperty().addListener((propertyY, oldY, newY) -> {
            lookahead.setCenterY(newY.doubleValue() + robot.getHeight() / 2);
        });

        backdrop.setOnMouseClicked(((e) -> {
            animateSquareKeyframe(e);
            backdrop.setOnMouseClicked((j) -> {});
        }));
    }


    private double getX(double ppX)
    {
        return ppX * spatialScaleFactor + originX + robot.getWidth() / 2;
    }

    private double getY(double ppY)
    {
        return -ppY * spatialScaleFactor + originY + robot.getHeight() / 2;
    }

    /**
     * Animate the robot following the path
     *
     * @param event This exists in case you want to add this as an onClickListener or something like that. Not used.
     */
    @FXML
    private void animateSquareKeyframe(Event event)
    {
        // Animation works by interpolating key values between key frames
        // We store all our keyframes in this handy dandy list
        List<KeyFrame> keyFrames = new ArrayList<>();

        // Scale our robot appropriately
        robot.setWidth(robot.getWidth() * spatialScaleFactor);
        robot.setHeight(robot.getHeight() * spatialScaleFactor);

        originY = backdrop.getPrefHeight() - robot.getHeight();


        // Add our first keyframe
        keyFrames.add(new KeyFrame(Duration.ZERO, new KeyValue(robot.xProperty(), originX),
                                   new KeyValue(robot.yProperty(), originY)));

        // Center the path on the robot
        double pathOffsetX = robot.getWidth() / 2;
        double pathOffsetY = robot.getHeight() / 2;

        MoveTo initialOffset = new MoveTo(originX + pathOffsetX, originY + pathOffsetY);

        robotPath.getElements().add(initialOffset);
        waypointPath.getElements().add(initialOffset);

        // Draw the path -- where our robot was told to go
        for(double[] waypoint : waypoints)
        {
            double x = waypoint[0] * spatialScaleFactor + originX;

            // We need this negative since positive y is downwards in JavaFX
            double y = -waypoint[1] * spatialScaleFactor + originY;

            LineTo lineTo = new LineTo(x + pathOffsetX, y + pathOffsetY);
            waypointPath.getElements().add(lineTo);
        }

        // Draw our drive path -- where our robot actually went
        for(int i = 1; i < robotTraj.length; i++)
        {
            // Get this waypoint and the next waypoint
            double[] waypoint = robotTraj[i - 1];

            double gpX = getX(waypoint[5]);
            double gpY = getY(waypoint[6]);

            double circleOnRadius = Math.abs(waypoint[7] * spatialScaleFactor);
            double circleOnX = getX(waypoint[8]);
            double circleOnY = getY(waypoint[9]);

            int pathOnI = (int) waypoint[10];

            double lineSegmentXI = getX(waypoints[pathOnI][0]);
            double lineSegmentYI  = getY(waypoints[pathOnI][1]);
            double lineSegmentXF = getX(waypoints[pathOnI+1][0]);
            double lineSegmentYF  = getY(waypoints[pathOnI+1][1]);


            double targetAngle = 90 - waypoint[4] * 180 / Math.PI;

            // Figure out where our robot belongs

            double x = waypoint[1] * spatialScaleFactor + originX;

            // We need this negative since positive y is downwards in JavaFX
            double y = -waypoint[2] * spatialScaleFactor + originY;

            // Get our lookahead
            double lookaheadDist = waypoint[3] * spatialScaleFactor;

            // Put all our keyvalues (robot pos, robot angle, lookahead) in a keyframe
            boolean useLine = circleOnRadius > 10000;
            double lineStartX;
            double lineStartY;
            double lineEndX;
            double lineEndY;

            double robotCenterX = x + robot.getWidth() / 2;
            double robotCenterY = y + robot.getHeight() / 2;
            double lineDX = robotCenterX - gpX;
            double lineDY = robotCenterY - gpY;
            if(lineDX != 0)
            {
                double slope = lineDY / lineDX;
                MathUtils.Function line = (inp) -> slope * (inp - robotCenterX) + robotCenterY; // pointslope
                lineStartX = 0;
                lineStartY = line.get(lineStartX);
                lineEndX = backdrop.getWidth();
                lineEndY = line.get(lineEndX);
            }
            else
            {
                lineStartX = robotCenterX;
                lineStartY = 0;
                lineEndX = robotCenterX;
                lineEndY = backdrop.getHeight();
            }
            keyFrames.add(new KeyFrame(Duration.seconds(waypoint[0]),
                                       // Robot position
                                       new KeyValue(robot.xProperty(), x),
                                       new KeyValue(robot.yProperty(), y),
                                       new KeyValue(robot.rotateProperty(), targetAngle),

                                       // Lookahead radius
                                       new KeyValue(lookahead.radiusProperty(), lookaheadDist),

                                       // Goalpoint position
                                       new KeyValue(goalPoint.centerXProperty(), gpX),
                                       new KeyValue(goalPoint.centerYProperty(), gpY),

                                       // Curvature pos
                                       new KeyValue(constantCurvature.centerXProperty(), circleOnX),
                                       new KeyValue(constantCurvature.centerYProperty(), circleOnY),
                                       new KeyValue(constantCurvature.radiusProperty(), circleOnRadius),

                                       // Whether to use the circle or the line
                                       new KeyValue(constantCurvature.visibleProperty(), !useLine),
                                       new KeyValue(constantCurvatureLine.visibleProperty(), useLine),

                                       // Line position
                                       new KeyValue(constantCurvatureLine.startXProperty(), lineStartX),
                                       new KeyValue(constantCurvatureLine.startYProperty(), lineStartY),
                                       new KeyValue(constantCurvatureLine.endXProperty(), lineEndX),
                                       new KeyValue(constantCurvatureLine.endYProperty(), lineEndY),

                                       new KeyValue(currentPathLine.startXProperty(),lineSegmentXI),
                                       new KeyValue(currentPathLine.startYProperty(),lineSegmentYI),
                                       new KeyValue(currentPathLine.endXProperty(),lineSegmentXF),
                                       new KeyValue(currentPathLine.endYProperty(),lineSegmentYF)
            ));

            // Add our position information to the translucent grey path that shows where our robot went
            robotPath.getElements().add(new LineTo(x + pathOffsetX, y + pathOffsetY));
        }

        // Create the animation
        final Timeline timeline = new Timeline();

        // Loop it forever
        timeline.setCycleCount(Timeline.INDEFINITE);

        // When the animation ends, the robot teleports from the end to the beginning instead of driving backwards
        timeline.setAutoReverse(false);

        // Add our keyframes to the animation
        keyFrames.forEach((KeyFrame kf) -> timeline.getKeyFrames().add(kf));

        timeline.setRate(configManager.getDouble("rate"));
        // Play it
        timeline.play();
    }


    private static void printWaypointsNicely(double[][] waypoints)
    {
        System.out.println("time, x, y");
        for(double[] row : waypoints)
        {
            System.out.println(row[0] + ", " + row[1] + ", " + row[2]);
        }
    }
}

enum StartPos
{
    LEFT(33D / 443D),
    CENTER(206D / 443D),
    RIGHT(358D / 443D);

    private final double proportion;

    StartPos(double proportion)
    {
        this.proportion = proportion;
    }

    public double getXPos(double windowWidth)
    {
        return windowWidth * proportion;
    }

    /**
     * Turn a string into a {@link StartPos}
     *
     * @param str A string (like "center" or "banana")
     * @return An instance of startpos if possible (e.g {@link StartPos#CENTER}) or null if not possible (e.g banana becomes null)
     */
    public static StartPos fromString(String str)
    {
        if(str.trim().equalsIgnoreCase("left"))
        {
            return LEFT;
        }
        else if(str.trim().equalsIgnoreCase("right"))
        {
            return RIGHT;
        }
        else if(str.trim().equalsIgnoreCase("center"))
        {
            return CENTER;
        }
        return null;
    }
}
