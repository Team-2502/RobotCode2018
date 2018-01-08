package com.team2502.robot2018.subsystem;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.RobotMap;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Contains all the code for the transmission and some of the math relating to Automatic Shifting
 */
public class TransmissionSubsystem extends Subsystem
{
	private static Solenoid switcher;
	public boolean disabledAutoShifting = false;

	// TODO: Change name to `lowGear` if applicable.
	public boolean highGear;

	public TransmissionSubsystem()
	{
		switcher = new Solenoid(RobotMap.Solenoid.TRANSMISSION_SWITCH);
		highGear = false;
	}

	@Override
	protected void initDefaultCommand()
	{
	}

	/**
	 * Switch the gear from its current state
	 */
	public void switchGear()
	{
		setGear(highGear = !highGear);
		Robot.SHIFTED = System.currentTimeMillis();
	}

	/**
	 * @return if we are in high gear
	 */
	public boolean getGear()
	{
		return highGear;
	}

	/**
	 * Set the transmission to a specific high gear or low gear
	 *
	 * @param highGear Boolean saying "do you want to be in high gear?"
	 */
	public void setGear(boolean highGear)
	{
		if (this.highGear != highGear)
		{
			Robot.SHIFTED = System.currentTimeMillis();
			switcher.set(this.highGear = highGear);
		}
	}

	/**
	 * Autoshifting can be disabled during robot demonstrations. This method toggles it.
	 */
	public void toggleAutoShifting()
	{
		disabledAutoShifting = !disabledAutoShifting;
	}

	/**
	 * @param fps The number in feet per second to convert to RPM given wheels of 4 inches in diameter
	 * @return The equivalent RPM
	 */
	public double fpsToRPM(double fps)
	{
		return ((fps) * 60 * 12) / (4 * Math.PI);
	}


	/**
	 * @param x A number
	 * @return The sign of the number
	 */
	public double sign(double x)
	{
		return Math.abs(x) / x;
	}

	/**
	 * @param x A number
	 * @param y Another number
	 * @return If the numbers have the same sign
	 */
	public boolean signsame(double x, double y)
	{
		return sign(x) == sign(y);
	}
}