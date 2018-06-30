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
                        case SCALE_WAIT:
                            addSequential(new WaitCommand(3));
                            goScaleLeft();
                            break;
                        default:
                            crossLine();
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
                        case SCALE_WAIT:
                            goScaleRight();
                            break;
                        default:
                            crossLine();
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
                            goScaleLeft();
                            break;

                        case ONLY_SAME_SIDE:
                            crossLine();
                            break;
                        case SWITCH:
                            goSwitch();
                            break;
                        case SCALE_WAIT:
                            addSequential(new WaitCommand(3));
                            goScaleLeft();
                            break;
                        default:
                            crossLine();
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
                        case SCALE_WAIT:
                            goScaleRight();
                            break;
                        default:
                            crossLine();
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
        emitCube();

        addParallel(new WaitCommand(3));
        addParallel(new ActiveIntakeRotate(1, -1));
        addParallel(new ElevatorAutonCommand(1.7, 0));
        addSequential(new DeadreckoningDrive(5.0, -4.0F));
    }

    private void goSwitch()
    {
        addParallel(new RaiseElevatorSwitch());

        addSequential(new PurePursuitCommand(Paths.Left.leftSwitch));

        addSequential(new FastRotateCommand(360-85, 5, -0.4F));

        addSequential(new DeadreckoningDrive(1.5, 4));

        emitCube();
    }

    private void goScaleRight()
    {
        addSequential(new PurePursuitCommand(Paths.Right.rightScale));

        emitCube();

        addParallel(new WaitCommand(3));
        addParallel(new ElevatorAutonCommand(1.7, 0));
        addSequential(new DeadreckoningDrive(7.0, -4.0F));
    }

    private void goDeepScaleRight()
    {
        addParallel(new PurePursuitCommand(Paths.Right.rightScaleDeepNullZone));
        emitCube();

        addParallel(new WaitCommand(3));
        addParallel(new ElevatorAutonCommand(1.7, 0));
        addSequential(new DeadreckoningDrive(5.0, -5.0F));
    }

    private void crossLine()
    {
        addSequential(new DriveTime(2.7F, 0.45F));
    }

    private void emitCube()
    {
        addSequential(new ActiveIntakeRotate(0.4, 1));

        addParallel(new ShootCubeCommand(3, 0.3F));
        addSequential(new WaitCommand(1));
        addSequential(new ToggleIntakeCommand());
    }
}
