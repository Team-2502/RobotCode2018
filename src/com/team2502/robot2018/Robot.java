package com.team2502.robot2018;

import com.google.common.collect.ImmutableMap;
import com.kauailabs.navx.frc.AHRS;
import com.team2502.robot2018.command.autonomous.ingredients.AutonStrategy;
import com.team2502.robot2018.sendables.SendableDriveStrategyType;
import com.team2502.robot2018.sendables.SendableDriveTrain;
import com.team2502.robot2018.sendables.SendableNavX;
import com.team2502.robot2018.sendables.SendableVersioning;
import com.team2502.robot2018.subsystem.ActiveIntakeSubsystem;
import com.team2502.robot2018.subsystem.DriveTrainSubsystem;
import com.team2502.robot2018.subsystem.ElevatorSubsystem;
import com.team2502.robot2018.subsystem.solenoid.*;
import com.team2502.robot2018.trajectory.localization.EncoderDifferentialDriveLocationEstimator;
import com.team2502.robot2018.trajectory.localization.NavXLocationEstimator;
import com.team2502.robot2018.utils.Files;
import com.team2502.robot2018.utils.InterpolationMap;
import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import logger.Log;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class Robot extends IterativeRobot
{
    public static CarriageBrakeSolenoid CARRIAGE_BRAKE_SOLENOID;
    public static double CAL_VELOCITY = 0D;
    public static String GAME_DATA = "...";

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
    public static SendableChooser<AutonStrategy> autonStrategySelector;
    public static RobotLocalizationCommand ROBOT_LOCALIZATION_COMMAND;
    private static List<String> logLines = new ArrayList<>();
    private static int LEVEL = 40;

    public static void write(String string)
    { LOG_OUTPUT.println(string); }

    public static void writeLog(String message, int level, Object... objects)
    {
        if(level >= LEVEL)
        {
            logLines.add("(" + level + ") " + String.format(message, objects));
        }
    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit()
    {
        SmartDashboard.putNumber("Side switch tuning: angle", 80);
        SmartDashboard.putNumber("Side switch distance", 13.5);
        MathUtils.init();
        NAVX = new AHRS(SPI.Port.kMXP);
//        CameraServer.getInstance().startAutomaticCapture();
        autonStrategySelector = new SendableChooser<>();
        AutonStrategy[] values = AutonStrategy.values();
        for(int i = 0; i < values.length; i++)
        {
            AutonStrategy autonStrategy = values[i];
            if(i == 0)
            {
                autonStrategySelector.addDefault(autonStrategy.getName(), autonStrategy);
            }
            else
            {
                autonStrategySelector.addObject(autonStrategy.getName(), autonStrategy);
            }
        }

        SmartDashboard.putData("auto_strategy", autonStrategySelector);

        Log.createLogger(true);

        TRANSMISSION_SOLENOID = new TransmissionSolenoid();
        COMPRESSOR = new Compressor();
        DRIVE_TRAIN = new DriveTrainSubsystem();

        ACTIVE_INTAKE = new ActiveIntakeSubsystem();
        ELEVATOR = new ElevatorSubsystem();
        ACTIVE_INTAKE_SOLENOID = new ActiveIntakeSolenoid();
        CLIMBER_SOLENOID = new ClimberSolenoid();
        BUTTERFLY_SOLENOID = new ButterflySolenoid();
        CARRIAGE_BRAKE_SOLENOID = new CarriageBrakeSolenoid();
        OI.init();


        Map<Double, Double> map = ImmutableMap.<Double, Double>builder()
                .put(0D, 14D)
                .build();

        Constants.ACCELERATION_FOR_ELEVATOR_HEIGHT = new InterpolationMap(map);
        AutoStartLocationSwitcher.putToSmartDashboard();

        SendableDriveTrain.init();
        DashboardData.addUpdater(SendableDriveTrain.INSTANCE);

        DashboardData.addUpdater(SendableDriveStrategyType.INSTANCE);

        SendableVersioning.INSTANCE.init();
        SmartDashboard.putData(SendableVersioning.INSTANCE);

        SendableNavX.init();
        DashboardData.addUpdater(SendableNavX.INSTANCE);

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

        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = logLines.iterator();

        stringBuilder.append("::: LOG ::: \n");
        while(iterator.hasNext())
        {
            String message = iterator.next();
            stringBuilder.append(message);
            stringBuilder.append('\n');
            iterator.remove();
        }
        System.out.println(stringBuilder.toString());
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
//
//        String fileName = "/home/lvuser/FILES";
//        Files.setFileName(fileName);
//        Files.newFile(fileName);

        NavXLocationEstimator rotEstimator = new NavXLocationEstimator();
        EncoderDifferentialDriveLocationEstimator encoderDifferentialDriveLocationEstimator = new EncoderDifferentialDriveLocationEstimator(rotEstimator);
        ROBOT_LOCALIZATION_COMMAND = new RobotLocalizationCommand(rotEstimator, encoderDifferentialDriveLocationEstimator, encoderDifferentialDriveLocationEstimator);

//        ROBOT_LOCALIZATION_COMMAND.execute();
        Scheduler.getInstance().add(ROBOT_LOCALIZATION_COMMAND);

        DRIVE_TRAIN.setAutonSettings();

        ELEVATOR.calibrateEncoder();

//        ROBOT_LOCALIZATION_THREAD.start();

        // 144 inches front = 12 ft
        // 53 inches left/right = 4.42 ft

//        Scheduler.getInstance().add(new CenterCommandGroup());

        Scheduler.getInstance().add(AutoStartLocationSwitcher.getAutoInstance());
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
        Scheduler.getInstance().run();
        DashboardData.update();
        Files.writeToFile();
    }

    public void teleopInit()
    {
        DRIVE_TRAIN.setTeleopSettings();
        ELEVATOR.calibrateEncoder();
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

//        if((System.currentTimeMillis() % 10000) == 0) { Files.newFile(fileName); }

//        Files.setNameAndValue("Right Pos", DRIVE_TRAIN.getRightPos());
//        Files.setNameAndValue("Left Pos", DRIVE_TRAIN.getLeftPos());
//        Files.writeToFile();
//        Files.s
    }
}