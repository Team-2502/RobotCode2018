package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.OI;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class ActiveIntakeTeleopCommand extends Command
{
    private double speed;
    private float BIAS_TOLERANCE = 0.0F;

    /**
     *
     * @param speed If positive ==> intake... if negative ==> shoot
     */
    public ActiveIntakeTeleopCommand(double speed)
    {
        requires(Robot.ACTIVE_INTAKE);
        this.speed = speed;
    }

    @Override
    protected void execute()
    {
        float z = (float) OI.JOYSTICK_FUNCTION.getZ()/2F + 0.5F; // did bounds wrong lol, converts bounds to [0, 1]


        // at z = 0 ... running (-1,1)
        // at z = 0.5 ... running (-1,-1)
        // at z = 1 ... running (1,-1)
        // z = 0 is rotated left ... 0.5 is no bias ... 1 is right

        // f(0.5) = 1, f(0) = -1 ... m = 2/0.5 = 4 ... y+1 = 4x ... y = 4x - 1
        if(z < 0.5F - BIAS_TOLERANCE) // going to left
        {
            float biasLeftMultiplier = 4F*z - 1F;
            double left = speed * biasLeftMultiplier;
            Robot.ACTIVE_INTAKE.runIntakeIndependent(left, speed);
            System.out.printf("run intake L: (%.2f, %.2f) z: %.2f\n",left,speed,z);
        }
        else //if(z >= 0.5F + BIAS_TOLERANCE) // going to right
        {
            float biasRightMultiplier = 4F*(1-z) - 1F;
            double right = speed * biasRightMultiplier;
            Robot.ACTIVE_INTAKE.runIntakeIndependent(speed, right);
            System.out.printf("run intake R: (%.2f, %.2f) z: %.2f\n",speed,right,z);
        }
//        else
//        {
//            Robot.ACTIVE_INTAKE.runIntake(speed);
//        }
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
        Robot.ACTIVE_INTAKE.stopIntake();
    }
}
