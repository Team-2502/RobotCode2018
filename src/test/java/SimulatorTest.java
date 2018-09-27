import com.team2502.robot2018.Constants;
import com.team2502.robot2018.command.autonomous.ingredients.PathConfig;
import com.team2502.robot2018.pathplanning.purepursuit.Path;
import com.team2502.robot2018.pathplanning.purepursuit.PurePursuitMovementStrategy;
import com.team2502.robot2018.pathplanning.purepursuit.SplineWaypoint;
import com.team2502.robot2018.pathplanning.purepursuit.Waypoint;
import com.team2502.robot2018.trajectory.record.PurePursuitCSVWriter;
import com.team2502.robot2018.utils.MathUtils;
import com.team2502.robot2018.utils.SimulatedStopwatch;
import org.joml.ImmutableVector2f;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Test
    public void testSplinePathCenterRight() throws IOException
    {
        Path splinePathRight = Path.fromSplinePoints(
                new SplineWaypoint(new ImmutableVector2f(0, 0), 0, 10, 10, -10),
//                new SplineWaypoint(new ImmutableVector2f(3.5F, 4.5F), 0, 10F, 10, -10), // if this doesn't work, PP is broken or field is off.
                new SplineWaypoint(new ImmutableVector2f(3.5F, 10F), 0, 10F, 10, -10) // if this doesn't work, PP is broken or field is off.
                                                    );
        testPath(splinePathRight.getWaypoints(), 90, 15, "centerSplinePathRight");
    }

    @Test
    public void testSplinePathCenterLeft() throws IOException
    {
        Path splinePathLeft = Path.fromSplinePoints(
                new SplineWaypoint(new ImmutableVector2f(0, 0), 0, 10, 10, -10),
//                new SplineWaypoint(new ImmutableVector2f(-4.5F, 4.5F), 0, 10F, 10, -10), // if this doesn't work, PP is broken or field is off.
                new SplineWaypoint(new ImmutableVector2f(-4.5F, 10F), 0, 10F, 10, -10) // if this doesn't work, PP is broken or field is off.
                                                   );
        testPath(splinePathLeft.getWaypoints(), 90, 15, "centerSplinePathLeft");
    }


    @Test
    public void testSplinePathLeftToRightScale() throws IOException
    {
        final ImmutableVector2f lastSlopeVec = new ImmutableVector2f(MathUtils.cos(3 * MathUtils.PI_F / 4), MathUtils.sin(3 * MathUtils.PI_F / 4)).mul(10);

        final List<SplineWaypoint> mySplinePath = Arrays.asList(
                new SplineWaypoint(new ImmutableVector2f(0.000F, 0.000F), new ImmutableVector2f(0.000F, 0.000F), 0, 20, -10),
                new SplineWaypoint(new ImmutableVector2f(0.000F, 12.000F), new ImmutableVector2f(0.000F, 25.000F), 25, 20, -10),
                new SplineWaypoint(new ImmutableVector2f(15.000F, 19.500F), new ImmutableVector2f(25.000F, 0.000F), 10, 20, -5),
                new SplineWaypoint(new ImmutableVector2f(19.000F, 26.500F), new ImmutableVector2f(-8.000F, 0.000F), 2, 10, -5)
                                                               );

        Path splinePathLeft = Path.fromSplinePoints(
                mySplinePath
                                                   );


        testPath(splinePathLeft.getWaypoints(), 36, 15, "leftSplineToRight");
    }


    private void testPath(List<Waypoint> pathToTest, float desiredHeading, float desiredTime, String fileName) throws IOException
    {
        PurePursuitCSVWriter manager = null;
        try
        {
            manager = new PurePursuitCSVWriter("outPaths/" + fileName + ".csv", false);
        }
        catch(IOException e1)
        {
            try
            {
                manager = new PurePursuitCSVWriter("../../outPaths/" + fileName + ".csv", false);
            }
            catch(IOException e2)
            {
                e2.printStackTrace();
            }
        }

        try
        {
            System.out.println("start: " + pathToTest);
            List<Waypoint> path = new ArrayList<>();

            for(Waypoint waypoint : pathToTest)
            {
                // Strip commands from waypoint because involve wpilib
                path.add(new Waypoint(waypoint.getLocation(), waypoint.getMaxSpeed(), waypoint.getMaxAccel(), waypoint.getMaxDeccel()));
            }

            manager.addWaypoints(path);

            SimulatedRobot simulatedRobot = new SimulatedRobot(Constants.PurePursuit.LATERAL_WHEEL_DISTANCE_FT);
            SimulatorLocationEstimator simulatorLocationEstimator = new SimulatorLocationEstimator(simulatedRobot);
            PurePursuitMovementStrategy purePursuitMovementStrategy = new PurePursuitMovementStrategy(simulatedRobot,
                                                                                                      simulatorLocationEstimator, simulatorLocationEstimator, simulatorLocationEstimator, path, Constants.PurePursuit.LOOKAHEAD, false);

            simulatorLocationEstimator.setEstimatedLocation(path.get(0).getLocation()); // Fixes paths that do not start at (0,0)
            float dt = 0.02F;
            purePursuitMovementStrategy.setStopwatch(new SimulatedStopwatch(dt));

            Path ppPath = purePursuitMovementStrategy.getPath();
            int i = 0;


            // Check PurePursuitFrame for header rows

            for(; i < 1000; i++)
            {
                if(purePursuitMovementStrategy.isFinishedPath())
                {
                    System.out.println("Finished @ " + purePursuitMovementStrategy.getUpdateCount());
                    break;
                }
                purePursuitMovementStrategy.update();
                ImmutableVector2f wheelVels = purePursuitMovementStrategy.getWheelVelocities();

                manager.addFrame(purePursuitMovementStrategy.getFrame(i * dt));

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
        }
        finally
        {
            manager.flush();
            manager.close();
        }


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

    private void writeCSV(FileWriter fileWriter, double... vals)
    {
        StringBuilder toWrite = new StringBuilder();
        for(int i = 0; i < vals.length - 1; i++)
        {
            toWrite.append(vals[i]).append(", ");
        }
        toWrite.append(vals[vals.length - 1]).append("\n");
        try
        {
            fileWriter.append(toWrite.toString());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

}
