package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonas on 29.05.2017.
 */
public class Component {
    public int getId() {
        return id;
    }
    private int id;

    public Date getCreated() {
        return created;
    }
    private Date created;
    public Component(int id){
        created = new Date();
        this.id = id;
    }
    protected Map<Date, Boolean> history = new HashMap<Date, Boolean>();
}
