package webserver;

import model.*;
import Radio.Radio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Created by Tim on 10.06.2017.
 */

@SpringBootApplication
public class Application {

    public static void main(String args[]) {
        Rule r = Alarmsystem.getInstance().newRule("rule1", true);
        r.addComponent(new Sensor(1, 0), true);
        r.addComponent(new Aktor(2, 0), true);
        Rule r2 = Alarmsystem.getInstance().newRule("rule2", true);
        r2.addComponent(new Sensor(3, 0), true);
        r2.addComponent(new Aktor(4, 0), true);
        /*try {
            Radio radio = Radio.getInstance();
        } catch(Exception e) {
            e.printStackTrace();
        }*/
        SpringApplication.run(Application.class, args);
    }
}
