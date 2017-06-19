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
    private String name;
    private double voltage;
    public abstract boolean isSensor();
    protected Map<Date, Boolean> history = new HashMap<Date, Boolean>();
    private int id;

    public int getId() {
        return id;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public Date getCreated() {
        return created;
    }
    private Date created;
    public Component(int id, double voltage){
        created = new Date();
        this.id = id;
        this.voltage = voltage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Gibt nur die Id aus, damit die Map im JSON funktioniert
    @Override
    public String toString() {
        return ""+id;
    }
}
