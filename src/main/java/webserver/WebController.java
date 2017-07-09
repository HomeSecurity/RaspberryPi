package webserver;

import model.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import webserver.RequestClasses.*;
import webserver.ResponseClasses.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * Created by Tim on 10.06.2017.
 */

@RestController
public class WebController {

    //check if logged in
    private boolean authorization(HttpSession session, HttpServletResponse response) {
        if(session.getAttribute("loggedIn") == null || !(boolean)session.getAttribute("loggedIn")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    //save the login-credentials into a file
    private void persistCredentials(Credentials credentials) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("credentials.ser", false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(credentials);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //http method to login
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BooleanOutput login(HttpSession session, HttpServletResponse response, @RequestBody Credentials input) {
        Credentials credentials = null;

        //the server credentials are read from a serialized file
        File file = new File("credentials.ser");
        if (file.exists() && !file.isDirectory()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                credentials = (Credentials) objectInputStream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }

        //if the file doesn't exists it gets created with default credentials
        } else {
            credentials = new Credentials("DefaultUser", "DefaultPassword");
            persistCredentials(credentials);
        }

        //the send credentials are compared with the saved ones
        if (input.getUsername().equals(credentials.getUsername()) && input.getPassword().equals(credentials.getPassword())) {
            session.setAttribute("loggedIn", true);
            return new BooleanOutput(true);
        }
        return new BooleanOutput(false);
    }

    //change the credentials to new ones
    @RequestMapping(value = "/changecredentials", method = RequestMethod.POST)
    public BooleanOutput changecredentials(HttpSession session, HttpServletResponse response, @RequestBody Credentials input) {
        if(!authorization(session,response)) {
            return null;
        }
            persistCredentials(input);
        return new BooleanOutput(true);
    }

    //get all rules
    @RequestMapping(value = "/rulelist", method = RequestMethod.GET)
    public Map<Integer, Rule> rulelist(HttpSession session, HttpServletResponse response) {
        if(!authorization(session,response)) {
            return null;
        }
        return Alarmsystem.getInstance().getAllRules();
    }

    //create a single rule
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

    //change the data for one rule
    @RequestMapping(value = "/updaterule", method = RequestMethod.PUT)
    public Rule updaterule(HttpSession session, HttpServletResponse response, @RequestBody RuleInput ruleInput) {
        if(!authorization(session,response)) {
            return null;
        }
        Rule rule = Alarmsystem.getInstance().getRulebyId(ruleInput.getId());
        int[] componentIds = ruleInput.getComponents();
        int isInArray;

        //components are saved with two maps in a rule
        //to find out which components were changed, each map has to be looped through
        for(Sensor sensor : rule.getInput().keySet()) {
            isInArray = 0;
            for(int i = 0; i<componentIds.length; i++) {

                //check if the sensor in the current rule is still present in the updated one
                //if this is true the index of the updated rule is saved
                if (sensor.getId() == componentIds[i]) {
                    isInArray = i;
                }
            }
            if(isInArray == 0) {

                //the sensor in the current rule was not found in the updated one and gets therefore deleted
                Alarmsystem.getInstance().removeComponentFromRule(rule.getId(), sensor.getId(), true);
            } else {

                //the sensor was found in the current rule
                //it gets deleted from the array of the updated rule
                int[] temp = new int[componentIds.length-1];
                System.arraycopy(componentIds, 0, temp, 0, isInArray);
                System.arraycopy(componentIds, isInArray+1, temp, isInArray, temp.length-isInArray);
                componentIds = temp;
            }
        }

        //the aktors are now checked in the same way as the sensors before
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

        //because the component array in the updated rule does only consist out of components
        //which were not found in the original rule they have to be added to the rule
        for(int id: componentIds) {
            Alarmsystem.getInstance().addComponentToRule(rule.getId(), id, true);
        }
        rule.setName(ruleInput.getName());
        rule.setActive(ruleInput.isActive());
        return rule;
    }

    //delete a rule by its id
    @RequestMapping(value = "/deleterule", method = RequestMethod.DELETE)
    public BooleanOutput deleterule(HttpSession session, HttpServletResponse response, @RequestParam(value = "id") int id) {
        if(!authorization(session,response)) {
            return null;
        }
        return new BooleanOutput(Alarmsystem.getInstance().removeRulebyId(id));
    }

    //get all components currently registered in the system
    @RequestMapping(value = "/componentlist", method = RequestMethod.GET)
    public Map<Integer, Component> componentlist(HttpSession session, HttpServletResponse response) {
        if(!authorization(session,response)) {
            return null;
        }
        return Alarmsystem.getInstance().getAllComponents();
    }

    //start the registration mode for 60 seconds
    //in this time new components can be registered via the radio-signals
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public BooleanOutput registration(HttpSession session, HttpServletResponse response) {
        if(!authorization(session,response)) {
            return null;
        }
        Alarmsystem.getInstance().activateRegistrationMode();
        return new BooleanOutput(true);
    }

    //change the additional information to a component
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

    //delete a single component
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

    //reset all rules and components
    //also all counters are set to zero and the credentials are set to the default ones
    @RequestMapping(value = "/resetall", method = RequestMethod.DELETE)
    public BooleanOutput resetall(HttpSession session, HttpServletResponse response) {
        if(!authorization(session,response)) {
            return null;
        }
        Credentials credentials = new Credentials("DefaultUser", "DefaultPassword");
        persistCredentials(credentials);
        Alarmsystem.getInstance().resetModel();
        return new BooleanOutput(true);
    }

    //add the component "kamera" to the system
    //because an ip-camera is connected via wifi the communication is not done with the radio-protocol
    //therefore the ip-address has to be given to the system
    @RequestMapping(value = "/addcamera", method = RequestMethod.POST)
    public Component addcamera(HttpSession session, HttpServletResponse response, @RequestBody KameraInput kameraInput) {
        if(!authorization(session,response)) {
            return null;
        }
        Component kamera = Alarmsystem.getInstance().registerIpCamera(kameraInput.getId(), kameraInput.getIp());
        return updatecomponent(session, response, kameraInput);
    }

    //the update method for the special component "kamera"
    //first the special attribute "ip" is changed and then the standard update method for a component is called
    @RequestMapping(value = "/updatecamera", method = RequestMethod.PUT)
    public Component updatecamera(HttpSession session, HttpServletResponse response, @RequestBody KameraInput kameraInput) {
        if (!authorization(session, response)) {
            return null;
        }
        Kamera kamera = (Kamera) Alarmsystem.getInstance().getComponentById(kameraInput.getId());
        kamera.setIp(kameraInput.getIp());
        return updatecomponent(session, response, kameraInput);
    }

    //get all notifiacations
    @RequestMapping(value = "/notificationlist", method = RequestMethod.GET)
    public Map<Integer, NotificationOutput> notificationlist(HttpSession session, HttpServletResponse response) {
        if (!authorization(session, response)) {
            return null;
        }
        HashMap<Date, Rule> notifications = Alarmsystem.getInstance().getNotifications();
        Map<Integer, NotificationOutput> output = new HashMap<>();
        int i = 0;

        //because images are not directly mapped to a notification the mapping has to be done here
        //for each notification is checked if an image was shot at the exact same time
        //if an image was shot it is saved to the notification
        for(Map.Entry<Date, Rule> entry : notifications.entrySet()) {
            output.put(i, new NotificationOutput(entry.getKey(), entry.getValue().getId(), entry.getValue().isTriggered()));
            BufferedImage bufferedImage = entry.getValue().getPictureForDate(entry.getKey());
            if (bufferedImage != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    ImageIO.write(bufferedImage, "jpg", baos);
                } catch(IOException e) {
                    e.printStackTrace();
                }
                byte[] bytes = baos.toByteArray();
                output.get(i).setImage(bytes);
            }
            i++;
        }
        return output;
    }

    //reset the alarm if a rule was triggered before
    @RequestMapping(value = "/resetalarm", method = RequestMethod.GET)
    public BooleanOutput resetalarm(HttpSession session, HttpServletResponse response) {
        if (!authorization(session, response)) {
            return null;
        }
        Alarmsystem.getInstance().resetAlarm();
        return new BooleanOutput(true);
    }

    //set the device token for the google message service
    //therefore messages are now send to the entered device
    @RequestMapping(value = "/settoken", method = RequestMethod.POST)
    public BooleanOutput settoken(HttpSession session, HttpServletResponse response, @RequestBody Token token) {
        if (!authorization(session, response)) {
            return null;
        }
        Alarmsystem.getInstance().setToken(token.getToken());
        return new BooleanOutput(true);
    }
}
