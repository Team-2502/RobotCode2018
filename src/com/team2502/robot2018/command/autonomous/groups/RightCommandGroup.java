package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

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
//                    System.out.println("Going cross country!");
//                    goScaleLeft();
//                    break;

                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case SCALE:
                            goScaleLeft();
                            break;

                        case ONLY_SAME_SIDE:
                            crossLine();
                            break;
                        default:
                            break;
                    }
                    break;

                case "LR":
                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case SCALE:
                            goScaleRight();
                            break;

                        case DEEP_SCALE:
                            goDeepScaleRight();
                            break;

                        case ONLY_SAME_SIDE:
                            goScaleRight();
                            break;
                        default:
                            break;
                    }
                    break;

                case "RL":
//                    System.out.println("Going cross country!");
//                    goScaleLeft();
//                    break;
                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case SCALE:
                            goScaleRight();
                            break;

                        case ONLY_SAME_SIDE:
                            crossLine();
                            break;
                        default:
                            break;
                    }
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
                        case DEEP_SCALE:
                            goDeepScaleRight();
                            break;
                        case ONLY_SAME_SIDE:
                            goScaleRight();
                            break;
                        default:
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
        addSequential(new PurePursuitCommand(Paths.Right.leftScale)); // Drive to locatino and raise elevator
        emitCube(); // Special cube shooty boi

        addSequential(new DeadreckoningDrive(0.7F, -4F)); // Reverse, reverse
        addSequential(new ElevatorAutonCommand(2.5, 0)); // resetti elevatori
    }

    private void goSwitch()
    {
        addParallel(new RaiseElevatorSwitch());

        addSequential(new PurePursuitCommand(Paths.Right.rightSwitch));

        addSequential(new ActiveIntakeRotate(0.35, 1));

        emitCube();
    }

    private void goScaleRight()
    {
//        addParallel(new ActiveIntakeRotate(1, 0.5));
        addSequential(new PurePursuitCommand(Paths.Right.rightScale));

        emitCube(); // Special cube shooty boi

        addSequential(new DeadreckoningDrive(0.7F, -4F)); // Reverse, reverse
        addSequential(new ElevatorAutonCommand(2.5, 0)); // resetti elevatori
    }

    private void goDeepScaleRight()
    {
        addParallel(new PurePursuitCommand(Paths.Right.rightScaleDeepNullZone));
        emitCube(); // Special cube shooty boi

        addSequential(new DeadreckoningDrive(0.7F, -4F)); // Reverse, reverse
        addSequential(new ElevatorAutonCommand(2.5, 0)); // resetti elevatori
    }

    private void crossLine()
    {
        addSequential(new DriveTime(9, 0.5F));

    }

    private void emitCube()
    {
        addSequential(new ActiveIntakeRotate(0.4, 1));

        addParallel(new ShootCubeCommand(3));
        addSequential(new WaitCommand(1));
        addSequential(new ToggleIntakeCommand());
    }
}
