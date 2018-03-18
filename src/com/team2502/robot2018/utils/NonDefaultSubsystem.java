package com.team2502.robot2018.utils;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * An abstract class for Subsystems without default command
 *
 * @see edu.wpi.first.wpilibj.command.Subsystem
 */
public abstract class NonDefaultSubsystem extends Subsystem
{
    @Override
    protected void initDefaultCommand()
    { }
}
