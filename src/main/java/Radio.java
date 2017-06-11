import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.RaspberryPiSerial;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.StopBits;

import java.io.IOException;
import java.nio.charset.Charset;

import model.Alarmsystem;

import static java.lang.Thread.sleep;

/**
 * Created by Armin on 26.12.2016.
 */
class Radio {
    Radio() throws IOException {
        Serial serial = SerialFactory.createInstance();
        serial.addListener((SerialDataEventListener) event -> {
            try {
                if (Charset.forName("US-ASCII").newEncoder().canEncode(event.getAsciiString())) {
                    //debug / log
                    System.out.println(event.getAsciiString());
                }

                onRead(event.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        SerialConfig config = new SerialConfig();
        config.device(RaspberryPiSerial.AMA0_COM_PORT)
                .baud(Baud._9600)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);

        System.out.println("Connecting to: " + config);
        serial.open(config);


        new Thread(() -> {
            while (true) {
                try {
                    sleep(5000);
                    //data format:
                    //first char 0-5: sensorId (filled with leading 0) e.g. id:124 -> 00124
                    //char 6: data (1 or 0)
                    //char 7: 'a' = indicate the message is over
                    String sensorId = "" + (int) (Math.random() * 65000);
                    //sensorId = "31558";
                    while (sensorId.length() < 5) {
                        sensorId = "0" + sensorId;
                    }
                    boolean data = Math.random() > 0.5;
                    System.out.println("SENDING TO AKTOR: id=" + sensorId + " data:" + data);
                    serial.write(sensorId.toCharArray());
                    serial.write(data ? '1' : '0');
                    serial.write('a');
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //todo filter readings from same sensor in very short time
    private void onRead(byte[] bytes) {
        int len = bytes.length;
        if (len > 8 || len < 6 || len % 2 != 0) {
            //for sure not our package (or invalid)
            return;
        }

        //parse (verified! tested every 2byte int value)
        int[] readings = new int[len / 2];
        for (int i = 0; i < len; i += 2) {
            int byte1 = (256 + bytes[i]) % 256; //fix unsigned (negative)
            int byte2 = (256 + bytes[i + 1]) % 256; //fix unsigned (negative)
            int result = 256 * byte2 + byte1;
            readings[i / 2] = result;
        }

        if (readings[0] == 0) {
            //registration message
            int sensorId = readings[1];
            int type = readings[2];
            double voltage = readings[3] / 1000.0;
            System.out.println("SENSOR REGISTRATION: " + "sensorId: " + sensorId + " type: " + type + " voltage: " + voltage);

            Alarmsystem.getInstance().onRegistrationMessage(sensorId, type, voltage);
            return;
        }

        //data message
        int sensorId = readings[0];
        boolean data = readings[1] > 0;
        double voltage = readings[2] / 1000.0;
        System.out.println("SENSOR DATA: " + "sensorId: " + sensorId + " data: " + data + " voltage: " + voltage);
        Alarmsystem.getInstance().onDataMessage(sensorId, data, voltage);
    }
}
