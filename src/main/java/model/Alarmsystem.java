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
    public static Alarmsystem getInstance() {
        if (Alarmsystem.instance == null){
            Alarmsystem.instance = new Alarmsystem();
            Alarmsystem.instance.init();
        }
        return Alarmsystem.instance;
    }
    private Map<Integer, Component> components = new HashMap<Integer, Component>();
    private Map<Integer, Rule> rules = new HashMap<Integer, Rule>();
    public Gson g = new Gson();
    public boolean removeComponent(int id){
        if (components.containsKey(id)) {
            components.remove(id);
            return true;
        }
        return false;
    }

    public void persist(){
        //clear files
        try {
            new FileOutputStream("Sensors.json").write("".getBytes());
            new FileOutputStream("Actors.json").write("".getBytes());
            new FileOutputStream("Rules.json").write("".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }


        //components
        for(Component comp : components.values()){
            String fileName;
            if(comp.isSensor()) {
                fileName = "Sensors";
            }
            else{
                fileName = "Actors";
            }
            try {
                new FileOutputStream(fileName + ".json", true).write((comp.toJson()+ "\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //rules
        for(Rule rule : rules.values()){
            try {
                new FileOutputStream("Rules.json", true).write((rule.toJson() + "\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void init(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("Actors.json")));
            String line;
        while ((line = bufferedReader.readLine()) != null){
            Aktor actor = g.fromJson(line,Aktor.class);
            components.put(actor.getId(),actor);
        }
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("Sensors.json")));
            while ((line = bufferedReader.readLine()) != null){
                Sensor sensor = g.fromJson(line,Sensor.class);
                components.put(sensor.getId(),sensor);
            }
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("Rules.json")));
            while ((line = bufferedReader.readLine()) != null){

                //doesnt work yet because json contains objects
                //example: {"id":1,"input":{},"output":{"model.Sirene@4dcbadb4":true}}
                Rule rule = g.fromJson(line,Rule.class);
                rules.put(rule.getId(),rule);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //MotionSensor fegit = g.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("fegit.json"))).readLine(),MotionSensor.class);




    }


    public Component getComponentById(int id){
        return components.get(id);
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
                    return false;
        }
        components.put(newComponent.getId(), newComponent);
        //persist
        Alarmsystem.getInstance().persist();
        return true;
    }
    public Rule newRule(){
        Rule rule = new Rule();
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





}
