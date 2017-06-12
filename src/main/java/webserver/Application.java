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
        Rule r = Alarmsystem.getInstance().newRule();
        r.addComponent(new Sensor(1, 0), true);
        r.addComponent(new Aktor(2, 0), true);
        try {
            Radio radio = new Radio();
        } catch(IOException e) {
            e.printStackTrace();
        }
        SpringApplication.run(Application.class, args);
    }
}
