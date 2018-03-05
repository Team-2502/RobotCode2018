package logger;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;
import java.util.List;

public class ShuffleboardLog
{
    private static List<String> messages = new ArrayList<>(4);

    public static ShuffleboardLog getInstance()
    {
        return null;
    }

    private static String get(int index)
    {
        try { return messages.get(index); }
        catch(Exception e) { return ""; }
    }

    public static void log(String message)
    {
//        Log.info(message);
        messages.add(message);
        if(messages.size() > 4) { messages.remove(0); }
        SmartDashboard.putString("systems_message_0", get(0));
        SmartDashboard.putString("systems_message_1", get(1));
        SmartDashboard.putString("systems_message_2", get(2));
        SmartDashboard.putString("systems_message_3", get(3));
    }
}
