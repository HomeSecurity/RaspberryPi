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

        Alarmsystem system = Alarmsystem.getInstance();

        //TODO:RESET ENTFERNEN!!!
        system.resetModel();

        system.activateRegistrationMode();
        system.onRegistrationMessage(1,1000,2.0);
        system.onRegistrationMessage(2,1001,2.0);
        system.onRegistrationMessage(3,2000,2.0);
        system.onRegistrationMessage(4,2001,2.0);

        Component sensor1 = system.getComponentById(1);
        Component sensor2 = system.getComponentById(2);
        Component aktor1 = system.getComponentById(3);
        Component aktor2 = system.getComponentById(4);

        sensor1.setName("sensor1");
        sensor2.setName("sensor2");
        aktor1.setName("aktor1");
        aktor2.setName("aktor2");

        Rule rule1 = system.newRule("rule1", true);
        Rule rule2 = system.newRule("rule2", true);

        system.addComponentToRule(rule1.getId(), sensor1.getId(), true);
        system.addComponentToRule(rule1.getId(), aktor1.getId(), true);
        system.addComponentToRule(rule2.getId(), sensor2.getId(), true);
        system.addComponentToRule(rule2.getId(), aktor2.getId(), true);

        /*try {
            Radio radio = Radio.getInstance();
        } catch(Exception e) {
            e.printStackTrace();
        }*/
        SpringApplication.run(Application.class, args);
    }
}
