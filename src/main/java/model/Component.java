package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonas on 29.05.2017.
 */
public abstract class Component implements Serializable{
    public int getId() {
        return id;
    }
    private int id;

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }
    public abstract boolean isSensor();

    private double voltage;

    public Date getCreated() {
        return created;
    }
    private Date created;
    public Component(int id, double voltage){
        created = new Date();
        this.id = id;
        this.voltage = voltage;
    }
    protected Map<Date, Boolean> history = new HashMap<Date, Boolean>();
}
