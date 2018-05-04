package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import com.team2502.robot2018.command.teleop.QuickCommand;
import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LeftCommandGroup extends CommandGroup
{


    public LeftCommandGroup()
    {
        Robot.TRANSMISSION_SOLENOID.setLowGear(true);
        String AUTO_GAME_DATA = Robot.GAME_DATA.substring(0, 2);

        Robot.NAVX.reset();

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
                        case SCALE:
                        {
                            goScaleLeft();
                            break;
                        }
                        case SWITCH:
                        {
                            goSwitch();
                            break;
                        }
                        case DEEP_SCALE:
                        {
                            goDeepScaleLeft();
                            break;
                        }
                        case ONLY_SAME_SIDE:
                        {
                            goScaleLeft();
                            break;
                        }
                        default:
                            crossLine();
                            break;
                    }
                    break;
                case "LR":

//                    System.out.println("Going cross country");
//                    goScaleRight();
//                    break;

                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case SCALE:
                            goScaleRight();
                            break;

                        case ONLY_SAME_SIDE:
                            crossLine();
                            break;
                        case SWITCH:
                            goSwitch();
                            break;
                        default:
                            crossLine();
                            break;
                    }


                case "RL":
                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case SCALE:
                            goScaleLeft();
                            break;

                        case DEEP_SCALE:
                            goDeepScaleLeft();
                            break;

                        case ONLY_SAME_SIDE:
                            goScaleLeft();
                            break;
                        default:
                            crossLine();
                            break;


                    }

                case "RR":
//                    System.out.println("Going cross country!");
//                    goScaleRight();
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
                            crossLine();
                            break;
                    }
            }
        }


    }


    private void crossLine()
    {
        addSequential(new DriveTime(7, 0.45F));
    }

    private void goSwitch()
    {
        addParallel(new RaiseElevatorSwitch());

        addSequential(new PurePursuitCommand(Paths.Left.leftSwitch));

        addSequential(new FastRotateCommand(((float) SmartDashboard.getNumber("Side switch tuning: angle", 80)), 5, -0.4F));

        addSequential(new DeadreckoningDrive(1.5, 4));

        emitCube();
    }

    private void goScaleLeft()
    {
        addSequential(new PurePursuitCommand(Paths.Left.leftScale)); //Drive to location and raise elevator

        emitCube(); // Shoot cube via active spinning wheels, then open up for safety

        addParallel(new WaitCommand(3));
        addParallel(new ElevatorAutonCommand(1.7, 0));
        addSequential(new DeadreckoningDrive(7.0, -4.0F));

    }

    private void secondCubeLeft()
    {
        addSequential(new WaitCommand(2));
        addParallel(new ActiveIntakeRotate(0.5F, 0.5));

        addParallel(new PurePursuitCommand(Paths.Left.leftScaleToSwitch));

        addSequential(new ElevatorAutonCommand(3F, -Constants.SCALE_ELEV_HEIGHT_FT));
        addSequential(new QuickCommand(Robot.ELEVATOR::calibrateEncoder));

        addSequential(new ToggleIntakeCommand());
        addParallel(new ShootCubeCommand(2, -1));
        addSequential(new DeadreckoningDrive(1.5, 5));

        addSequential(new ToggleIntakeCommand());

        addParallel(new ActiveIntakeRotate(1F, -0.7));
        addSequential(new ElevatorAutonCommand(3F, Constants.SWITCH_ELEV_HEIGHT_FT + 1));

        emitCube();
    }

    private void goDeepScaleLeft()
    {
        addSequential(new PurePursuitCommand(Paths.Left.leftScaleDeepNullZone)); // Drive to deep scale
        emitCube(); // Shoot cube via active spinning wheels, then open up for safety

        addParallel(new WaitCommand(3));
        addParallel(new ElevatorAutonCommand(1.7, 0));
        addSequential(new DeadreckoningDrive(5.0, -5.0F));
    }

    private void goScaleRight()
    {
        addSequential(new PurePursuitCommand(Paths.Left.rightScale));

        emitCube();


        addParallel(new WaitCommand(3));
        addParallel(new ElevatorAutonCommand(1.7, 0));
        addSequential(new DeadreckoningDrive(5.0, -4.0F));
    }

    private void emitCube()
    {
        addSequential(new ActiveIntakeRotate(0.4, 1));

        addParallel(new ShootCubeCommand(3));
        addSequential(new WaitCommand(1));
        addSequential(new ToggleIntakeCommand());
    }


}
