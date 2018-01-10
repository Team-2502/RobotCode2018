package logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({ "WeakerAccess", "StringBufferReplaceableByString" })
public class PrintFormat
{
    private static final Pattern TIMESTAMP = Pattern.compile("timestamp", Pattern.LITERAL);
    private static final Pattern TYPE = Pattern.compile("type", Pattern.LITERAL);
    private static final Pattern CALLER_CLASS = Pattern.compile("caller_class", Pattern.LITERAL);
    private static final Pattern MSG = Pattern.compile("msg", Pattern.LITERAL);

    private String format;
    private int[] timeOrganizer;
    private String[] formats;
    private Timer timer;

    public PrintFormat(String format, String timeFormat)
    {
        this.format = format;
        this.timeOrganizer = new int[] { 0, 0, 0, 0, 0 };
        for(String spl : timeFormat.split(":"))
        {
            if(spl.contains("M")) { timeOrganizer[0] = spl.length(); }
            else if(spl.contains("s")) { timeOrganizer[1] = spl.length(); }
            else if(spl.contains("m")) { timeOrganizer[2] = spl.length(); }
            else if(spl.contains("h")) { timeOrganizer[3] = spl.length(); }
            else if(spl.contains("d")) { timeOrganizer[4] = spl.length(); }
        }
        formats = new String[5];
        formats[0] = new StringBuilder("%0").append(timeOrganizer[0]).append('d').toString();
        formats[1] = new StringBuilder("%0").append(timeOrganizer[1]).append('d').toString();
        formats[2] = new StringBuilder("%0").append(timeOrganizer[2]).append('d').toString();
        formats[3] = new StringBuilder("%0").append(timeOrganizer[3]).append('d').toString();
        formats[4] = new StringBuilder("%0").append(timeOrganizer[4]).append('d').toString();
        timer = new Timer();
    }

    public <T> String getPrintString(String type, String caller, T msg) { return MSG.matcher(CALLER_CLASS.matcher(TYPE.matcher(TIMESTAMP.matcher(format).replaceAll(Matcher.quoteReplacement(timer.getTime()))).replaceAll(Matcher.quoteReplacement(type))).replaceAll(Matcher.quoteReplacement(caller))).replaceAll(Matcher.quoteReplacement(String.valueOf((Object) msg))); }

    protected class Timer
    {
        protected long[] times;
        protected long[] lastTimes;
        protected String[] stringTimes;
        protected final long startTime;

        private Timer()
        {
            times = new long[5];
            lastTimes = new long[] { -1, -1, -1, -1, -1 };
            stringTimes = new String[5];
            for(int i = 0; i < stringTimes.length; ++i) { stringTimes[i] = "00"; }
            startTime = System.currentTimeMillis();
        }

        protected String formatTime()
        {
            StringBuilder out = new StringBuilder();
            for(int i = timeOrganizer.length - 1; i >= 0; --i)
            {
                if(timeOrganizer[i] == 0) { continue; }
                if(times[i] == lastTimes[i]) { out.append(stringTimes[i]); }
                else { out.append(stringTimes[i] = String.format(formats[i], times[i])); }
                out.append(':');
            }
            return out.toString().substring(0, out.length() - 1);
        }

        protected String getTime()
        {
            System.arraycopy(times, 0, lastTimes, 0, times.length);
            long time = System.currentTimeMillis() - startTime;
            times[0] = time % 1000;
            time /= 1000;
            times[1] = time % 60;
            time /= 60;
            times[2] = time % 60;
            time /= 60;
            times[3] = time % 24;
            times[4] = time / 24;
            return formatTime();
        }
    }
}