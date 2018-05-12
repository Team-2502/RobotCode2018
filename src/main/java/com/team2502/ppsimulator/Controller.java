package com.team2502.ppsimulator;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable
{
    /**
     * The blue rectangle that represents the robot
     */
    @FXML
    Rectangle robot;

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
    private final double originX;
    private final double originY;

    /**
     * By default, the {@link Controller#robot} is 2-3 pixels tall. This is much too small to learn anything.
     * This scales everything up so that everything is still proportional to each other but you can at least see it
     */
    private final double spatialScaleFactor;

    /**
     * Read the points from the CSV and put them into {@link Controller#robotTraj} and {@link Controller#waypoints} as needed
     *
     * @throws IOException In case the file handler cannot reat the file
     */
    public Controller() throws IOException
    {
        // Read the config file in the resources folder and initialize values appropriately
        FileHandler config = new FileHandler("src/main/resources/com/team2502/ppsimulator/config");
        String[] settingsFlat = config.readWholeFile().split("\n");
        Map<String, String> settings = new HashMap<>();
        for(int i1 = 0; i1 < settingsFlat.length; i1++)
        {
            String line = settingsFlat[i1];

            if(line.trim().length() > 0 && line.trim().charAt(0) != '#') // If the line is not commented out
            {
                String[] keyValPair = line.split("=");
                settings.put(keyValPair[0].trim(), keyValPair[1].trim());
            }
        }

        // Set our settings
        originX = Double.parseDouble(settings.get("originX"));
        originY = Double.parseDouble(settings.get("originY"));
        spatialScaleFactor = Double.parseDouble(settings.get("spatialScaleFactor"));


        // Load csv
        FileHandler csv = new FileHandler(settings.get("fileName"));
        String contents = csv.readWholeFile();
        String[] rows = contents.split("\n");

        // Find out how many waypoints from PathConfig there are
        int numDefinedWaypoints = Integer.valueOf(rows[0]);

        // Initialize the array for waypoints
        waypoints = new double[numDefinedWaypoints][2];

        // Remember that the waypoints begin on the row at the second index
        int startOfWaypoints = 2;

        // Knowing the total amount of rows and rows for PathConfig waypoints, we make a new array for the robot's movement
        robotTraj = new double[rows.length - startOfWaypoints - numDefinedWaypoints + 1][4];
        int i = startOfWaypoints; // 0th row has num waypoints; 1st has column headers for humans

        // Process the PathConfig waypoints
        for(; i < numDefinedWaypoints + 2; i++)
        {
            String row = rows[i];
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
        for(; i < rows.length; i++)
        {
            String row = rows[i];
            String[] data = row.split(", ");

            for(int j = 0; j < data.length; j++)
            {
                robotTraj[i - rowsForRobotMovement][j] = Double.valueOf(data[j]);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Make sure the circle always stays with the robot
        robot.xProperty().addListener((propertyX, oldX, newX) -> {
            lookahead.setCenterX(newX.doubleValue() + robot.getWidth() / 2);
        });

        robot.yProperty().addListener((propertyY, oldY, newY) -> {
            lookahead.setCenterY(newY.doubleValue() + robot.getHeight() / 2);
        });

        // Start the animation
        animateSquareKeyframe(null);
    }


    /**
     * Animate the robot following the path
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

            waypointPath.getElements().add(new LineTo(x + pathOffsetX, y + pathOffsetY));
        }

        // Draw our drive path -- where our robot actually went
        for(int i = 1; i < robotTraj.length; i++)
        {
            // Get this waypoint and the next waypoint
            double[] waypoint = robotTraj[i - 1];
            double[] nextWaypoint = robotTraj[i];

            // Calculate the angle between this and the next waypoint
            double dx = nextWaypoint[1] - waypoint[1];
            double dy = nextWaypoint[2] - waypoint[2];
            double targetAngle = Math.toDegrees(Math.atan2(-dy, dx));

            // Figure out where our robot belongs

            double x = waypoint[1] * spatialScaleFactor + originX;

            // We need this negative since positive y is downwards in JavaFX
            double y = -waypoint[2] * spatialScaleFactor + originY;

            // Get our lookahead
            double lookaheadDist = waypoint[3] * spatialScaleFactor;

            // Put all our keyvalues (robot pos, robot angle, lookahead) in a keyframe
            keyFrames.add(new KeyFrame(Duration.seconds(waypoint[0]), new KeyValue(robot.xProperty(), x),
                                       new KeyValue(robot.yProperty(), y),
                                       new KeyValue(robot.rotateProperty(), targetAngle),
                                       new KeyValue(lookahead.radiusProperty(), lookaheadDist)
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
