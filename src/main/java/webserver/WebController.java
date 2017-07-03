package webserver;

import model.*;
import org.springframework.web.bind.annotation.*;
import webserver.RequestClasses.*;
import webserver.ResponseClasses.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
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
        int[] componentIds = ruleInput.getComponents();
        int isInArray;
        for(Sensor sensor : rule.getInput().keySet()) {
            isInArray = 0;
            for(int i = 0; i<componentIds.length; i++) {
                if (sensor.getId() == componentIds[i]) {
                    isInArray = i;
                }
            }
            if(isInArray == 0) {
                Alarmsystem.getInstance().removeComponentFromRule(rule.getId(), sensor.getId(), true);
            } else {
                int[] temp = new int[componentIds.length-1];
                System.arraycopy(componentIds, 0, temp, 0, isInArray);
                System.arraycopy(componentIds, isInArray+1, temp, isInArray, temp.length-isInArray);
                componentIds = temp;
            }
        }
        for(Aktor aktor : rule.getOutput().keySet()) {
            isInArray = 0;
            for(int i = 0; i<componentIds.length; i++) {
                if (aktor.getId() == componentIds[i]) {
                    isInArray = i;
                }
            }
            if(isInArray == 0) {
                Alarmsystem.getInstance().removeComponentFromRule(rule.getId(), aktor.getId(), true);
            } else {
                int[] temp = new int[componentIds.length-1];
                System.arraycopy(componentIds, 0, temp, 0, isInArray);
                System.arraycopy(componentIds, isInArray+1, temp, isInArray, temp.length-isInArray);
                componentIds = temp;
            }
        }
        for(int id: componentIds) {
            Alarmsystem.getInstance().addComponentToRule(rule.getId(), id, true);
        }
        rule.setName(ruleInput.getName());
        rule.setActive(ruleInput.isActive());
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

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
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
        component.setDescription(componentInput.getDescription());
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

    @RequestMapping(value = "/addcamera", method = RequestMethod.POST)
    public Component addcamera(HttpSession session, HttpServletResponse response, @RequestBody KameraInput kameraInput) {
        if(!authorization(session,response)) {
            return null;
        }
        Component kamera = Alarmsystem.getInstance().registerIpCamera(kameraInput.getId(), kameraInput.getIp());
        return updatecomponent(session, response, kameraInput);
    }

    @RequestMapping(value = "/updatecamera", method = RequestMethod.PUT)
    public Component updatecamera(HttpSession session, HttpServletResponse response, @RequestBody KameraInput kameraInput) {
        if (!authorization(session, response)) {
            return null;
        }
        Kamera kamera = (Kamera) Alarmsystem.getInstance().getComponentById(kameraInput.getId());
        kamera.setIp(kameraInput.getIp());
        return updatecomponent(session, response, kameraInput);
    }

    @RequestMapping(value = "/notificationlist", method = RequestMethod.GET)
    public NotificationOutput[] notificationlist(HttpSession session, HttpServletResponse response) {
        if (!authorization(session, response)) {
            return null;
        }
        HashMap<Date, Rule> notifications = Alarmsystem.getInstance().getNotifications();
        NotificationOutput[] notificationOutputs = new NotificationOutput[notifications.size()];
        int i = 0;
        for(Map.Entry<Date, Rule> entry : notifications.entrySet()) {
            notificationOutputs[i] = new NotificationOutput(entry.getKey(), entry.getValue(), null);
            BufferedImage bufferedImage = entry.getValue().getPictureForDate(entry.getKey());
            if (bufferedImage != null) {
                notificationOutputs[i].setBufferedImage(bufferedImage);
            }
            i++;
        }
        return notificationOutputs;
    }

    @RequestMapping(value = "/resetalarm", method = RequestMethod.GET)
    public Boolean resetalarm(HttpSession session, HttpServletResponse response) {
        if (!authorization(session, response)) {
            return null;
        }
        Alarmsystem.getInstance().resetAlarm();
        return true;
    }

    @RequestMapping(value = "/settoken", method = RequestMethod.POST)
    public Boolean settoken(HttpSession session, HttpServletResponse response, @RequestBody String token) {
        if (!authorization(session, response)) {
            return null;
        }
        Alarmsystem.getInstance().setToken(token);
        return true;
    }
}
