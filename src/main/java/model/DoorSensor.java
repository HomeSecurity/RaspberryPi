package model;

/**
 * Created by Jonas on 29.05.2017.
 */
public class DoorSensor extends Sensor implements Funk868 {
    @Override
    public void sendCommand(boolean data) {

    }
    public DoorSensor(int id){
        super(id);
    }
}
