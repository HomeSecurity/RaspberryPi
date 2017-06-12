package model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Jonas on 29.05.2017.
 */
public class Sensor extends Component implements Serializable{
    private boolean state;

    public Sensor(int id, double voltage) {
        super(id, voltage);
    }

    @Override
    public boolean isSensor() {
        return true;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
        history.put(new Date(), state);
    }
}
