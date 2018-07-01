package com.team2502.robot2018.command.autonomous.groups;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team2502.ezauton.actuators.IVelocityMotor;
import com.team2502.ezauton.command.ICommand;
import com.team2502.ezauton.command.PPCommand;
import com.team2502.ezauton.localization.TankRobotEncoderRotationEstimator;
import com.team2502.ezauton.localization.sensors.EncoderWheel;
import com.team2502.ezauton.localization.sensors.Encoders;
import com.team2502.ezauton.localization.sensors.IEncoder;
import com.team2502.ezauton.pathplanning.PP_PathGenerator;
import com.team2502.ezauton.pathplanning.Path;
import com.team2502.ezauton.pathplanning.purepursuit.ILookahead;
import com.team2502.ezauton.pathplanning.purepursuit.LookaheadBounds;
import com.team2502.ezauton.pathplanning.purepursuit.PPWaypoint;
import com.team2502.ezauton.pathplanning.purepursuit.PurePursuitMovementStrategy;
import com.team2502.ezauton.robot.ITankRobotConstants;
import com.team2502.ezauton.robot.implemented.TankRobotTransLocDriveable;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.FastRotateCommand;
import com.team2502.robot2018.command.autonomous.ingredients.PurePursuitCommand;
import com.team2502.robot2018.pathplanning.srxprofiling.SRXProfilingCommand;
import com.team2502.robot2018.pathplanning.srxprofiling.TrajConfig;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

import static com.team2502.robot2018.Constants.SRXProfiling.NO_COMMANDS;

/**
 * Should be used for all testing to make sure that we don't test something
 * by taking out working code and forget to change it back
 */
public class TestCommandGroup extends CommandGroup
{
    public TestCommandGroup()
    {
        testEzAuton();
    }

    private void testMotionProfiling()
    {
        addSequential(new SRXProfilingCommand(NO_COMMANDS
                , 1,
                                              TrajConfig.testTraj
        ));
    }

    private void testEzAuton()
    {

        PPWaypoint waypoint1 = PPWaypoint.simple2D(0, 0, 0, 3, -3);
        PPWaypoint waypoint2 = PPWaypoint.simple2D(0, 6, 5, 3, -3);
        PPWaypoint waypoint3 = PPWaypoint.simple2D(0, 12, 0, 3, -3);

        PP_PathGenerator pathGenerator = new PP_PathGenerator(waypoint1, waypoint2, waypoint3);
        Path path = pathGenerator.generate(0.05);

        PurePursuitMovementStrategy ppMoveStrat = new PurePursuitMovementStrategy(path, 0.1D);

        IVelocityMotor leftMotor = velocity -> Robot.DRIVE_TRAIN.runLeftVel((float) velocity);
        IVelocityMotor rightMotor = velocity -> Robot.DRIVE_TRAIN.runRightVel((float) velocity);

        IEncoder leftEncoder = new IEncoder() {
            @Override
            public double getPosition()
            {
                return Robot.DRIVE_TRAIN.getLeftPos();
            }

            @Override
            public double getVelocity()
            {
                return Robot.DRIVE_TRAIN.getLeftVel();
            }
        };

        IEncoder rightEncoder = new IEncoder() {
            @Override
            public double getPosition()
            {
                return Robot.DRIVE_TRAIN.getRightPos();
            }

            @Override
            public double getVelocity()
            {
                return Robot.DRIVE_TRAIN.getRightVel();
            }
        };

        EncoderWheel leftEncoderWheel = new EncoderWheel(leftEncoder, 0.25);

        EncoderWheel rightEncoderWheel = new EncoderWheel(rightEncoder, 0.25);

        ITankRobotConstants constants = () -> 3;
//
        TankRobotEncoderRotationEstimator locEstimator = new TankRobotEncoderRotationEstimator(leftEncoderWheel, rightEncoderWheel, constants);
//
        ILookahead lookahead = new LookaheadBounds(1, 5, 2, 10, locEstimator);
//
        TankRobotTransLocDriveable tankRobotTransLocDriveable = new TankRobotTransLocDriveable(leftMotor, rightMotor, locEstimator, locEstimator, constants);
        Command command = new PPCommand(ppMoveStrat, locEstimator, lookahead, tankRobotTransLocDriveable).build();
        addSequential(command);
    }

    private void testRotation()
    {
        addSequential(new FastRotateCommand(0, 8, -0.4F)); // rotate 0 degrees (shouldn't move anywhere)
        addSequential(new FastRotateCommand(90, 8, -0.4F)); // rotate 90 degrees clockwise
    }

    private void testRotateScaleToSwitch()
    {
        addSequential(new FastRotateCommand(92, 8, -0.4F));
    }

    // test going forward from scale to switch in auton after a rotation
    private void testPurePursuitForwardBackScaleSwitch()
    {
        PurePursuitCommand back = new PurePursuitCommand.Builder()
                .addWaypoint(0, 0, 8)
                .addWaypoint(0, 4.0F, 0)
                .setForward(true)
                .build();

        PurePursuitCommand forward = new PurePursuitCommand.Builder()
                .addWaypoint(0, 4.0F, 8)
                .addWaypoint(0, 0.8F, 0)
                .setForward(false)
                .build();

        // move back
        addSequential(back);

        // move forward
        addSequential(forward);
    }
}
