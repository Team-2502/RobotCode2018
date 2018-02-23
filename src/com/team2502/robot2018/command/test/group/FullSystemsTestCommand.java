package com.team2502.robot2018.command.test.group;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import logger.Log;

import java.util.ArrayList;
import java.util.List;

public class FullSystemsTestCommand extends InstantCommand
{
    private int pushCount;

    public FullSystemsTestCommand()
    {
        pushCount = 0;

        requires(Robot.DRIVE_TRAIN);
        requires(Robot.ACTIVE_INTAKE);
        requires(Robot.ACTIVE_INTAKE_SOLENOID);
        requires(Robot.ELEVATOR);
        requires(Robot.CLIMBER_SOLENOID);
        requires(Robot.BUTTERFLY_SOLENOID);
        requires(Robot.TRANSMISSION_SOLENOID);
    }

    private List<String> messages = new ArrayList<String>(4);

    private String get(int index)
    {
        try { return messages.get(index); }
        catch(Exception e) { return ""; }
    }

    private void log(String message)
    {
        Log.info(message);
        messages.add(message);
        if(messages.size() > 4) { messages.remove(0); }
        SmartDashboard.putString("Test0", get(0));
        SmartDashboard.putString("Test1", get(1));
        SmartDashboard.putString("Test2", get(2));
        SmartDashboard.putString("Test3", get(3));
    }

    private static void sleep(int millis)
    {
        try { Thread.sleep(millis); }
        catch(InterruptedException e) { e.printStackTrace(); }
    }

    /**
     * Run a command every n milliseconds for a period of time.
     *
     * @param r         The Command to run.
     * @param delay     The delay between each cycle in milliseconds
     * @param runTime   The amount of time to run for in milliseconds
     */
    private void run(Runnable r, int delay, int runTime)
    {
        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - startTime < runTime)
        {
            r.run();
            sleep(delay);
        }
    }

    @Override
    protected void execute()
    {
        messages.clear();
        log("Preparing to run system tests.");
        if(!SmartDashboard.getBoolean("Run Debug Tests", false)) { return; }

        if(++pushCount > 2) { pushCount = 0; }
        else { return; }

        log("::: Performing Full System Test :::");
        log("There are 3 second delays between each test");

        sleep(1000);

        int testId = 1;

        log("Test " + testId++ + ": Run left motors forward");
        run(() -> Robot.DRIVE_TRAIN.runMotors(0.5f, 0.0f), 10, 1000);
        Robot.DRIVE_TRAIN.stop();
        sleep(3000);

        log("Test " + testId++ + ": Run right motors forward");
        run(() -> Robot.DRIVE_TRAIN.runMotors(0.0f, 0.5f), 10, 1000);
        Robot.DRIVE_TRAIN.stop();
        sleep(3000);

        log("Test " + testId++ + ": Run left motors backwards");
        run(() -> Robot.DRIVE_TRAIN.runMotors(-0.5f, 0.0f), 10, 1000);
        Robot.DRIVE_TRAIN.stop();
        sleep(3000);

        log("Test " + testId++ + ": Run right motors backwards");
        run(() -> Robot.DRIVE_TRAIN.runMotors(0.0f, -0.5f), 10, 1000);
        Robot.DRIVE_TRAIN.stop();
        sleep(3000);

        log("Test " + testId++ + ": Run active intake.");
        run(() -> Robot.ACTIVE_INTAKE.runIntake(0.5D), 10, 1000);
        Robot.ACTIVE_INTAKE.stopAll();
        sleep(3000);

        log("Test " + testId++ + ": Rotate active intake.");
        run(() -> Robot.ACTIVE_INTAKE.rotateIntake(0.5D), 10, 500);
        Robot.ACTIVE_INTAKE.stopAll();
        sleep(3000);

        log("Test " + testId++ + ": Toggle active intake");
        Robot.ACTIVE_INTAKE_SOLENOID.toggleIntake();
        sleep(3000);

        log("Test " + testId++ + ": Engaging climber");
        Robot.CLIMBER_SOLENOID.engageClimber();
        sleep(3000);

        log("Test " + testId++ + ": Disengaging climber");
        Robot.CLIMBER_SOLENOID.disengageClimber();
        sleep(3000);

        log("Test " + testId++ + ": Raise elevator");
        run(() -> Robot.ELEVATOR.moveElevator(0.5D), 10, 1000);
        sleep(3000);

        log("Test " + testId++ + ": Lower elevator");
        run(() -> Robot.ELEVATOR.moveElevator(-0.5D), 10, 800);
        sleep(3000);

        log("Test " + testId++ + ": Open butterfly solenoids");
        Robot.BUTTERFLY_SOLENOID.set(false);
        sleep(3000);

        log("Test " + testId++ + ": Close butterfly solenoids");
        Robot.BUTTERFLY_SOLENOID.set(true);
        sleep(3000);

        log("Test " + testId++ + ": Setting high gear");
        Robot.TRANSMISSION_SOLENOID.setGear(true);
        sleep(3000);

        log("Test " + testId++ + ": Setting low gear");
        Robot.TRANSMISSION_SOLENOID.setGear(false);
        sleep(3000);

        log("Finished!");
        log("Finished!");
        log("Finished!");
        log("Finished!");
    }
}
