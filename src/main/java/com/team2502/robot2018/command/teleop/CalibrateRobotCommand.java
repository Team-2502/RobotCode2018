package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedList;
import java.util.List;

/**
 * Used for tuning the PID on the drivetrain
 */
public class CalibrateRobotCommand extends Command
{

    public static final float ROT_UNTIL_STOP = 1080F;
    double velocity = 0;
    float initAngle = 0;
    int samples = 0;
    List<Float> error2 = new LimitedQueue<>(250);


    @Override
    protected void initialize()
    {
        System.out.println("initialized");
        // We are blocking the right wheels
        initAngle = (float) Robot.NAVX.getAngle();
    }

    @Override
    protected void execute()
    {
        if(SmartDashboard.getBoolean("calibration_enabled", false))
        {
            velocity = Robot.CAL_VELOCITY;

            int actualVel = Robot.DRIVE_TRAIN.getRightRawVel();

            float dif = (float) (actualVel - velocity);

            error2.add(dif * dif);
            int size = error2.size();
            float error2Avg = 0;

            // F ... rpm = 557.6  ... 557.6 * 1/60 * 1/10 * 4096 = 3806.5493333333 ... .0.2687473379
            for(Float anError2 : error2)
            {
                error2Avg += anError2 / size;
            }

            SmartDashboard.putNumber("enc_error_2", error2Avg);
            SmartDashboard.putNumber("enc_velocity", actualVel);

            Robot.DRIVE_TRAIN.runMotorsRawVelocity((float) velocity, (float) velocity);
        }
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
        Robot.DRIVE_TRAIN.setTeleopSettings();
    }

    class LimitedQueue<E> extends LinkedList<E>
    {

        private int limit;

        public LimitedQueue(int limit)
        {
            this.limit = limit;
        }

        @Override
        public boolean add(E o)
        {
            boolean added = super.add(o);
            while(added && size() > limit)
            {
                super.remove();
            }
            return added;
        }
    }

}
