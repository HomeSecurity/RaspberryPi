import com.google.gson.Gson;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;
import model.*;

import java.io.*;
import java.nio.channels.AlreadyBoundException;
import java.util.Arrays;

/**
 * Created by Armin on 26.12.2016.
 */
public class Main implements RadioListener {

    public static void main(String[] args) throws Exception {
        new Main();
        Thread.sleep(1000000);
    }

    private Main() throws IOException, InterruptedException {
        //printSystemInformation();
        Gson g = new Gson();
        MotionSensor sensor = new MotionSensor(4,3.0);
        new FileOutputStream("fegit.json").write(g.toJson(sensor).getBytes());
        MotionSensor fegit = g.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream("fegit.json"))).readLine(),MotionSensor.class);
        System.out.println(fegit);


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



        Radio radio = new Radio();



        radio.addListener(this);
    }

    private void printSystemInformation() throws IOException, InterruptedException {
        System.out.println("Board Type        :  " + SystemInfo.getBoardType().name());
        System.out.println("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
        System.out.println("Java Version      :  " + SystemInfo.getJavaVersion());
        System.out.println("IP Addresses      :  " + Arrays.toString(NetworkInfo.getIPAddresses()));
    }

    @Override
    public void onData(int sensorId, boolean data, double voltage) {
        System.out.println("DATA: " + "sensorId: " + sensorId + " data: " + data + " voltage: " + voltage);
    }

    @Override
    public void onRegistration(int sensorId, int type, double voltage) {
        System.out.println("REGISTRATION: " + "sensorId: " + sensorId + " type: " + type + " voltage: " + voltage);
    }
}
