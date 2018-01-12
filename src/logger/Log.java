package logger;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Timer;

@SuppressWarnings({ "WeakerAccess", "unused", "EmptyCatchBlock", "SameParameterValue" })
public final class Log
{
    private static final Timer TIMER;
    private static boolean debug = false;
    private static PrintFormat pf = null;

    static
    {
        LoggerPrintStream ops = new LoggerPrintStream(System.out);
        LoggerPrintStream eps = new LoggerPrintStream(System.err);
        System.setOut(ops);
        System.setErr(eps);
        TIMER = new Timer();
        TIMER.scheduleAtFixedRate(new LoggerPrintStream.PrintTimer(ops), 1000, 1000);
        TIMER.scheduleAtFixedRate(new LoggerPrintStream.PrintTimer(eps), 1000, 1000);
    }

    private Log() {}

    public static boolean isDebugMode() { return debug; }

    public static void setDebugMode(boolean debug)
    {
        boolean tmp = Log.debug;
        Log.debug = debug;
        if(tmp != debug) { Log.debug(new StringBuilder(28).append("Logger ").append(tmp ? "dis" : "en").append("abling debug mode.").toString()); }
    }

    public static void createLogger() { createLogger(false, "[timestamp] [type] [caller_class]: msg", "hh:mm:ss"); }

    public static void createLogger(boolean debug) { createLogger(debug, "[timestamp] [type] [caller_class]: msg", "hh:mm:ss"); }

    public static void createLogger(boolean debug, String format, String timeFormat)
    {
        pf = new PrintFormat(format, timeFormat);
        setDebugMode(debug || (System.getProperty("debug") != null));
    }

    public static <T> void log(LogType type, T msg, int depth, Object... args)
    {
        if((type == LogType.DEBUG) && !debug) { return; }
        if(pf == null) { createLogger(); }
        String out = pf.getPrintString(type.toString(), ClassGetter.getCallerClassName(depth), MessageFormat.format(msg.toString(), args));
        if(type.output instanceof LoggerPrintStream) { ((LoggerPrintStream) type.output).outputln(out); }
        else { type.output.println(out); }
        type.output.flush();
    }

    public static <T> void log(LogType type, T msg, int depth)
    { log(type, msg, depth, new Object[0]); }

    public static <T> void info(T msg, Object... args) { log(LogType.INFO, msg, args); }

    public static <T> void warn(T msg, Object... args) { log(LogType.WARN, msg, args); }

    public static <T> void error(T msg, Object... args) { log(LogType.ERROR, msg, args); }

    public static <T> void trace(T msg, Object... args) { log(LogType.TRACE, msg, args); }

    public static <T> void log(LogType type, T msg, Object... args) { log(type, msg, 0, args); }

    static <T> void out(T msg, Object... args) { log(LogType.STD_OUT, msg, args); }

    static <T> void err(T msg, Object... args) { log(LogType.STD_ERR, msg, args); }

    public static void trace(StackTraceElement[] e)
    {
        StringBuilder sb = new StringBuilder();
        for(StackTraceElement el : e) { sb.append("\tat ").append(el.toString()).append('\n'); }
        trace(sb.toString());
    }

    public static <T> void trace(Exception e, T msg, Object... args)
    {
        trace(msg, args);
        trace(e.toString());
        trace(e.getStackTrace());
    }

    public static void trace(Exception e) { trace(e, ""); }

    public static <T> void debug(T msg, Object... args) { log(LogType.DEBUG, msg, args); }

    public enum LogType
    {
        STD_OUT(System.out),
        STD_ERR(System.err),
        INFO(System.out),
        WARN(System.out),
        ERROR(System.err),
        TRACE(System.err),
        DEBUG(System.out);

        public final PrintStream output;

        LogType(PrintStream output) { this.output = output; }
    }

    @SuppressWarnings("WeakerAccess")
    public static final class ClassGetter
    {
        private static final int BASE_DEPTH = 5;

        private ClassGetter() {}

        public static String getCallerClassName(int depth)
        {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            StackTraceElement element = elements[BASE_DEPTH + depth];
            if(element.getClassName().startsWith("kotlin.io.")) { element = elements[BASE_DEPTH + 2 + depth]; }
            else if(element.getClassName().startsWith("java.lang.Throwable")) { element = elements[BASE_DEPTH + 4 + depth]; }
            return new StringBuilder().append(element.getClassName()).append('#').append(element.getMethodName()).append(':').append(element.getLineNumber()).toString();
        }

        public static String getCallerClassName() { return getCallerClassName(1); }
    }
}