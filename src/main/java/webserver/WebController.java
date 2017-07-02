package webserver;

import model.*;
import org.springframework.web.bind.annotation.*;
import webserver.RequestClasses.ComponentInput;
import webserver.RequestClasses.Credentials;
import webserver.RequestClasses.RuleInput;
import webserver.ResponseClasses.BooleanOutput;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Map;

/**
 * Created by Tim on 10.06.2017.
 */

@RestController
public class WebController {

    private boolean authorization(HttpSession session, HttpServletResponse response) {
        if(session.getAttribute("loggedIn") == null || !(boolean)session.getAttribute("loggedIn")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    private void persistCredentials(Credentials credentials) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("credentials.ser", false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(credentials);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BooleanOutput login(HttpSession session, HttpServletResponse response, @RequestBody Credentials input) {
        Credentials credentials = null;
        File file = new File("credentials.ser");
        if (file.exists() && !file.isDirectory()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                credentials = (Credentials) objectInputStream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            credentials = new Credentials("DefaultUser", "DefaultPassword");
            persistCredentials(credentials);
        }
        if (input.getUsername().equals(credentials.getUsername()) && input.getPassword().equals(credentials.getPassword())) {
            session.setAttribute("loggedIn", true);
            return new BooleanOutput(true);
        }
        return new BooleanOutput(false);
    }

    @RequestMapping(value = "/changecredentials", method = RequestMethod.POST)
    public Boolean changecredentials(HttpSession session, HttpServletResponse response, @RequestBody Credentials input) {
        if(!authorization(session,response)) {
            return null;
        }
            persistCredentials(input);
        return true;
    }

    @RequestMapping(value = "/rulelist", method = RequestMethod.GET)
    public Map<Integer, Rule> rulelist(HttpSession session, HttpServletResponse response) {
        if(!authorization(session,response)) {
            return null;
        }
        return Alarmsystem.getInstance().getAllRules();
    }

    @RequestMapping(value = "/addrule", method = RequestMethod.POST)
    public Rule addrule(HttpSession session, HttpServletResponse response, @RequestBody RuleInput ruleInput) {
        if(!authorization(session,response)) {
            return null;
        }
        Rule rule = Alarmsystem.getInstance().newRule(ruleInput.getName(), ruleInput.isActive());
        for(int id : ruleInput.getComponents()) {
            Alarmsystem.getInstance().addComponentToRule(rule.getId(), id, true);
        }
        return rule;
    }

    @RequestMapping(value = "/updaterule", method = RequestMethod.PUT)
    public Rule updaterule(HttpSession session, HttpServletResponse response, @RequestBody RuleInput ruleInput) {
        if(!authorization(session,response)) {
            return null;
        }
        Rule rule = Alarmsystem.getInstance().getRulebyId(ruleInput.getId());
        int[] componentId = ruleInput.getComponents();
        int isInArray;
        for(Sensor sensor : rule.getInput().keySet()) {
            isInArray = 0;
            for(int i : componentId) {
                if (sensor.getId() == componentId[i]) {
                    isInArray = i;
                }
            }
            if(isInArray == 0) {
                rule.removeComponent(sensor, true);
            } else {
                int[] temp = new int[componentId.length-1];
                System.arraycopy(componentId, 0, temp, 0, isInArray);
                System.arraycopy(componentId, isInArray+1, temp, isInArray, temp.length-isInArray);
                componentId = temp;
            }
        }
        for(Aktor aktor : rule.getOutput().keySet()) {
            isInArray = 0;
            for(int i : componentId) {
                if(aktor.getId() == componentId[i]) {
                    isInArray = i;
                }
            }
            if(isInArray == 0) {
                rule.removeComponent(aktor, true);
            } else {
                int[] temp = new int[componentId.length-1];
                System.arraycopy(componentId, 0, temp, 0, isInArray);
                System.arraycopy(componentId, isInArray+1, temp, isInArray, temp.length-isInArray);
                componentId = temp;
            }
        }
        for(int id: componentId) {
            rule.addComponent(Alarmsystem.getInstance().getComponentById(id), true);
        }
        return rule;
    }

    @RequestMapping(value = "/deleterule", method = RequestMethod.DELETE)
    public Boolean deleterule(HttpSession session, HttpServletResponse response, @RequestParam(value = "id") int id) {
        if(!authorization(session,response)) {
            return null;
        }
        return Alarmsystem.getInstance().removeRulebyId(id);
    }

    @RequestMapping(value = "/componentlist", method = RequestMethod.GET)
    public Map<Integer, Component> componentlist(HttpSession session, HttpServletResponse response) {
        if(!authorization(session,response)) {
            return null;
        }
        return Alarmsystem.getInstance().getAllComponents();
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public Boolean registration(HttpSession session, HttpServletResponse response) {
        if(!authorization(session,response)) {
            return null;
        }
        Alarmsystem.getInstance().activateRegistrationMode();
        return true;
    }

    @RequestMapping(value = "/updatecomponent", method = RequestMethod.PUT)
    public Component updatecomponent(HttpSession session, HttpServletResponse response, @RequestBody ComponentInput componentInput) {
        if(!authorization(session,response)) {
            return null;
        }
        Component component = Alarmsystem.getInstance().getComponentById(componentInput.getId());
        component.setName(componentInput.getName());
        return component;
    }

    @RequestMapping(value = "/deletecomponent", method = RequestMethod.DELETE)
    public Boolean deletecomponent(HttpSession session, HttpServletResponse response, @RequestParam(value = "id") int id) {
        if(!authorization(session,response)) {
            return null;
        }
        Rule[] rules = Alarmsystem.getInstance().getAllRules().values().toArray(new Rule[0]);
        for(Rule r : rules){
            Alarmsystem.getInstance().removeComponentFromRule(r.getId(), id, true);
        }
        return Alarmsystem.getInstance().removeComponent(id);
    }

    @RequestMapping(value = "/resetall", method = RequestMethod.DELETE)
    public Boolean resetall(HttpSession session, HttpServletResponse response) {
        if(!authorization(session,response)) {
            return null;
        }
        Credentials credentials = new Credentials("DefaultUser", "DefaultPassword");
        persistCredentials(credentials);
        Alarmsystem.getInstance().resetModel();
        return true;
    }

    //TODO: Für die Kamera???
    /*@RequestMapping(value = "/component", method = RequestMethod.GET)
    public Component component(HttpSession session, HttpServletResponse response, @RequestParam(value = "id", defaultValue = "1") String id) {
        if(!authorization(session,response)) {
            return null;
        }
        return new Sensor(Integer.parseInt(id), 0);
    }*/

}
