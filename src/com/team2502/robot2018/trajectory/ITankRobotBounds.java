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
}
