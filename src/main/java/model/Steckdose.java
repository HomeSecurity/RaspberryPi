package model;

/**
 * Created by Jonas on 29.05.2017.
 */
public class Steckdose extends Aktor implements Funk868 {
    @Override
    public void sendCommand(boolean data) {
        //build byte-Data with actor id and data and send it
        if(data) super.activate();
        else super.disable();
    }
    public Steckdose(int id){
        super(id);
    }
}
