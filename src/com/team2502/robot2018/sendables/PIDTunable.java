package com.team2502.robot2018.sendables;

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

    void setPID();

}
