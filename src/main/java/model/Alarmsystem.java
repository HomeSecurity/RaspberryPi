package model;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jonas on 29.05.2017.
 */
public class Alarmsystem {
    private static Alarmsystem instance;
    private Alarmsystem(){}
    private Map<Integer, Component> components = new HashMap<Integer, Component>();
    private Map<Integer, Rule> rules = new HashMap<Integer, Rule>();
    private boolean registrationMode = false;



    public static Alarmsystem getInstance() {
        if (Alarmsystem.instance == null){
            Alarmsystem.instance = new Alarmsystem();
            Alarmsystem.instance.init();
        }
        return Alarmsystem.instance;
    }

    public void persist(){
        try{
            FileOutputStream fout = new FileOutputStream("components.ser", false);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(components);
            fout = new FileOutputStream("rules.ser", false);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(rules);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void init(){
        ObjectInputStream objectinputstream = null;
        try {
            FileInputStream streamIn = new FileInputStream("components.ser");
            objectinputstream = new ObjectInputStream(streamIn);
            components = (Map<Integer, Component>) objectinputstream.readObject();

            streamIn = new FileInputStream("rules.ser");
            objectinputstream = new ObjectInputStream(streamIn);
            rules = (Map<Integer, Rule>) objectinputstream.readObject();

            //adjust global rule Id for further rules
            for (Rule rule : rules.values()){
                if(Rule.globalId < rule.getId()){
                    Rule.globalId = rule.getId() + 1;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDataMessage(int sensorId, boolean data, double voltage) {
        Component comp = Alarmsystem.getInstance().getComponentById(sensorId);
        comp.setVoltage(voltage);
        if(comp.isSensor()) {
            Sensor sensor = (Sensor) comp;
            sensor.setState(data);
        }

        //persist
        Alarmsystem.getInstance().persist();
        //check all Rules
        Alarmsystem.getInstance().checkRules();
    }
    public ArrayList<Rule> checkRules(){
        ArrayList<Rule> triggered = new ArrayList<>();
        for (Rule rule : rules.values()) {
            if(rule.isTriggered()) {
                triggered.add(rule);
            }
        }
        return triggered;
    }

    public void resetAlarm(){
        for(Component comp : components.values()){
            if(!comp.isSensor()){
                Aktor aktor = (Aktor)comp;
                aktor.disable();
            }
            else{
                //not sure if we should reset the sensor values after disabling the alarm?
                //todo
                Sensor sensor = (Sensor)comp;
                sensor.setState(false);
            }
        }
        //persist
        Alarmsystem.getInstance().persist();
    }

    public boolean onRegistrationMessage(int id, int type, double voltage){
        if(!registrationMode){
            return false;
        }


        //perform parsing the byte message
        // not sure if message is String or byte array
        //if invalid message return false

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
                    newComponent = new DoorSensor(id, voltage);
        }
        components.put(newComponent.getId(), newComponent);
        //persist
        Alarmsystem.getInstance().persist();
        return true;
    }
    public Rule newRule(String name, boolean active){
        Rule rule = new Rule(name, active);
        rules.put(rule.getId(), rule);
        //persist
        Alarmsystem.getInstance().persist();
        return rule;
    }
    public boolean addComponentToRule(int ruleId, int componendId, boolean value){
        Component comp = Alarmsystem.getInstance().getComponentById(componendId);
        Rule rule = Alarmsystem.getInstance().getRulebyId(ruleId);
        if(rule.addComponent(comp,value)){
            //persist
            Alarmsystem.getInstance().persist();
            return true;
        }
        return false;
    }
    public boolean removeComponentFromRule(int ruleId, int componentId, boolean value){
        Component comp = Alarmsystem.getInstance().getComponentById(componentId);
        Rule rule = Alarmsystem.getInstance().getRulebyId(ruleId);

        if(rule.removeComponent(comp, value)){
            //persist
            Alarmsystem.getInstance().persist();
            return true;
        }
        return false;
    }

    public Rule getRulebyId(int id){
        return rules.get(id);
    }

    public boolean removeRulebyId(int id) {
        rules.remove(id);
        return true;
    }

    public Map<Integer, Rule> getAllRules(){
        return rules;
    }
    public Map<Integer, Component> getAllComponents(){
        return components;
    }

    public Component getComponentById(int id){
        return components.get(id);
    }

    public boolean removeComponent(int id){
        if (components.containsKey(id)) {
            components.remove(id);
            return true;
        }
        return false;
    }

    public void activateRegistrationMode(){
        Alarmsystem.getInstance().registrationMode = true;
        new Thread(() -> {

            try {
                //sleep for 60sec
                Thread.sleep(60000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
            finally {
                Alarmsystem.getInstance().registrationMode = false;
            }
        }).start();
    }
    public void resetModel(){
        components = new HashMap<Integer, Component>();
        rules = new HashMap<Integer, Rule>();
        persist();
    }





}
