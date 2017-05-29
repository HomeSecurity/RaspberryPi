package model;

import java.util.Date;

/**
 * Created by Jonas on 29.05.2017.
 */
public class Aktor extends Component{
    private boolean isActivated;

    public Aktor(int id) {
        super(id);
    }

    public void activate(){
        isActivated = true;
        history.put(new Date(), true);
    }
    public void disable(){
        isActivated = false;
        history.put(new Date(), false);
    }
}
