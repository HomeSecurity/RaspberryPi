package webserver;

import model.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tim on 10.06.2017.
 */

@RestController
public class WebController {

    @RequestMapping(value = "/rulelist", method = RequestMethod.GET)
    public RuleList rulelist() {
        return new RuleList();
    }

    @RequestMapping(value = "/component", method = RequestMethod.GET)
    public Component component(@RequestParam(value = "id", defaultValue = "1") String id) {
        return new Sensor(Integer.parseInt(id), 0);
    }

}
