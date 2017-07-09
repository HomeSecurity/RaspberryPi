package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.*;
import java.nio.Buffer;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Jonas on 29.05.2017.
 */
public class Kamera extends Aktor {
    private String ip;
    private HashMap<Date, BufferedImage> snapshots = new HashMap<Date, BufferedImage>();

    public BufferedImage getSnapShotForTimeStamp(Date d){
        if(snapshots.isEmpty()) return null;
        return snapshots.get(d);
    }

    public Kamera(int id) {
        super(id, Double.MAX_VALUE);
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public void activate(Date d){
        //take picture with this ip adress
        //override activation behavior, because its no 868-communication
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("admin", "123456".toCharArray());
            }
        });

        try {
            //snapshot url for wansview ip cameras
            URL url = new URL("http://" + ip + "/mjpeg/snap.cgi?chn=0");
            System.out.println(url);
            URLConnection uc = url.openConnection();
            uc.connect();

            File picture = new File(this.getId() + "_" + d.getTime() + ".jpg");
            FileOutputStream fis = new FileOutputStream(picture);
            InputStream is = uc.getInputStream();
            int read = 0;
            while (read != -1) {
                read = is.read();
                fis.write(read);
            }
            snapshots.put(d, ImageIO.read(picture));
        }
        catch (Exception e){
            System.out.println(e);

        }
    }

    public String getIp() {
        return this.ip;
    }
}
