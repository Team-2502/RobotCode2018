import com.team2502.robot2018.Constants;
import com.team2502.robot2018.command.autonomous.ingredients.PathConfig;
import com.team2502.robot2018.pathplanning.purepursuit.PurePursuitMovementStrategy;
import com.team2502.robot2018.pathplanning.purepursuit.Waypoint;
import com.team2502.robot2018.utils.MathUtils;
import com.team2502.robot2018.utils.SimulatedStopwatch;
import org.joml.ImmutableVector2f;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimulatorTest
{

    private static final int HEADING_DEGREE_TOLERANCE = 50;

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
    public void testLeftPaths()
    {
        // CCW degrees where 0 is front of robot
        testPath(PathConfig.Left.leftScale,332);
        testPath(PathConfig.Left.rightScale,39);
        testPath(PathConfig.Left.leftSwitch,0);
        testPath(PathConfig.Left.leftScaleDeepNullZone,275);
        testPath(PathConfig.Left.leftScaleToSwitch,332);
        testPath(PathConfig.Left.leftSwitchToScale,332);
    }

    @Test
    public void testRightPaths()
    {
        testPath(PathConfig.Right.leftScale,321);
        testPath(PathConfig.Right.rightScale,28);
        testPath(PathConfig.Right.rightSwitch,0);
        testPath(PathConfig.Right.rightScaleDeepNullZone,90);
    }

    @Test
    public void testCenterPaths()
    {
        testPath(PathConfig.Center.rightSwitch,0);
        testPath(PathConfig.Center.leftSwitch,0);
    }


    private void testPath(List<Waypoint> pathToTest, float desiredHeading)
    {
        System.out.println("start: " + pathToTest);
        List<Waypoint> path = new ArrayList<>();
        for(Waypoint waypoint : pathToTest)
        {
            // Strip commands from waypoint because involve wpilib
            path.add(new Waypoint(waypoint.getLocation(), waypoint.getMaxSpeed(), waypoint.getMaxAccel(), waypoint.getMaxDeccel()));
        }
        SimulatedRobot simulatedRobot = new SimulatedRobot(Constants.PurePursuit.LATERAL_WHEEL_DISTANCE_FT);
        SimulatorLocationEstimator simulatorLocationEstimator = new SimulatorLocationEstimator(simulatedRobot);
        PurePursuitMovementStrategy purePursuitMovementStrategy = new PurePursuitMovementStrategy(simulatedRobot,
                                                                                                  simulatorLocationEstimator, simulatorLocationEstimator, simulatorLocationEstimator, path, Constants.PurePursuit.LOOKAHEAD, false);

        simulatorLocationEstimator.setEstimatedLocation(path.get(0).getLocation()); // Fixes paths that do not start at (0,0)
        purePursuitMovementStrategy.setStopwatch(new SimulatedStopwatch(0.02F));
        for(int i = 0; i < 1000; i++)
        {
            if(purePursuitMovementStrategy.isFinishedPath())
            {
                System.out.println("Finished @ "+purePursuitMovementStrategy.getUpdateCount());
                break;
            }
            purePursuitMovementStrategy.update();
            ImmutableVector2f wheelVels = purePursuitMovementStrategy.getWheelVelocities();
            simulatedRobot.runMotorsVel(wheelVels.x, wheelVels.y);
            simulatorLocationEstimator.update();
        }

        float finalHeading = MathUtils.rad2Deg(simulatorLocationEstimator.estimateHeading());
        if(finalHeading < 0)
        {
            finalHeading = 360 + finalHeading;
        }
        float dHeading = Math.abs(desiredHeading - finalHeading) % 360;
        if(dHeading > 180)
        {
            dHeading = 360-dHeading;
        }
        System.out.println("Final heading: "+finalHeading+" dHeading: "+dHeading);
        boolean success = isSuccess(simulatorLocationEstimator.estimateLocation(), path) && Math.abs(dHeading) < HEADING_DEGREE_TOLERANCE;
        Assert.assertTrue(success);
    }

    boolean isSuccess(ImmutableVector2f finalLoc, List<Waypoint> path)
    {
        float distance = finalLoc.distance(path.get(path.size() - 1).getLocation());
        System.out.println("distance = " + distance);
        boolean success = distance < 1F;
        if(!success)
        {
            System.out.println("Not success! finalLoc: "+finalLoc);
        }
        return success;
    }

}
