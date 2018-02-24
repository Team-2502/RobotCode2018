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

import java.util.*;

public class FullSystemsTestCommand extends CommandGroup
{

    Map<String,Boolean> statuses = new HashMap<>();

    public FullSystemsTestCommand()
    {
        messages.clear();

        print("Performing Full System Test");
        print("There are 3 second delays between each test");

        newSection("Rotate Stationary Command Active");
        RotateStationaryCommand command = new RotateStationaryCommand(3);
        addSequential(command);

        newSection("Active Intake Up");
        addSequential(new ActiveIntakeMove(3,-0.5));
        print("Active Intake Down");
        addSequential(new ActiveIntakeMove(3,0.5));
        promptYesNo("Did this occur?","Active Intake Up/Down");

        newSection("Shooting OUT cube in active");
        addSequential(new ShootCubeCommand(3));
        promptYesNo("Did this occur?","Shooting out cube");

        newSection("Toggling active intake grab");
        addSequential(new GrabCommand());
        promptYesNo("Did this occur?","Active intake grab");
        addSequential(new GrabCommand());

        newSection("Toggling transmission");
        addSequential(new TransmissionCommand());
        promptYesNo("Did this occur?","Transmission toggle");
        addSequential(new TransmissionCommand());

        newSection("Toggling climber solenoid");
        addSequential(new ShiftElevatorCommand());
        promptYesNo("Did this occur?","Climber shift");
        addSequential(new ShiftElevatorCommand());

        newSection("Raising Elevator");
        addSequential(new ElevatorAutonCommand(1,1));
        promptYesNo("Did this occur?","Raising elevator");

        newSection("Lowering Elevator");
        addSequential(new ElevatorAutonCommand(1,-0.5F));
        promptYesNo("Did this occur?","Lowering elevator");

        newSection("Deploying Butterfly");
        addSequential(new ButterflySetCommand(true));
        promptYesNo("Did this occur?","Deploying butterfly");
        addSequential(new ButterflySetCommand(false));

        print("Systems test completed!");
        print("::: Results :::");
        printResults();
    }

    void printResults()
    {
        String results = "{";

        int i = 0;
        Set<Map.Entry<String, Boolean>> entries = statuses.entrySet();
        for(Map.Entry<String, Boolean> entry : entries)
        {
            i++;
            results+=entry.getKey()+":"+entry.getValue();
            if(i < entries.size() - 1)
            {
                results+=", ";
            }
        }
        results+="}";
        print(results);
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

    private void promptYesNo(String message, String name)
    {
        print(message);
        addSequential(new PromptCommand(name, statuses));
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
