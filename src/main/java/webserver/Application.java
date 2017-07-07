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

        //RESET MODEL
        //Alarmsystem.getInstance().resetModel();


        try {
            Radio radio = Radio.getInstance();
        } catch(Exception e) {
            e.printStackTrace();
        }
        SpringApplication.run(Application.class, args);
    }
}
