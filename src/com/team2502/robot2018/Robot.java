package com.team2502.robot2018;

import com.kauailabs.navx.frc.AHRS;
import com.team2502.robot2018.command.autonomous.PurePursuitCommand;
import com.team2502.robot2018.data.Vector;
import com.team2502.robot2018.subsystem.ClimberSubsystem;
import com.team2502.robot2018.subsystem.DriveTrainSubsystem;
import com.team2502.robot2018.subsystem.TransmissionSubsystem;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import logger.Log;

import java.io.*;
import java.util.ArrayList;

public final class Robot extends IterativeRobot
{
    public static DriveTrainSubsystem DRIVE_TRAIN;
    public static TransmissionSubsystem TRANSMISSION;
    public static ClimberSubsystem CLIMBER;
    public static long SHIFTED;
    public static Compressor COMPRESSOR;
    public static String GAME_DATA; //TODO: Have better name

    public static PrintWriter LOG_OUTPUT;

    // Currently the max percent voltage that can be given to each to each wheel
    public static final float VR_MAX = .3F;
    public static final float VL_MAX = .3F;
    public static final float VR_MIN = -.3F;
    public static final float VL_MIN = -.3F;
    // The distance between wheels (laterally) in Raw Encoder Units. Measure from the centerpoints of the wheels.
    public static final float LATERAL_WHEEL_DISTANCE = 7.6666666667F;
    // The lookahead distance (Raw Encoder Units) for Pure Pursuit
    public static final float LOOKAHEAD_DISTANCE = 1000F;

    // NavX Subsystem
    public static AHRS NAVX;
    private File logFile;

    public static void write(String string){
        LOG_OUTPUT.println(string);
        // System.out.println("I am writing something ");
    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        logFile = new File("/home/lvuser/log.txt");
        try
        {
            logFile.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            logFile.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        FileWriter fileWriter = null;
        try
        {
            fileWriter = new FileWriter(logFile);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        LOG_OUTPUT = new PrintWriter(fileWriter,true);

        Robot.write("tester");
        // System.out.println("writing tester");

        Log.createLogger();
        DRIVE_TRAIN = new DriveTrainSubsystem();
        CLIMBER = new ClimberSubsystem();
        NAVX = new AHRS(SPI.Port.kMXP);
        TRANSMISSION = new TransmissionSubsystem();
        COMPRESSOR = new Compressor();

        AutoSwitcher.putToSmartDashboard();

        OI.init();

        NAVX.resetDisplacement();
        // DashboardData.addUpdater(DRIVE_TRAIN);
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
     */

    public void disabledInit()
    {
        CLIMBER.stop();
        LOG_OUTPUT.close();
    }

    public void disabledPeriodic()
    {
        Scheduler.getInstance().run();
        DashboardData.update();
        GAME_DATA = DriverStation.getInstance().getGameSpecificMessage();
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
//        Scheduler.getInstance().add(AutoSwitcher.getAutoInstance());
        ArrayList<Vector> waypoints = new ArrayList<>();
        waypoints.add(new Vector(0, 0));
        waypoints.add(new Vector(7, 7));
        Scheduler.getInstance().add(new PurePursuitCommand(waypoints,LOOKAHEAD_DISTANCE));
        // Scheduler.getInstance().add(AutoSwitcher.getAutoInstance());
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

    public void teleopInit() { }

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
}
