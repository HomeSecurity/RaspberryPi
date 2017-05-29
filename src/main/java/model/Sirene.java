package model;

/**
 * Created by Jonas on 29.05.2017.
 */
public class Sirene extends Aktor implements Funk868 {
    public Sirene(int id) {
        super(id);
    }

    @Override
    public void sendCommand(boolean data) {
        //build byte-Data with actor id and data and send it
        if(data) super.activate();
        else super.disable();
    }
}
