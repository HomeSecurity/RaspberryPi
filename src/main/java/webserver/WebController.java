package webserver;

import model.*;
import org.springframework.web.bind.annotation.*;
import webserver.RequestClasses.LoginInput;
import webserver.RequestClasses.RuleInput;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public boolean login(HttpSession session, HttpServletResponse response, @RequestBody LoginInput input) {
        if (input.getUsername().equals("Test") && input.getPassword().equals("1234")) {
            session.setAttribute("loggedIn", true);
            return true;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
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
        Rule rule = Alarmsystem.getInstance().newRule(ruleInput.getName(),ruleInput.isActive());
        for(int id : ruleInput.getComponents()) {
            rule.addComponent(Alarmsystem.getInstance().getComponentById(id), true);
        }
        return rule;
    }

    //update Rule

    //delete Rule

    @RequestMapping(value = "/componentlist", method = RequestMethod.GET)
    public Map<Integer, Component> componentlist(HttpSession session, HttpServletResponse response) {
        if(!authorization(session,response)) {
            return null;
        }
        return Alarmsystem.getInstance().getAllComponents();
    }

    //create component

    //update component

    //delete component

    /*@RequestMapping(value = "/component", method = RequestMethod.GET)
    public Component component(HttpSession session, HttpServletResponse response, @RequestParam(value = "id", defaultValue = "1") String id) {
        if(!authorization(session,response)) {
            return null;
        }
        return new Sensor(Integer.parseInt(id), 0);
    }*/

}
