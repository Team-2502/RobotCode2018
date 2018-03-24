import com.team2502.robot2018.utils.MathUtils;
import org.joml.ImmutableVector2f;
import org.junit.Test;

import static org.junit.Assert.*;

public class MathTest
{
    @Test
    public void testRotation()
    {
        ImmutableVector2f e1 = new ImmutableVector2f(1,0);
        ImmutableVector2f e2 = new ImmutableVector2f(0,1);

        ImmutableVector2f e1Rot = MathUtils.LinearAlgebra.rotate2D(e1, MathUtils.PI_F);
        System.out.println(e1Rot);
        assertTrue(MathUtils.epsilonEquals(e1Rot.distance(e2), 0));

         assertEquals(2+2,4);
        assertEquals(4-1,3);
    }
}
