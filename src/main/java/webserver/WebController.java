package webserver;

import model.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * Created by Tim on 10.06.2017.
 */

@RestController
public class WebController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public boolean login(HttpSession httpSession, @RequestBody LoginInput input) {
        if (input.getUsername().equals("Test") && input.getPassword().equals("1234")) {
            httpSession.setAttribute("loggedIn", true);
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/rulelist", method = RequestMethod.GET)
    public ArrayList<Rule> rulelist(HttpSession httpSession, HttpServletResponse response) {
        if(httpSession.getAttribute("loggedIn") == null || !(boolean)httpSession.getAttribute("loggedIn")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        return Alarmsystem.getInstance().getAllRules();
    }

    @RequestMapping(value = "/component", method = RequestMethod.GET)
    public Component component(@RequestParam(value = "id", defaultValue = "1") String id, HttpSession httpSession, HttpServletResponse response) {
        if(httpSession.getAttribute("loggedIn") == null || !(boolean)httpSession.getAttribute("loggedIn")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        return new Sensor(Integer.parseInt(id), 0);
    }

}
