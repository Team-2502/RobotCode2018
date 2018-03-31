package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightCommandGroup extends CommandGroup
{
    public RightCommandGroup()
    {
        Robot.NAVX.reset();

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
                        case SWITCH_SCALE:
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
     * mirror perfectly... we aren't doing this at Duluth anyway.
     */
    private void secondCubeRight()
    {
    }

    private void goScaleLeft()
    {
        addSequential(new PurePursuitCommand(Paths.Right.leftScale));

        addParallel(new ActiveIntakeRotate(1F, -0.5));

        emitCube();

        addSequential(new DeadreckoningDrive(0.7F, -4F));
        addSequential(new ElevatorAutonCommand(2.5, 0));
    }

    private void goSwitch()
    {
        addSequential(new PurePursuitCommand(Paths.Right.rightSwitch));
        addSequential(new ElevatorAutonCommand(.8F, Constants.SWITCH_ELEV_HEIGHT_FT));
        addSequential(new ActiveIntakeRotate(0.35, 1));

        emitCube();
    }

    private void goScaleRight()
    {
        // TODO: Move things into constants
        addParallel(new ActiveIntakeRotate(1, 0.5));
        addSequential(new PurePursuitCommand(Paths.Right.rightScale));

        addSequential(new ToggleIntakeCommand());
        addSequential(new ActiveIntakeRotate(.25F, -0.5));

        emitCube();

        addSequential(new DeadreckoningDrive(0.7F, -4F));
        addSequential(new ElevatorAutonCommand(2.5, 0));


    }

    private void goDeepScaleRight()
    {
        addParallel(new PurePursuitCommand(Paths.Right.rightScaleDeepNullZone));
    }

    private void crossLine()
    {
        addSequential(new DriveTime(7, 0.4F));

    }

    private void emitCube()
    {
        addSequential(new ShootCubeCommand(1, .5));

    }
}
