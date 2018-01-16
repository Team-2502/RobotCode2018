package logger;

import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TimerTask;

@SuppressWarnings({ "WeakerAccess" })
public class LoggerPrintStream extends PrintStream
{
    protected final boolean isOutputStream;
    private final Queue<String> printBuffer;
    private transient int depth;
    private int length;

    public LoggerPrintStream(PrintStream original)
    {
        super(original);
        this.isOutputStream = original.equals(System.out);
        depth = 0;
        printBuffer = new PriorityQueue<>();
    }

    public synchronized void outputln(String s)
    {
        super.println(s);
    }

    public synchronized void printBuffer()
    {
//        super.println("Printing Buffer:");
        synchronized(printBuffer)
        {
            StringBuilder sb = new StringBuilder(length);
            length = 0;
            while(!printBuffer.isEmpty()) { sb.append(printBuffer.poll()).append('\n'); }

            if(sb.length() != 0) { super.print(sb); }
        }
    }

    @Override
    public void println(String msg)
    {
        if(isOutputStream) { Log.log(Log.LogType.STD_OUT, msg, 1 + depth); }
        else { Log.log(Log.LogType.STD_ERR, msg, 1 + depth); }
        depth = 0;
    }

    @Override
    public void println(Object msg)
    {
        ++depth;
        println(msg.toString());
    }

    public static class PrintTimer extends TimerTask
    {
        private LoggerPrintStream lps;

        public PrintTimer(LoggerPrintStream lps)
        {
            this.lps = lps;
        }

        @Override
        public void run()
        { lps.printBuffer(); }
    }
}