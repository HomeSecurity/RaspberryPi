package model;

import java.io.Serializable;
import java.util.ArrayList;
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

    public Rule(){
        id = Rule.globalId++;
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
    }

    //Weiß nicht, ob das noch benötigt wird, aber ich kommentiere es mal aus, da gson nicht mehr zur Verfügung steht
    /*public String toJson(){
        return Alarmsystem.getInstance().g.toJson(this);
    }*/
}
