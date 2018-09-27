package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import edu.wpi.first.wpilibj.command.CommandGroup;


public class RightCommandGroup extends CommandGroup
{
    public RightCommandGroup()
    {
        String AUTO_GAME_DATA = Robot.GAME_DATA.substring(0, 2);

        if(Robot.autonStrategySelector.getSelected() == AutonStrategy.STRAIGHT)
        {
            crossLine();
        }
        else
        {
            switch(AUTO_GAME_DATA)
            {
                case "RL":
                case "LL":
                    System.out.println("Going cross country!");
                    goScaleLeft();
                    break;

                case "RR":
                case "LR":
                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case SCALE:
                            goScaleRight();
                            break;
                    }
            }
        }
    }

    /**
     * @deprecated should be tested on left side before mirroring on right side... tis a bit of a mess rn and might not
     * mirror perfectly.
     */
    private void secondCubeRight()
    {
    }

    private void goScaleLeft()
    {
        addSequential(new GoScaleCrossCountry(PathConfig.Right.leftScale));
    }

    private void goSwitch()
    {

        addParallel(new RaiseElevatorSwitch());

        addSequential(new PurePursuitCommand(PathConfig.Right.rightSwitch, true));

        addSequential(new ActiveIntakeRotate(0.35, 1));

        emitCube();
    }

    private void goScaleRight()
    {
        addSequential(new GoScaleSameSide(PathConfig.Right.rightScale));
    }

    private void crossLine()
    {
        addSequential(new CrossLine());
    }

    private void emitCube()
    {
        addSequential(new RunIntakeCommand(1, .5));

    }
}
