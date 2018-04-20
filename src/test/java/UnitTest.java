import com.team2502.robot2018.utils.UnitUtils;
import org.junit.Assert;
import org.junit.Test;
import sun.tools.jconsole.Plotter;

import static com.team2502.robot2018.utils.UnitUtils.Distance.FEET;
import static com.team2502.robot2018.utils.UnitUtils.Distance.METER;
import static com.team2502.robot2018.utils.UnitUtils.Rotations.DEGREES;
import static com.team2502.robot2018.utils.UnitUtils.Rotations.RADIANS;

/**
 * A class to test unit conversions
 */
public class UnitTest
{
    @Test
    public void testFeetTomMeters()
    {
        float threeFeet = 3F;
        float almostOneMeter = UnitUtils.convert(threeFeet, FEET, METER);
        System.out.println("almostOneMeter = " + almostOneMeter);
        Assert.assertEquals(0.9144, almostOneMeter, 1E-3);
    }

    @Test
    public void testRadiansToDegrees()
    {
        float rightAngle = 90F;
        float rightAngleRad = UnitUtils.convert(rightAngle, DEGREES, RADIANS);

        Assert.assertEquals(Math.PI / 2, rightAngleRad, 1E-3g);

    }
}
