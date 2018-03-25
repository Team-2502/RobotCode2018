package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import edu.wpi.first.wpilibj.command.CommandGroup;

import static com.team2502.robot2018.Constants.Physical.Elevator;

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
                case "LL":
                    System.out.println("Going cross country!");
                    goScaleLeft();
                    break;

                case "LR":
                    goScaleRight();
                    break;

                case "RL":
                    System.out.println("Going cross country!");
                    goScaleLeft();
                    break;

                case "RR":
                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case SCALE:
                            goScaleRight();
                            break;
                        case SWITCH:
                            goSwitch();
                            break;
                        case SCALE_TWICE:
                            goScaleRight();
                            secondCubeRight();
                            break;
                    }
                    break;
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
        addSequential(new PurePursuitCommand(PathConfig.Right.rightSwitch, false));
        addSequential(new ElevatorAutonCommand(.8F, Elevator.SWITCH_ELEV_HEIGHT_FT));
        addSequential(new ActiveIntakeRotate(0.35, 1));

        emitCube();
    }

    private void goScaleRight()
    {
        addSequential(new GoScaleSameSide(PathConfig.Right.rightScale));
    }

    private void crossLine()
    {
        addSequential(new DriveTime(7, 0.4F));
    }

    private void emitCube()
    {
        addSequential(new RunIntakeCommand(1, .5));

    }
}
