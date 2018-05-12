import com.team2502.robot2018.Constants;
import com.team2502.robot2018.command.autonomous.ingredients.PathConfig;
import com.team2502.robot2018.pathplanning.purepursuit.PurePursuitMovementStrategy;
import com.team2502.robot2018.pathplanning.purepursuit.Waypoint;
import com.team2502.robot2018.utils.MathUtils;
import com.team2502.robot2018.utils.SimulatedStopwatch;
import org.joml.ImmutableVector2f;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimulatorTest
{

    private static final int HEADING_DEGREE_TOLERANCE = 50;
    private static final int TIME_TOLERANCE = 15;

    @Test
    public void testStraight()
    {
        SimulatedRobot simulatedRobot = new SimulatedRobot(1);
        SimulatorLocationEstimator simulatorLocationEstimator = new SimulatorLocationEstimator(simulatedRobot);
        for(int i = 0; i < 1000; i++)
        {
            simulatedRobot.runMotorsVel(5, 5);
            simulatorLocationEstimator.update();
        }
        ImmutableVector2f estimateLocation = simulatorLocationEstimator.estimateLocation();
        System.out.println("estimateLocation = " + estimateLocation);
        assertTrue(estimateLocation.y > 0);
        assertEquals(estimateLocation.x, 0, 1E-6F);
    }

    @Test
    public void testRotation()
    {
        SimulatedRobot simulatedRobot = new SimulatedRobot(1);
        SimulatorLocationEstimator simulatorLocationEstimator = new SimulatorLocationEstimator(simulatedRobot);
        for(int i = 0; i < 1000; i++)
        {
            simulatedRobot.runMotorsVel(0, 5);
            simulatorLocationEstimator.update();
        }
        ImmutableVector2f estimateLocation = simulatorLocationEstimator.estimateLocation();
        System.out.println("estimateLocation = " + estimateLocation);
        assertTrue(Math.abs(estimateLocation.x) < 1.5F);
        assertTrue(Math.abs(estimateLocation.y) < 1.5F);
    }

    @Test
    public void testStandstillRotation()
    {
        SimulatedRobot simulatedRobot = new SimulatedRobot(1);
        SimulatorLocationEstimator simulatorLocationEstimator = new SimulatorLocationEstimator(simulatedRobot);
        for(int i = 0; i < 2000; i++)
        {
            simulatedRobot.runMotorsVel(-5, 5);
            simulatorLocationEstimator.update();
        }
        ImmutableVector2f estimateLocation = simulatorLocationEstimator.estimateLocation();
        System.out.println("estimateLocation = " + estimateLocation);
        assertEquals(estimateLocation, new ImmutableVector2f(0, 0));
    }

    @Test
    public void testLeftPaths() throws IOException
    {
        // CCW degrees where 0 is front of robot
        testPath(PathConfig.Left.leftScale, 332, 15, "leftToLeftScale");
        testPath(PathConfig.Left.rightScale, 39, 15, "leftToRightScale");
        testPath(PathConfig.Left.leftSwitch, 0, 15, "leftToLeftSwitch");
        testPath(PathConfig.Left.leftScaleDeepNullZone, 275, 15, "leftToLeftScaleDeepNullZone");
        testPath(PathConfig.Left.leftScaleToSwitch, 332, 15, "leftScaleToLeftSwitch");
        testPath(PathConfig.Left.leftSwitchToScale, 275, 15, "leftSwitchToLeftScale");
        testPath(PathConfig.combinePaths(PathConfig.Left.leftScale,
                                         PathConfig.Left.leftScaleToSwitch,
                                         PathConfig.Left.leftSwitchToScale), 275, 9.46F, "left2Cube");
    }

    @Test
    public void testRightPaths() throws IOException
    {
        testPath(PathConfig.Right.leftScale, 321, 7.3F, "rightToLeftScale");
        testPath(PathConfig.Right.rightScale, 28, 3.5F, "rightToRightScale");
        testPath(PathConfig.Right.rightSwitch, 0, 3.5F, "rightToRightSwitch");
        testPath(PathConfig.Right.rightScaleDeepNullZone, 90, 15, "rightToRightScaleInNullZone");
    }

    @Test
    public void testCenterPaths() throws IOException
    {
        testPath(PathConfig.Center.rightSwitch, 0, 15, "centerToRightSwitch");
        testPath(PathConfig.Center.leftSwitch, 0, 15, "centerToLeftSwitch");
    }


    private void testPath(List<Waypoint> pathToTest, float desiredHeading, float desiredTime, String fileName) throws IOException
    {
        File file = null;
        try{
            file = new File("outPaths/" + fileName + ".csv");
            file.createNewFile();
        }
        catch(IOException e1)
        {
            try
            {
                file = new File("../../outPaths/" + fileName + ".csv");
                file.createNewFile();
            }
            catch(IOException e2)
            {
                e2.printStackTrace();
            }
        }


        System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());

        FileWriter fileWriter = new FileWriter(file);

        System.out.println("start: " + pathToTest);
        List<Waypoint> path = new ArrayList<>();

        // How many waypoints are there?
        fileWriter.append(pathToTest.size() + "\n");
        fileWriter.append("x, y\n");

        for(Waypoint waypoint : pathToTest)
        {
            // Strip commands from waypoint because involve wpilib
            path.add(new Waypoint(waypoint.getLocation(), waypoint.getMaxSpeed(), waypoint.getMaxAccel(), waypoint.getMaxDeccel()));
            double x = waypoint.getLocation().x;
            double y = waypoint.getLocation().y;
            fileWriter.append(x + ", " + y + "\n");
        }


        SimulatedRobot simulatedRobot = new SimulatedRobot(Constants.PurePursuit.LATERAL_WHEEL_DISTANCE_FT);
        SimulatorLocationEstimator simulatorLocationEstimator = new SimulatorLocationEstimator(simulatedRobot);
        PurePursuitMovementStrategy purePursuitMovementStrategy = new PurePursuitMovementStrategy(simulatedRobot,
                                                                                                  simulatorLocationEstimator, simulatorLocationEstimator, simulatorLocationEstimator, path, Constants.PurePursuit.LOOKAHEAD, false);

        simulatorLocationEstimator.setEstimatedLocation(path.get(0).getLocation()); // Fixes paths that do not start at (0,0)
        float dt = 0.02F;
        purePursuitMovementStrategy.setStopwatch(new SimulatedStopwatch(dt));
        int i = 0;

//        System.out.println("time, x, y, lookahead");
        fileWriter.append("time, x, y, lookahead, heading\n");

        for(; i < 1000; i++)
        {
            if(purePursuitMovementStrategy.isFinishedPath())
            {
                System.out.println("Finished @ " + purePursuitMovementStrategy.getUpdateCount());
                break;
            }
            purePursuitMovementStrategy.update();
            ImmutableVector2f wheelVels = purePursuitMovementStrategy.getWheelVelocities();
            ImmutableVector2f usedEstimatedLocation = purePursuitMovementStrategy.getUsedEstimatedLocation();
            double usedLookahead = purePursuitMovementStrategy.getUsedLookahead();
            float usedHeading = purePursuitMovementStrategy.getUsedHeading();
//            System.out.println(i * dt + ", " + usedEstimatedLocation.x + ", " + usedEstimatedLocation.y + ", " + usedLookahead);
            fileWriter.append(i * dt + ", " + usedEstimatedLocation.x + ", " + usedEstimatedLocation.y + ", " + (float) usedLookahead + ", "+ simulatorLocationEstimator.estimateHeading()+"\n");
            simulatedRobot.runMotorsVel(wheelVels.x, wheelVels.y);
            simulatorLocationEstimator.update();
        }

        Assert.assertEquals(desiredTime, dt * i, TIME_TOLERANCE);
        float finalHeading = MathUtils.rad2Deg(simulatorLocationEstimator.estimateHeading());
        if(finalHeading < 0)
        {
            finalHeading = 360 + finalHeading;
        }
        float dHeading = Math.abs(desiredHeading - finalHeading) % 360;
        if(dHeading > 180)
        {
            dHeading = 360 - dHeading;
        }
        System.out.println("Final heading: " + finalHeading + " dHeading: " + dHeading);
        boolean success = isSuccess(simulatorLocationEstimator.estimateLocation(), path) && Math.abs(dHeading) < HEADING_DEGREE_TOLERANCE;
        Assert.assertTrue(success);

        fileWriter.flush();
        fileWriter.close();

    }

    boolean isSuccess(ImmutableVector2f finalLoc, List<Waypoint> path)
    {
        float distance = finalLoc.distance(path.get(path.size() - 1).getLocation());
        System.out.println("distance = " + distance);
        boolean success = distance < 1F;
        if(!success)
        {
            System.out.println("Not success! finalLoc: " + finalLoc);
        }
        return success;
    }

}
