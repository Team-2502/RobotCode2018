package com.team2502.robot2018.command.test.group;

import com.team2502.robot2018.command.autonomous.ingredients.ActiveIntakeMove;
import com.team2502.robot2018.command.autonomous.ingredients.ElevatorAutonCommand;
import com.team2502.robot2018.command.autonomous.ingredients.ShootCubeCommand;
import com.team2502.robot2018.command.teleop.ButterflySetCommand;
import com.team2502.robot2018.command.teleop.GrabCommand;
import com.team2502.robot2018.command.teleop.ShiftElevatorCommand;
import com.team2502.robot2018.command.teleop.TransmissionCommand;
import com.team2502.robot2018.command.test.PrintCommand;
import com.team2502.robot2018.command.test.PromptCommand;
import com.team2502.robot2018.command.test.RotateStationaryCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

import java.util.ArrayList;
import java.util.List;

public class FullSystemsTestCommand extends CommandGroup
{
    public FullSystemsTestCommand()
    {
        messages.clear();

        print("Performing Full System Test");
        print("There are 3 second delays between each test");

        print("Rotate Stationary Command Active");
        RotateStationaryCommand command = new RotateStationaryCommand(3);
        addSequential(command);
        if(!command.getSuccess())
        {
            prompt("FAILURE. DO YOU UNDERSTAND?");
        }
        else
        {
            print("Success!");
        }

        newSection("Active Intake Up");
        addSequential(new ActiveIntakeMove(3,-0.5));
        print("Active Intake Down");
        addSequential(new ActiveIntakeMove(3,0.5));
        prompt("Did this occur?");

        newSection("Shooting OUT cube in active");
        addSequential(new ShootCubeCommand(3));
        prompt("Did this occur?");

        newSection("Toggling active intake grab");
        addSequential(new GrabCommand());
        prompt("Did this occur?");
        addSequential(new GrabCommand());

        newSection("Toggling transmission");
        addSequential(new TransmissionCommand());
        prompt("Did this occur?");
        addSequential(new TransmissionCommand());

        newSection("Toggling climber solenoid");
        addSequential(new ShiftElevatorCommand());
        prompt("Did this occur?");
        addSequential(new ShiftElevatorCommand());

        newSection("Raising Elevator");
        addSequential(new ElevatorAutonCommand(1,1));
        prompt("Did this occur?");

        newSection("Lowering Elevator");
        addSequential(new ElevatorAutonCommand(1,-0.5F));
        prompt("Did this occur?");

        newSection("Deploying Butterfly");
        addSequential(new ButterflySetCommand(true));
        prompt("Did this occur?");
        addSequential(new ButterflySetCommand(false));

        prompt("Systems test completed!");
    }

    private void print(String message)
    {
        addSequential(new PrintCommand(message));
    }

    private void newSection(String message)
    {
        print("");
        print(message);
        waitSome();
    }

    private void prompt(String message)
    {
        print(message);
        addSequential(new PromptCommand());
    }

    /**
     * gr8 name ... totally best conventions and totally not trying to get around {@link Object#wait()}...
     */
    private void waitSome()
    {
        addSequential(new WaitCommand(3));
    }

    private List<String> messages = new ArrayList<>(4);

    private String get(int index)
    {
        try { return messages.get(index); }
        catch(Exception e) { return ""; }
    }

    @Override
    protected void initialize()
    {

    }
}
