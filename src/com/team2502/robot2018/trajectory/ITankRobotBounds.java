package com.team2502.robot2018.trajectory;

public interface ITankRobotBounds
{

    float getV_rMax();

    float getA_rMax();

    float getV_lMax();

    float getA_lMax();

    float getV_lMin();

    float getA_lMin();

    float getV_rMin();

    float getA_rMin();

    float getLateralWheelDistance();

    default ITankRobotBounds getInverted()
    {
        return new ITankRobotBounds()
        {
            @Override
            public float getV_rMax()
            {
                return -ITankRobotBounds.this.getV_lMin();
            }

            @Override
            public float getA_rMax()
            {
                return -ITankRobotBounds.this.getA_lMin();
            }

            @Override
            public float getV_lMax()
            {
                return -ITankRobotBounds.this.getV_rMin();
            }

            @Override
            public float getA_lMax()
            {
                return -ITankRobotBounds.this.getA_rMin();
            }

            @Override
            public float getV_lMin()
            {
                return -ITankRobotBounds.this.getV_rMax();
            }

            @Override
            public float getA_lMin()
            {
                return ITankRobotBounds.this.getA_rMax();
            }

            @Override
            public float getV_rMin()
            {
                return -ITankRobotBounds.this.getV_lMax();
            }

            @Override
            public float getA_rMin()
            {
                return -ITankRobotBounds.this.getA_lMax();
            }

            @Override
            public float getLateralWheelDistance()
            {
                return ITankRobotBounds.this.getLateralWheelDistance();
            }
        };
    }
}
