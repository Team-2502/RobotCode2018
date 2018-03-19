package com.team2502.robot2018;

import com.google.common.collect.ImmutableMap;
import com.kauailabs.navx.frc.AHRS;
import com.team2502.robot2018.command.autonomous.ingredients.AutonStrategy;
import com.team2502.robot2018.sendables.SendableDriveStrategyType;
import com.team2502.robot2018.sendables.SendableDriveTrain;
import com.team2502.robot2018.sendables.SendableVersioning;
import com.team2502.robot2018.subsystem.ActiveIntakeSubsystem;
import com.team2502.robot2018.subsystem.DriveTrainSubsystem;
import com.team2502.robot2018.subsystem.ElevatorSubsystem;
import com.team2502.robot2018.subsystem.solenoid.*;
import com.team2502.robot2018.trajectory.localization.EncoderDifferentialDriveLocationEstimator;
import com.team2502.robot2018.trajectory.localization.NavXLocationEstimator;
import com.team2502.robot2018.utils.Files;
import com.team2502.robot2018.utils.InterpolationMap;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import logger.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Software manifestation of Daedalus. If you delete this class you're doing something wrong.
 */
public final class Robot extends IterativeRobot
{
    public static double CAL_VELOCITY = 0D;
    public static String GAME_DATA = "...";

    /**
     * One and only instance of DriveTrainSubsystem. The transmission is in its own class.
     *
     * @see DriveTrainSubsystem
     * @see TransmissionSolenoid
     */
    public static DriveTrainSubsystem DRIVE_TRAIN;

    /**
     * One and only instance of Active Intake. The grabber solenoid is in its own class.
     *
     * @see ActiveIntakeSubsystem
     * @see ActiveIntakeSolenoid
     */
    public static ActiveIntakeSubsystem ACTIVE_INTAKE;

    /**
     * The compressor. It compresses air for us
     *
     * @see Compressor
     */
    public static Compressor COMPRESSOR;

    /**
     * The elevator and climber. The climber solenoid is in its own class
     *
     * @see ElevatorSubsystem
     * @see ClimberSolenoid
     */
    public static ElevatorSubsystem ELEVATOR;

    /**
     * The grabber solenoid for the active intake
     *
     * @see ActiveIntakeSubsystem
     * @see ActiveIntakeSolenoid
     */
    public static ActiveIntakeSolenoid ACTIVE_INTAKE_SOLENOID;

    /**
     * The climber solenoid. When activated, the climber is engaged and the elevator cannot go up
     *
     * @see ClimberSolenoid
     * @see ElevatorSubsystem
     */
    public static ClimberSolenoid CLIMBER_SOLENOID;

    /**
     * The butterfly solenoid. When toggled, the butterfly will drop.
     *
     * @see ButterflySolenoid
     */
    public static ButterflySolenoid BUTTERFLY_SOLENOID;

    /**
     * One and only instance of the Transmission Solenoid. The Drivetrain is in another class
     *
     * @see DriveTrainSubsystem
     * @see TransmissionSolenoid
     */
    public static TransmissionSolenoid TRANSMISSION_SOLENOID;

    public static ClimberCarriageBreakSubsystem CLIMBER_CARRIAGE_BREAK;

    /**
     * The NavX on the robot. To fully recalibrate,
     *
     * <ul>
     * <li>Press and hold the CAL button on the NavX for 5(?) seconds</li>
     * <li>Press the reset button</li>
     * <li>Wait until the orange light on the NavX stops flashing</li>
     * </ul>
     *
     * @see AHRS
     */
    public static AHRS NAVX;

    /**
     * Select our autonomous strategy for the match
     *
     * @see AutonStrategy
     */
    public static SendableChooser<AutonStrategy> autonStrategySelector;

    /**
     * A continuously running command that localizes the robot
     *
     * @see Robot#autonomousInit()
     * @see RobotLocalizationCommand
     */
    public static RobotLocalizationCommand ROBOT_LOCALIZATION_COMMAND;

    /**
     * A list of log messages that will get printed out once the robot is disabled
     *
     * @see Robot#writeLog(String, int, Object...)
     */
    private static List<String> logLines = new ArrayList<>();
    /**
     * Specifies the minimum level of log message to print out
     *
     * @see Robot#writeLog(String, int, Object...)
     */

    private static int LEVEL = 40;

