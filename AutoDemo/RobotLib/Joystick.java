package RobotLib;

public class Joystick {
    public Joystick(int port) {
        if (port != 0)
            System.out.println("No joystick connected to port " + port);
    }

    public boolean getButtonDown(int idx) {
        if (idx == 0)
            return Data.toInt(Data.robotData[RobotData.button0.ordinal()]) != 0;
        else if (idx == 1)
            return Data.toInt(Data.robotData[RobotData.button1.ordinal()]) != 0;
        else if (idx == 2)
            return Data.toInt(Data.robotData[RobotData.button2.ordinal()]) != 0;
        else if (idx == 3)
            return Data.toInt(Data.robotData[RobotData.button3.ordinal()]) != 0;
        else if (idx == 4)
            return Data.toInt(Data.robotData[RobotData.button4.ordinal()]) != 0;
        else if (idx == 5)
            return Data.toInt(Data.robotData[RobotData.button5.ordinal()]) != 0;
        else {
            System.out.println("Error: no button at index " + idx + "!");
            return false;
        }
    }

    public boolean getButtonPressed(int idx) {
        if (idx == 0)
            return Data.toInt(Data.robotData[RobotData.button0.ordinal()]) == 2;
        else if (idx == 1)
            return Data.toInt(Data.robotData[RobotData.button1.ordinal()]) == 2;
        else if (idx == 2)
            return Data.toInt(Data.robotData[RobotData.button2.ordinal()]) == 2;
        else if (idx == 3)
            return Data.toInt(Data.robotData[RobotData.button3.ordinal()]) == 2;
        else if (idx == 4)
            return Data.toInt(Data.robotData[RobotData.button4.ordinal()]) == 2;
        else if (idx == 5)
            return Data.toInt(Data.robotData[RobotData.button5.ordinal()]) == 2;
        else {
            System.out.println("Error: no button at index " + idx + "!");
            return false;
        }
    }

    public float getAxis(int idx) {
        if (idx == 1) {
            return Data.robotData[RobotData.axis1.ordinal()];
        } else if (idx == 2) {
            return Data.robotData[RobotData.axis2.ordinal()];
        } else if (idx == 3) {
            return Data.robotData[RobotData.axis3.ordinal()];
        } else if (idx == 4) {
            return Data.robotData[RobotData.axis4.ordinal()];
        }
        return 0;
    }

}
