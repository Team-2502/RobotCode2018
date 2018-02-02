package com.team2502.robot2018;

import com.team2502.robot2018.command.autonomous.PurePursuitCommand;
import com.team2502.robot2018.command.autonomous.groups.CenterCommandGroup;
import com.team2502.robot2018.command.autonomous.groups.LeftCommandGroup;
import com.team2502.robot2018.command.autonomous.groups.RightCommandGroup;
import com.team2502.robot2018.command.autonomous.ingredients.DriveTime;
import com.team2502.robot2018.command.teleop.CalibrateRobotCommand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.joml.ImmutableVector2f;

import java.util.ArrayList;

class AutoSwitcher
{
    private static SendableChooser<AutoMode> autoChooser;
    private static ArrayList<ImmutableVector2f> waypoints;

    static
    {
        waypoints = new ArrayList<>();
        waypoints.add(new ImmutableVector2f(0.0F, 0.0F));
        waypoints.add(new ImmutableVector2f(0.0F, 10.0F));
        waypoints.add(new ImmutableVector2f(10.0F, 10.0F));
        waypoints.add(new ImmutableVector2f(0.0F, 0.0F));
    }

    static void putToSmartDashboard()
    {
        autoChooser = new SendableChooser<AutoMode>();

        for(int i = 0; i < AutoMode.values().length; i++)
        {
            AutoMode mode = AutoMode.values()[i];
            if(i == 0) { autoChooser.addDefault(mode.name, mode); }
            else { autoChooser.addObject(mode.name, mode); }
        }

        SmartDashboard.putData("Autonomi:", autoChooser);
    }

    static Command getAutoInstance() { return autoChooser.getSelected().getInstance(); }

    public enum AutoMode
    {
        PURE_PURSUIT("PurePursuit", new PurePursuitCommand(waypoints)),
        CALIBRATE("Calibrate", new CalibrateRobotCommand()),
        DEMO("DriveTime", new DriveTime(3F, 0.2F)),
        CENTERCOMMANDGROUP("Center Line Up", new CenterCommandGroup()),
        LEFTCOMMANDGROUP("Left Line Up", new LeftCommandGroup()),
        RIGHTCOMMANDGROUP("Right Line Up", new RightCommandGroup());

        public final Command autoCommand;
        public final String name;

        AutoMode(String name, Command autoCommand)
        {
            this.autoCommand = autoCommand;
            this.name = name;
        }

        public Command getInstance()
        {
            return autoCommand;
        }
    }
}
