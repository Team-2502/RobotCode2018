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

import java.util.List;

public final class Robot extends IterativeRobot
{
	public static DriveTrainSubsystem DRIVE_TRAIN;
	public static TransmissionSubsystem TRANSMISSION;
	public static ClimberSubsystem CLIMBER;
	public static long SHIFTED;
	public static Compressor COMPRESSOR;
	public static String GAME_DATA; //TODO: Have better name


	// NavX Subsystem
	public static AHRS NAVX;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit()
	{
		Log.createLogger();
		DRIVE_TRAIN = new DriveTrainSubsystem();
		CLIMBER = new ClimberSubsystem();
		NAVX = new AHRS(SPI.Port.kMXP);
		TRANSMISSION = new TransmissionSubsystem();
		COMPRESSOR = new Compressor();

		AutoSwitcher.putToSmartDashboard();

		OI.init();

		NAVX.resetDisplacement();

		DashboardData.addUpdater(DRIVE_TRAIN);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	public void disabledInit()
	{
		CLIMBER.stop();
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
		// waypoints (meter,meter) ... first waypoint should _probably_ be (0,0)
		List<Vector> waypoints = Vector.genFromArray(
				new float[][] {
						{ 0, 0 },
						{ 1, 1 },
						{ 2, 7 },
				});
		Scheduler.getInstance().add(new PurePursuitCommand(waypoints, 1));
		// Scheduler.getInstance().add(AutoSwitcher.getAutoInstance());

		GAME_DATA = DriverStation.getInstance().getGameSpecificMessage();
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
}