    /**
     * Save a log message for later so that it can be printed out once disabled
     *
     * @param message A string that can be used with String.format
     * @param level   The level of the log message (how important it is)
     * @param objects The objects to format the string with
     * @see Robot#disabledInit()
     * @see Robot#logLines
     * @see String#format(String, Object...)
     */
    public static void writeLog(String message, int level, Object... objects)
    {
        if(level >= LEVEL)
        {
            logLines.add("(" + level + ") " + String.format(message, objects));
        }
    }

    /**
     * This method is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit()
    {
        // Initialize NavX
        NAVX = new AHRS(SPI.Port.kMXP);

        // Start pushing video from the camera to the DS
//        CameraServer.getInstance().startAutomaticCapture();

        // Create the autonomous strategy selector
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

        // Make Donovan's logger
        Log.createLogger(true);

        // Initialize all subsystems
        TRANSMISSION_SOLENOID = new TransmissionSolenoid();
        COMPRESSOR = new Compressor();
        DRIVE_TRAIN = new DriveTrainSubsystem();

        ACTIVE_INTAKE = new ActiveIntakeSubsystem();
        ELEVATOR = new ElevatorSubsystem();
        ACTIVE_INTAKE_SOLENOID = new ActiveIntakeSolenoid();
        CLIMBER_SOLENOID = new ClimberSolenoid();
        BUTTERFLY_SOLENOID = new ButterflySolenoid();
        CLIMBER_CARRIAGE_BREAK = new ClimberCarriageBreakSubsystem();

        // Initialize OI 
        OI.init();

        // Initialize the ACCELERATION_FOR_ELEVATOR_HEIGHT interpolation map. 
        Map<Double, Double> map = ImmutableMap.<Double, Double>builder()
                .put(0D, 14D)
                .build();

        Constants.Physical.ACCELERATION_FOR_ELEVATOR_HEIGHT = new InterpolationMap(map);
        AutoStartLocationSwitcher.putToSmartDashboard();

        // Initialize our sendables
        SendableDriveTrain.init();
        DashboardData.addUpdater(SendableDriveTrain.INSTANCE);

        DashboardData.addUpdater(SendableDriveStrategyType.INSTANCE);

        SendableVersioning.INSTANCE.init();
        SmartDashboard.putData(SendableVersioning.INSTANCE);

        // If this doesn't work update shuffleboard
        DashboardData.addUpdater(() -> SmartDashboard.putData(NAVX));

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
     * This method is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
     */

    public void disabledInit()
    {
        // Must lock climber when disabled
        // At the end of the match, we MUST lock the climber, which
        // will prevent our robot from falling. If it fell, we might drop two other
        // robots to the ground (which would be bad)

        Robot.CLIMBER_SOLENOID.lockElevator();

        // Print out the logs we saved up
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

    /**
     * Periodically runs while disabled
     */
    public void disabledPeriodic()
    {
        // TODO: why did we have this uncommented before?
//        Scheduler.getInstance().run();
//        DashboardData.update();

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
        String fileName = "/home/lvuser/FILES";
        Files.setFileName(fileName);
        Files.newFile(fileName);

        // Initialize Estimators
        NavXLocationEstimator rotEstimator = new NavXLocationEstimator();
        EncoderDifferentialDriveLocationEstimator encoderDifferentialDriveLocationEstimator = new EncoderDifferentialDriveLocationEstimator(rotEstimator);

        // Begin running the localization routine
        ROBOT_LOCALIZATION_COMMAND = new RobotLocalizationCommand(rotEstimator, encoderDifferentialDriveLocationEstimator, encoderDifferentialDriveLocationEstimator);

//        ROBOT_LOCALIZATION_COMMAND.execute();
        Scheduler.getInstance().add(ROBOT_LOCALIZATION_COMMAND);

        // Ensure that the motors are in slave mode like they should be
        DRIVE_TRAIN.setAutonSettings();

        ELEVATOR.calibrateEncoder();

//        ROBOT_LOCALIZATION_THREAD.start();

        // 144 inches front = 12 ft
        // 53 inches left/right = 4.42 ft

        // Drive the selected autonomous
        Scheduler.getInstance().add(AutoStartLocationSwitcher.getAutoInstance());
    }

    /**
     * This method is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
        Scheduler.getInstance().run();
        DashboardData.update();
        Files.writeToFile();
    }

    /**
     * This method is run once when Teleop is enabled
     */
    public void teleopInit()
    {
        DRIVE_TRAIN.setTeleopSettings();
        ELEVATOR.calibrateEncoder();
    }

    /**
     * This method is called periodically during operator control
     */
    public void teleopPeriodic()
    {
        Scheduler.getInstance().run();
        DashboardData.update();
    }

    /**
     * This method is called periodically during test mode
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