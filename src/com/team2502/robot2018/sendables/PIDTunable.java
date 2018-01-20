package com.team2502.robot2018.sendables;

/**
 * Created by 64009334 on 1/20/18.
 */
public interface PIDTunable
{
    void setkP(double kP);

    void setkI(double kI);

    void setkD(double kD);

    void setkF(double kF);

//    void setiZone(double iZone);

    double getkP();

    double getkI();

    double getkD();

    double getkF();

//    double getiZone();
}
