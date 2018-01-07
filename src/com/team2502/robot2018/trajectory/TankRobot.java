package com.team2502.robot2018.trajectory;

public interface TankRobot
{
    double getHeading();
    double getV_rMax();
    double getV_lMax();
    double getV_lMin();
    double getV_rMin();
    double getLateralWheelDistance();
}
