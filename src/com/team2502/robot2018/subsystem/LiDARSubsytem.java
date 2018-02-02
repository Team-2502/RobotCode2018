package com.team2502.robot2018.subsystem;

import com.team2502.robot2018.utils.RangeScan;
import com.team2502.robot2018.utils.Trilateration;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;

public class LiDARSubsytem extends Subsystem
{
    public SerialPort lidarSensor;

    public LiDARSubsytem()
    {
        lidarSensor = new SerialPort(9600, SerialPort.Port.kOnboard);
        lidarSensor.write(new byte[] { 0x42, 0x57, 0x02, 0x00,
                                       0x00, 0x00, 0x01, 0x06 }, 8);
    }

    @Override
    protected void initDefaultCommand()
    {

    }

    int state = 0;
    byte distHi, distLo, strengthLo;

    void decodeMethod2(byte[] buffer)
    {
        byte[] end = new byte[buffer.length];
        for(byte aBuffer : buffer)
        {
            switch(state)
            {
                default:
                case 0:
                    state = (0x59 == aBuffer) ? 1 : 0;
                    break;
                case 1:
                    state = (0x59 == aBuffer) ? 2 : 0;
                    break;
                case 2:
                    distLo = aBuffer;
                    state++;
                    break;
                case 3:
                    distHi = aBuffer;
                    state++;
                    break;
                case 4:
                    strengthLo = aBuffer;
                    state++;
                    break;
                case 5:
                    state = 0;
                    // get the bytes
                    float distance = 0.001F * ((((short) distHi) << 8) | distLo);
                    short strength = (short) ((((short) aBuffer) << 8) | strengthLo);

                    int x = 10;

                    // If the strength is good, report it
                    // Note: strength may or may not be valid from the sensor
                    if(strength > x && distance > 0.010)
                    {
                        RangeScan.rangeScan_add(distance);
                    }
                    break;
            }
        }
    }


}
