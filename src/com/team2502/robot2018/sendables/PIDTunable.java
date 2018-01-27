package com.team2502.robot2018.sendables;

/**
 * Created by 64009334 on 1/20/18.
 */
public interface PIDTunable
{
    double getkP();

    void setkP(double kP);

    double getkI();

    void setkI(double kI);

    double getkD();

    void setkD(double kD);

    double getkF();

    void setkF(double kF);

}
