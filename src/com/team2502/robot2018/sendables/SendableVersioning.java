package com.team2502.robot2018.sendables;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Send branch name, commit hash, deployer to Shuffleboard
 */
public class SendableVersioning implements Sendable
{

    /**
     * Singleton because there is only 1 code version
     */
    public static final SendableVersioning INSTANCE = new SendableVersioning();

    private String name;
    private String branch;
    private String commit;
    private String version;
    private String time;
    private String blame;

    /**
     * Singleton.
     *
     * @see SendableVersioning#INSTANCE
     */
    private SendableVersioning()
    {
        this.name = "Versioning";
        this.branch = "unknown";
        this.commit = "unknown";
        this.version = "unknown";
        this.time = "unknown";
        this.blame = "unknown";
    }

    /**
     * Stick versioning in the Jar manifest file
     */
    public void init()
    {
        Class thisClass = SendableVersioning.class;
        String className = thisClass.getSimpleName() + ".class";
        String classPath = thisClass.getResource(className).toString();
        if(!classPath.startsWith("jar")) { return; }
        String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) + "/META-INF/MANIFEST.MF";
        Manifest manifest;
        try
        {
            manifest = new Manifest(new URL(manifestPath).openStream());
            Attributes attr = manifest.getMainAttributes();
            branch = attr.getValue("branch");
            commit = attr.getValue("commit");
            version = attr.getValue("version");
            time = attr.getValue("time");
            blame = attr.getValue("blame");
        }
        catch(Exception ignored) { }
    }

    /**
     * Put it on the shuffleboard
     *
     * @param builder Something WPILib provides
     */
    @Override
    public void initSendable(SendableBuilder builder)
    {
        builder.addStringProperty("Branch: ", () -> branch, null);
        builder.addStringProperty("Commit: ", () -> commit, null);
        builder.addStringProperty("Version: ", () -> version, null);
        builder.addStringProperty("Time: ", () -> time, null);
        builder.addStringProperty("Blame: ", () -> blame, null);
    }

    @Override
    public String getSubsystem()
    { return name; }

    @Override
    public void setSubsystem(String subsystem)
    { this.name = subsystem; }

    @Override
    public String getName()
    { return name; }

    @Override
    public void setName(String name)
    { this.name = name; }
}
