import com.team2502.robot2018.Constants;
import com.team2502.robot2018.command.autonomous.ingredients.PathConfig;
import com.team2502.robot2018.pathplanning.purepursuit.PurePursuitMovementStrategy;
import com.team2502.robot2018.pathplanning.purepursuit.Waypoint;
import com.team2502.robot2018.utils.SimulatedStopwatch;
import org.joml.ImmutableVector2f;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimulatorTest
{
    //    @Test
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
        for(int i = 0; i < 1000; i++)
        {
            simulatedRobot.runMotorsVel(-5, 5);
            simulatorLocationEstimator.update();
        }
        ImmutableVector2f estimateLocation = simulatorLocationEstimator.estimateLocation();
        System.out.println("estimateLocation = " + estimateLocation);
        assertEquals(estimateLocation, new ImmutableVector2f(0, 0));
    }

    @Test
    public void testLeft()
    {
        testPath(PathConfig.Left.leftScale);
        testPath(PathConfig.Left.rightScale);
        testPath(PathConfig.Left.leftSwitch);
        testPath(PathConfig.Left.leftScaleDeepNullZone);
        testPath(PathConfig.Left.leftScaleToSwitch);
        testPath(PathConfig.Left.leftSwitchToScale);
    }


    private void testPath(List<Waypoint> pathToTest)
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
                System.out.println("Finished");
                break;
            }
            purePursuitMovementStrategy.update();
            ImmutableVector2f wheelVels = purePursuitMovementStrategy.getWheelVelocities();
            simulatedRobot.runMotorsVel(wheelVels.x, wheelVels.y);
            simulatorLocationEstimator.update();
        }
        Assert.assertTrue(isSuccess(simulatorLocationEstimator.estimateLocation(), path));
    }

    boolean isSuccess(ImmutableVector2f finalLoc, List<Waypoint> path)
    {
        float distance = finalLoc.distance(path.get(path.size() - 1).getLocation());
        System.out.println("distance = " + distance);
        return distance < 1F;
    }

}
