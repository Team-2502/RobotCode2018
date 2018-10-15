package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;


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
                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case SCALE_WAIT:
                            addSequential(new WaitCommand(3));
                        case SCALE:
                        case DEEP_SCALE:
                            goScaleLeft();
                            break;

                        case ONLY_SAME_SIDE:
                        default:
                            crossLine();
                            break;
                    }
                    break;

                case "LR":
                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case SCALE_WAIT:
                        case ONLY_SAME_SIDE:
                        case SCALE:
                            goScaleRight();
                            break;

                        case DEEP_SCALE:
                            goDeepScaleRight();
                            break;

                        default:
                            crossLine();
                            break;
                    }
                    break;

                case "RL":
                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case SCALE_WAIT:
                            addSequential(new WaitCommand(3));
                        case SCALE:
                            goScaleLeft();
                            break;
                            
                        case SWITCH:
                            goSwitch();
                            break;
                            
                        case ONLY_SAME_SIDE:
                        default:
                            crossLine();
                            break;
                    }
                    break;

                case "RR":
                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case ONLY_SAME_SIDE:
                        case SCALE_WAIT:
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
                            
                        default:
                            crossLine();
                            break;
                    }
                    break;

            }
        }
    }

    private void goDeepScaleRight()
    {
        
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
