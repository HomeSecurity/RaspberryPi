package model;

/**
 * Created by Jonas on 29.05.2017.
 */
public class MotionSensor extends Sensor implements Funk868 {
    public MotionSensor(int id) {
        super(id);
    }

    @Override
    public void sendCommand(boolean data) {
        //logic for sending the data to the sensor
    }
}
