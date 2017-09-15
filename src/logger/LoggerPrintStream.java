package logger;

import java.io.PrintStream;

@SuppressWarnings({ "WeakerAccess" })
public class LoggerPrintStream extends PrintStream
{
    protected final boolean isOutputStream;
    private transient int depth;

    public LoggerPrintStream(PrintStream original)
    {
        super(original);
        this.isOutputStream = original.equals(System.out);
        depth = 0;
    }

    public void outputln( String s) { super.println(s); }

    @Override
    public void println( String msg)

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
}