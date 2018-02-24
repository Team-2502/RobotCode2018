package logger;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;
import java.util.List;

public class ShuffleboardLog
{

    private static ShuffleboardLog instance;

    public static ShuffleboardLog getInstance()
    {
        return instance == null ? instance = new ShuffleboardLog() : instance;
    }

    private List<String> messages = new ArrayList<>(4);

    private String get(int index)
    {
        try { return messages.get(index); }
        catch(Exception e) { return ""; }
    }

    public void log(String message)
    {
        Log.info(message);
        messages.add(message);
        if(messages.size() > 4) { messages.remove(0); }
        SmartDashboard.putString("systems_message_0", get(0));
        SmartDashboard.putString("systems_message_1", get(1));
        SmartDashboard.putString("systems_message_2", get(2));
        SmartDashboard.putString("systems_message_3", get(3));
    }
}
