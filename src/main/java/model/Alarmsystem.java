package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonas on 29.05.2017.
 */
public class Alarmsystem {
    private static Alarmsystem instance;
    private Alarmsystem(){}
    public static Alarmsystem getInstance() {
        if (Alarmsystem.instance == null){
            Alarmsystem.instance = new Alarmsystem();
        }
        return Alarmsystem.instance;
    }
    private Map<Integer, Component> components = new HashMap<Integer, Component>();
    private Map<Integer, Rule> rules = new HashMap<Integer, Rule>();
    public boolean removeComponent(int id){
        if (components.containsKey(id)) {
            components.remove(id);
            return true;
        }
        return false;
    }
    public Component getComponentById(int id){
        return components.get(id);
    }

    public boolean onRegistrationMessage(String data){
        //perform parsing the byte message
        // not sure if message is String or byte array
        //if invalid message return false

        int id = 5;
        int type = 0;
        double voltage = 3.0;
        //1000 Tür
        //1001 Bewegungssensor
        //2000 Sirene
        //2001 Steckdose
        Component newComponent;
        switch(type){
            case 1000:
                newComponent = new DoorSensor(id, voltage);
                break;
            case 1001:
                newComponent = new MotionSensor(id, voltage);
                break;
            case 2000:
                newComponent = new Sirene(id, voltage);
                break;
            case 2001:
                newComponent = new Steckdose(id, voltage);
                break;
                default:
                    return false;
        }
        components.put(newComponent.getId(), newComponent);
        return true;
    }
    public void newRule(){
        Rule rule = new Rule();
        rules.put(rule.getId(), rule);
    }
    public boolean addComponentToRule(int ruleId, int componendId, boolean value){
        Component comp = Alarmsystem.getInstance().getComponentById(componendId);
        Rule rule = Alarmsystem.getInstance().getRulebyId(ruleId);
        return rule.addComponent(comp,value);
    }
    public boolean removeComponentFromRule(int ruleId, int componentId, boolean value){
        Component comp = Alarmsystem.getInstance().getComponentById(componentId);
        Rule rule = Alarmsystem.getInstance().getRulebyId(ruleId);
        return rule.removeComponent(comp, value);
    }

    public Rule getRulebyId(int id){
        return rules.get(id);
    }





}
