import com.team2502.robot2018.utils.MathUtils;
import org.joml.ImmutableVector2f;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathTest
{
    @Test
    public void testRotation()
    {
        ImmutableVector2f e1 = new ImmutableVector2f(1,0);
        ImmutableVector2f e2 = new ImmutableVector2f(0,1);

        ImmutableVector2f rotated = MathUtils.LinearAlgebra.rotate2D(e1, MathUtils.PI_F);


        assertTrue(MathUtils.epsilonEquals(rotated.x, 0),"test");
        assertTrue(MathUtils.epsilonEquals(rotated.y, 1));

         assertEquals(2+2,4);
        assertEquals(4-1,3);
    }
}
