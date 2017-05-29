package model;

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
        //1000 Tür
        //1001 Bewegungssensor
        //2000 Sirene
        //2001 Steckdose
        Component newComponent;
        switch(type){
            case 1000:
                newComponent = new DoorSensor(id);
                break;
            case 1001:
                newComponent = new MotionSensor(id);
                break;
            case 2000:
                newComponent = new Sirene(id);
                break;
            case 2001:
                newComponent = new Steckdose(id);
                break;
                default:
                    return false;
        }
        components.put(newComponent.getId(), newComponent);
        return true;
    }






}
