package model;

import java.util.Date;

/**
 * Created by Jonas on 29.05.2017.
 */
public class Sensor extends Component{
    public Sensor(int id, double voltage) {
        super(id, voltage);
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
        history.put(new Date(), state);
    }

    private boolean state;


}
