import org.joml.ImmutableVector2f;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimulatorTest
{
    @Test
    public void testStraight()
    {
        SimulatedRobot simulatedRobot = new SimulatedRobot(1);
        SimulatorLocationEstimator simulatorLocationEstimator = new SimulatorLocationEstimator(simulatedRobot);
        for(int i = 0; i < 1000; i++)
        {
            simulatedRobot.runMotorsVel(5,5);
            simulatorLocationEstimator.update();
        }
        ImmutableVector2f estimateLocation = simulatorLocationEstimator.estimateLocation();
        System.out.println("estimateLocation = " + estimateLocation);
        assertTrue(estimateLocation.y>0);
        assertEquals(estimateLocation.x,0,1E-6F);
    }

    @Test
    public void testRotation()
    {
        SimulatedRobot simulatedRobot = new SimulatedRobot(1);
        SimulatorLocationEstimator simulatorLocationEstimator = new SimulatorLocationEstimator(simulatedRobot);
        for(int i = 0; i < 1000; i++)
        {
            simulatedRobot.runMotorsVel(0,5);
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
            simulatedRobot.runMotorsVel(-5,5);
            simulatorLocationEstimator.update();
        }
        ImmutableVector2f estimateLocation = simulatorLocationEstimator.estimateLocation();
        System.out.println("estimateLocation = " + estimateLocation);
        assertEquals(estimateLocation,new ImmutableVector2f(0,0));
    }
}
