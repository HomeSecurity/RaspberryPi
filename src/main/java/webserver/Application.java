package webserver;

import Radio.Radio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Tim on 10.06.2017.
 */

@SpringBootApplication
public class Application {

    public static void main(String args[]) {

        //RESET MODEL
        //Alarmsystem.getInstance().resetModel();

        //Start Radio-Module
        try {
            Radio radio = Radio.getInstance();
        } catch(Exception e) {
            e.printStackTrace();
        }

        //Start Web-Server
        SpringApplication.run(Application.class, args);
    }
}
