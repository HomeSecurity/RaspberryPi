package model;

import java.util.Date;

/**
 * Created by Jonas on 29.05.2017.
 */
public class Aktor extends Component{
    private boolean isActivated;

    public Aktor(int id, double voltage) {
        super(id, voltage);
    }

    public void activate(){
        send(true);
    }
    public void disable(){
        send(false);
    }
    private void send(boolean data){
        isActivated = data;
        history.put(new Date(), data);
        //implement sending here @Armin
    }
}
