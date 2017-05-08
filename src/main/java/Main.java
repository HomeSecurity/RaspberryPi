import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

import java.io.IOException;
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
        printSystemInformation();
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
