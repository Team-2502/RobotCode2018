package com.team2502.robot2018;

import com.kauailabs.navx.frc.AHRS;
import com.team2502.robot2018.command.autonomous.ingredients.AutonStrategy;
import com.team2502.robot2018.command.autonomous.ingredients.PurePursuitCommand;
import com.team2502.robot2018.sendables.SendableDriveStrategyType;
import com.team2502.robot2018.sendables.SendableDriveTrain;
import com.team2502.robot2018.sendables.SendableNavX;
import com.team2502.robot2018.sendables.SendableVersioning;
import com.team2502.robot2018.subsystem.ActiveIntakeSubsystem;
import com.team2502.robot2018.subsystem.DriveTrainSubsystem;
import com.team2502.robot2018.subsystem.ElevatorSubsystem;
import com.team2502.robot2018.subsystem.solenoid.ActiveIntakeSolenoid;
import com.team2502.robot2018.subsystem.solenoid.ButterflySolenoid;
import com.team2502.robot2018.subsystem.solenoid.ClimberSolenoid;
import com.team2502.robot2018.subsystem.solenoid.TransmissionSolenoid;
import com.team2502.robot2018.trajectory.Waypoint;
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
    public static String GAME_DATA = "...";

    public static AutonStrategy AUTON_STRATEGY;
    public static DriveTrainSubsystem DRIVE_TRAIN;
    public static ActiveIntakeSubsystem ACTIVE_INTAKE;
    public static Compressor COMPRESSOR;
    public static PrintWriter LOG_OUTPUT;
    public static ElevatorSubsystem ELEVATOR;
    public static ActiveIntakeSolenoid ACTIVE_INTAKE_SOLENOID;
    public static ClimberSolenoid CLIMBER_SOLENOID;
    public static ButterflySolenoid BUTTERFLY_SOLENOID;
    public static TransmissionSolenoid TRANSMISSION_SOLENOID;
    public static AHRS NAVX;

    public static void write(String string)
    {
        LOG_OUTPUT.println(string);
    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit()
    {
        // TODO: needs to be changed in shuffleboard
        AUTON_STRATEGY = AutonStrategy.SCALE;

        Log.createLogger(true);

        COMPRESSOR = new Compressor();
        DRIVE_TRAIN = new DriveTrainSubsystem();
        NAVX = new AHRS(SPI.Port.kMXP);
        ACTIVE_INTAKE = new ActiveIntakeSubsystem();
        ELEVATOR = new ElevatorSubsystem();
        ACTIVE_INTAKE_SOLENOID = new ActiveIntakeSolenoid();
        CLIMBER_SOLENOID = new ClimberSolenoid();
        BUTTERFLY_SOLENOID = new ButterflySolenoid();
        TRANSMISSION_SOLENOID = new TransmissionSolenoid();

        OI.init();

        AutoStartLocationSwitcher.putToSmartDashboard();

        SendableDriveTrain.init();
        DashboardData.addUpdater(SendableDriveTrain.getInstance());

        DashboardData.addUpdater(SendableDriveStrategyType.getInstance());

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
        // Must lock climber when disabled
        // At the end of the match, we MUST lock the climber, which
        // will prevent our robot from falling, thus dropping two other
        // robots to the ground.
        Robot.CLIMBER_SOLENOID.lockElevator();
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

        // 144 inches front = 12 ft
        // 53 inches left/right = 4.42 ft

        List<Waypoint> waypoints = Arrays.asList(
            new Waypoint(new ImmutableVector2f(0,0),0),
            new Waypoint(new ImmutableVector2f(0,7),8),
            new Waypoint(new ImmutableVector2f(0,14),0)
        );
        Scheduler.getInstance().add(new PurePursuitCommand(waypoints,3,1.5F));

//        Scheduler.getInstance().add(AutoStartLocationSwitcher.getAutoInstance());
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