package com.team2502.robot2018;

import com.kauailabs.navx.frc.AHRS;
import com.team2502.robot2018.command.autonomous.PurePursuitCommand;
import com.team2502.robot2018.sendables.SendableDriveTrain;
import com.team2502.robot2018.sendables.SendableNavX;
import com.team2502.robot2018.sendables.SendableVersioning;
import com.team2502.robot2018.subsystem.*;
import com.team2502.robot2018.utils.Files;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import logger.Log;
import org.joml.ImmutableVector2f;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public final class Robot extends IterativeRobot
{
    public static double CAL_VELOCITY = 0D;
    public static long SHIFTED;
    public static String GAME_DATA = "    ";

    public static DriveTrainSubsystem DRIVE_TRAIN;
    public static TransmissionSubsystem TRANSMISSION;
    public static ActiveSubsystem ACTIVE_INTAKE;
    public static ClimberSubsystem CLIMBER;
    public static Compressor COMPRESSOR;
    public static PrintWriter LOG_OUTPUT;
    public static ElevatorSubsystem ELEVATOR;
    public static AHRS NAVX;

    public static void write(String string)
    {
        LOG_OUTPUT.println(string);
        // System.out.println("I am writing something ");
    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        Log.createLogger(true);

        CLIMBER = new ClimberSubsystem();
        COMPRESSOR = new Compressor();
        DRIVE_TRAIN = new DriveTrainSubsystem();
        NAVX = new AHRS(SPI.Port.kMXP);
        TRANSMISSION = new TransmissionSubsystem();
        ACTIVE_INTAKE = new ActiveSubsystem();
        ELEVATOR = new ElevatorSubsystem();

        OI.init();

        AutoSwitcher.putToSmartDashboard();

        SendableDriveTrain.init();
        DashboardData.addUpdater(SendableDriveTrain.getInstance());

        SendableVersioning.getInstance().init();
        SmartDashboard.putData(SendableVersioning.getInstance());

        SendableNavX.init();
        DashboardData.addUpdater(SendableNavX.getInstance());

        DashboardData.addUpdater(() -> {
            Robot.CAL_VELOCITY = SmartDashboard.getNumber("calibration_velocity", 0);
            SmartDashboard.putNumber("calibration_velocity", Robot.CAL_VELOCITY);
        });

        SmartDashboard.putBoolean("calibration_enabled", false);
        SmartDashboard.putNumber("calibration_velocity", 0);

        NAVX.resetDisplacement();

        fileWriting();
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
     */

    public void disabledInit()
    {
        CLIMBER.stop();
//        LOG_OUTPUT.close();
    }

    public void disabledPeriodic()
    {
        Scheduler.getInstance().run();
        DashboardData.update();
        GAME_DATA = DriverStation.getInstance().getGameSpecificMessage();
        if(GAME_DATA == null) { GAME_DATA = "___"; }
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString code to get the auto name from the text box below the Gyro
     * <p>
     * You can add additional auto modes by adding additional commands to the
     * chooser code above (like the commented example) or additional comparisons
     * to the switch structure below with additional strings and commands.
     */
    public void autonomousInit()
    {
        DRIVE_TRAIN.setAutonSettings();

        List<ImmutableVector2f> waypoints = Arrays.asList(
                new ImmutableVector2f(0, 0),
                new ImmutableVector2f(0, 26),
                new ImmutableVector2f(-6, 26),
                new ImmutableVector2f(-6, 0),
                new ImmutableVector2f(0, 0)
                                                         );

//        Scheduler.getInstance().add(new CalibrateRobotCommand());
        Scheduler.getInstance().add(new PurePursuitCommand(waypoints, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
//        Scheduler.getInstance().add(AutoSwitcher.getAutoInstance());

        NAVX.reset();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
        Scheduler.getInstance().run();
        DashboardData.update();
    }

    public void teleopInit()
    {
        DRIVE_TRAIN.setTeleopSettings();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
        Scheduler.getInstance().run();
        DashboardData.update();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic()
    {
        LiveWindow.run();
        DashboardData.update();
    }

    private void fileWriting()
    {
        String fileName = "/home/lvuser/FILES";
        Files.setFileName(fileName);
        
        if((System.currentTimeMillis() % 10000) == 0) { Files.newFile(fileName); }

        Files.setNameAndValue("Right Pos", DRIVE_TRAIN.getRightPos());
        Files.setNameAndValue("Left Pos", DRIVE_TRAIN.getLeftPos());
        Files.writeToFile();
    }
}