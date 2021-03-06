package model;

import Radio.Radio;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Jonas on 29.05.2017.
 */
public class Aktor extends Component implements Serializable{
    private boolean isActivated;

    public Aktor(int id, double voltage) {
        super(id, voltage);
    }

    @Override
    public boolean isSensor() {
        return false;
    }

    public void activate(Date d){
        send(true);
    }
    public void disable(){
        send(false);
    }

    private void send(boolean data){
        isActivated = data;
        history.put(new Date(), data);
        try {
            Radio.getInstance().sendData(getId(), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
