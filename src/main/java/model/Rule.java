package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by D062452 on 02.06.2017.
 */
public class Rule implements Serializable{
    private int id;
    static int globalId = 0;
    private Map<Sensor,Boolean> input = new HashMap<Sensor, Boolean>();
    private Map<Aktor,Boolean> output = new HashMap<Aktor, Boolean>();
    protected ArrayList<Date> history = new ArrayList<Date>();
    private String name;
    private boolean active;

    public Rule(String name, boolean active) {
        id = Rule.globalId++;
        this.name = name;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Map<Sensor, Boolean> getInput() {
        return input;
    }

    public Map<Aktor, Boolean> getOutput() {
        return output;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean addComponent(Component comp, boolean value){
        //return false if component is already part of the rule with same or different boolean value
        if(comp instanceof Aktor){
            if (output.containsKey(comp)) {
                //component is already part of the rule
                return false;
            }
            else{
                output.put((Aktor) comp, value);
                return true;
            }
        }
        else{
            if(input.containsKey(comp)){
                //component is already part of the rule
                return false;
            }
            else{
                input.put((Sensor) comp, value);
                return true;
            }
        }
    }
    
    public boolean isTriggered(){
        boolean alarm = true;
        for (Map.Entry<Sensor, Boolean> entry : input.entrySet()) {
            if(entry.getValue()!= entry.getKey().getState()){
                alarm = false;
                break;
            }
        }
        return alarm;
    }
    public boolean removeComponent(Component comp, boolean value){
        if(comp instanceof Aktor){
           return output.remove(comp, value);
        }
        else{
            return input.remove(comp, value);
        }
    }
    public void executeAlarm(){
        for(Map.Entry<Aktor, Boolean> aktor : output.entrySet()){
            aktor.getKey().activate();
        }
        history.add(new Date());
    }
}
