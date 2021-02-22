package RobotLib;

public abstract class RobotBase {

    
    /** 
     * @param message
     */
    /**
     * This function is called once when the robot program starts.
     */
    public abstract void start();

    
    /** 
     * @param message
     */
    /**
     * This function is called repeatedly (frequency = RobotSim FPS) when the robot
     * runs.
     */
    public abstract void update();

    /**
     * A shortcut way to print to the console.
     */
    protected void print(Object message) {
        System.out.println(message);
    }
}
