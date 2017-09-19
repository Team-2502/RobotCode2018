package logger;

import java.io.PrintStream;

@SuppressWarnings({ "WeakerAccess", "unused", "EmptyCatchBlock", "SameParameterValue" })
public final class Log
{
    private static boolean debug = false;

    private static PrintFormat pf = null;

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

    public static <T> void log(LogType type, T msg, int depth)
    {
        if((type == LogType.DEBUG) && !debug) { return; }
        if(pf == null) { createLogger(); }
        String out = pf.getPrintString(type.toString(), ClassGetter.getCallerClassName(depth), msg);
        if(type.output instanceof LoggerPrintStream) { ((LoggerPrintStream) type.output).outputln(out); }
        else { type.output.println(out); }
        type.output.flush();
    }

    public static <T> void info(T msg) { log(LogType.INFO, msg); }

    public static <T> void warn(T msg) { log(LogType.WARN, msg); }

    public static <T> void error(T msg) { log(LogType.ERROR, msg); }

    public static <T> void trace(T msg) { log(LogType.TRACE, msg); }

    public static <T> void log(LogType type, T msg) { log(type, msg, 0); }

    static <T> void out(T msg) { log(LogType.STD_OUT, msg); }

    static <T> void err(T msg) { log(LogType.STD_ERR, msg); }

    public static void trace(StackTraceElement[] e)
    {
        StringBuilder sb = new StringBuilder();
        for(StackTraceElement el : e) { sb.append("\tat ").append(el.toString()).append('\n'); }
        trace(sb.toString());
    }

    public static <T> void trace(Exception e, T msg)
    {
        trace(msg);
        trace(e.toString());
        trace(e.getStackTrace());
    }

    public static void trace(Exception e) { trace(e, ""); }

    public static <T> void debug(T msg) { log(LogType.DEBUG, msg); }

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
        private ClassGetter() {}

        private static final int BASE_DEPTH = 4;

        public static String getCallerClassName(int depth)
        {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            StackTraceElement element = elements[BASE_DEPTH + depth];
            if(element.getClassName().startsWith("kotlin.io.")) { element = elements[BASE_DEPTH + 2 + depth]; }
            else if(element.getClassName().startsWith("java.lang.Throwable")) { element = elements[BASE_DEPTH + 4 + depth]; }
            return element.getClassName();
        }

        public static String getCallerClassName() { return getCallerClassName(0); }
    }

    static
    {
        System.setOut(new LoggerPrintStream(System.out));
        System.setErr(new LoggerPrintStream(System.err));
    }

    private Log() {}
}