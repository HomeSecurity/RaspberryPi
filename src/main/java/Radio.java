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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Armin on 26.12.2016.
 */
class Radio {
    List<RadioListener> listeners;

    Radio() throws IOException {
        listeners = new ArrayList<>();
        Serial serial = SerialFactory.createInstance();
        serial.addListener((SerialDataEventListener) event -> {
            try {
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
            int byte1 = (256 + bytes[i]) % 256; //fix negative bytes ffs software serial !
            int byte2 = (256 + bytes[i + 1]) % 256; //fix negative bytes ffs software serial !
            int result = 256 * byte2 + byte1;
            readings[i / 2] = result;
        }

        if (readings[0] == 0) {
            //registration message
            for (RadioListener listener : listeners) {
                listener.onRegistration(readings[1], readings[2], readings[3] / 1000.0);
            }
            return;
        }

        for (RadioListener listener : listeners) {
            listener.onData(readings[0], readings[1] > 0, readings[2] / 1000.0);
        }
    }

    void addListener(RadioListener listener) {
         listeners.add(listener);
    }
}
