import Radio.Radio;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;
import model.*;

import java.io.*;
import java.util.Arrays;

/**
 * Created by Armin on 26.12.2016.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        new Main();
        Thread.sleep(1000000);
    }

    private Main() throws IOException, InterruptedException {
        //printSystemInformation();


/*

        Alarmsystem.getInstance().onRegistrationMessage(1,1000,2.0);
        Alarmsystem.getInstance().onRegistrationMessage(2,1001,2.0);
        Alarmsystem.getInstance().onRegistrationMessage(3,2000,2.0);
        Alarmsystem.getInstance().onRegistrationMessage(4,2001,2.0);
        Alarmsystem.getInstance().onRegistrationMessage(5,2001,2.0);
        Alarmsystem.getInstance().onRegistrationMessage(6,1000,2.45);

        Rule r = Alarmsystem.getInstance().newRule();
        r.addComponent(Alarmsystem.getInstance().getComponentById(1), true);
        r.addComponent(Alarmsystem.getInstance().getComponentById(3), true);
        Alarmsystem.getInstance().persist();


        */
        Alarmsystem.getInstance();
        Radio.getInstance();
    }

    private void printSystemInformation() throws IOException, InterruptedException {
        System.out.println("Board Type        :  " + SystemInfo.getBoardType().name());
        System.out.println("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
        System.out.println("Java Version      :  " + SystemInfo.getJavaVersion());
        System.out.println("IP Addresses      :  " + Arrays.toString(NetworkInfo.getIPAddresses()));
    }
}
