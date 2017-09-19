import java.io.*;

public class Version
{
    public static void main(String[] args) throws Exception
    {
        if((args.length < 2) || (args[0] == null) || args[0].isEmpty())
        {
            throw new Exception("Needs file and version type as parameters.");
        }

        File versionFile = new File(args[0]);
        if(!versionFile.exists())
        {
            if(!versionFile.createNewFile())
            {
                throw new IOException("Cannot create version file.");
            }
        }

        BufferedReader br = new BufferedReader(new FileReader(versionFile));
        int major = 0, minor = 0, build = 0;

        String line;
        while((line = br.readLine()) != null)
        {
            if(line.startsWith("version="))
            {
                String[] split = line.split("=");
                if(split.length < 2) { throw new IOException("Invalid file data."); }
                String[] digits = split[1].split("\\.");
                if(digits.length != 3) { throw new IOException("Invalid file data."); }
                major = Integer.valueOf(digits[0]);
                minor = Integer.valueOf(digits[1]);
                build = Integer.valueOf(digits[2]);
                break;
            }
        }
        br.close();

        switch(Integer.valueOf(args[1]))
        {
            case 0:
                ++build;
                break;
            case 1:
                ++minor;
                build = 0;
                break;
            case 2:
                ++major;
                minor = 0;
                build = 0;
                break;
            default:
                throw new IllegalArgumentException("Version type must be `0` for build, `1` for minor and `2` for major.");
        }

        if(!versionFile.delete())
        {
            throw new Exception("Cannot delete version file.");
        }
        if(!versionFile.createNewFile())
        {
            throw new Exception("Cannot create version file.");
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(versionFile));
        bw.write(new StringBuilder(17).append("version=").append(major).append('.').append(minor).append('.').append(build).toString());
        bw.newLine();
        bw.close();
    }
}
