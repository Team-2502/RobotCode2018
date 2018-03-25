import com.team2502.robot2018.utils.MathUtils;
import org.joml.ImmutableVector2f;
import org.junit.Assert;
import org.junit.Test;

public class MathTest
{

    private ImmutableVector2f e1 = new ImmutableVector2f(1, 0);

    @Test
    public void testRotation90()
    {
        ImmutableVector2f rotated90 = MathUtils.LinearAlgebra.rotate2D(e1, MathUtils.PI_F/2);

        Assert.assertEquals(0,rotated90.x, 0.001);
        Assert.assertEquals(1,rotated90.y, 0.001);
    }

    @Test
    public void testRotation720()
    {
        ImmutableVector2f rotated720 = MathUtils.LinearAlgebra.rotate2D(e1, MathUtils.PI_F*2);

        Assert.assertEquals(1,rotated720.x, 0.001);
        Assert.assertEquals(0,rotated720.y, 0.001);
    }

    @Test
    public void testPosRotationCoordinateTransform()
    {
        ImmutableVector2f robotLocation = new ImmutableVector2f(1,1);
        float robotHeading = 7F*MathUtils.PI_F/4;
        ImmutableVector2f absoluteCoord = new ImmutableVector2f(2,2);

        float distance = robotLocation.distance(absoluteCoord);

        ImmutableVector2f relativeCoord = MathUtils.LinearAlgebra.absoluteToRelativeCoord(absoluteCoord, robotLocation, robotHeading);

        Assert.assertEquals(0,relativeCoord.x, 0.001);
        Assert.assertEquals(distance,relativeCoord.y, 0.001);
    }

    @Test
    public void testNegRotationCoordinateTransform()
    {
        ImmutableVector2f robotLocation = new ImmutableVector2f(1,1);
        float robotHeading = -MathUtils.PI_F/4;
        ImmutableVector2f absoluteCoord = new ImmutableVector2f(2,2);

        float distance = robotLocation.distance(absoluteCoord);

        ImmutableVector2f relativeCoord = MathUtils.LinearAlgebra.absoluteToRelativeCoord(absoluteCoord, robotLocation, robotHeading);

        Assert.assertEquals(0,relativeCoord.x, 0.001);
        Assert.assertEquals(distance,relativeCoord.y, 0.001);
    }

    @Test
    public void testAbsoluteDPos45()
    {
        ImmutableVector2f dPos = MathUtils.Kinematics.getAbsoluteDPosLine(1, 1, 0.1F, (float) (Math.PI / 4F));

        Assert.assertEquals(Math.sqrt(1/2F),dPos.x, 0.001);
        Assert.assertEquals(Math.sqrt(1/2F),dPos.y, 0.001);
    }

}
