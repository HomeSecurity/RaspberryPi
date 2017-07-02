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

/**
 * Created by Jonas on 29.05.2017.
 */
public class Kamera extends Aktor {
    private String ip;
    private ArrayList<BufferedImage> snapshots = new ArrayList<>();
    public BufferedImage getLastSnapShot(){
        if(snapshots.isEmpty()) return null;
        return snapshots.get(snapshots.size()-1);
    }

    public Kamera(int id) {
        super(id, Double.MAX_VALUE);
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public void activate(){
        //take picture with this ip adress
        //override activation behavior, because its no 868-communication
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("admin", "123456".toCharArray());
            }
        });

        try {
            URL url = new URL("http://" + ip + "/mjpeg/snap.cgi?chn=0");
            System.out.println(url);
            URLConnection uc = url.openConnection();
            uc.connect();

            File picture = new File(this.getId() + "_" + new Date().getTime() + ".jpg");
            FileOutputStream fis = new FileOutputStream(picture);
            InputStream is = uc.getInputStream();
            int read = 0;
            while (read != -1) {
                read = is.read();
                fis.write(read);
            }
            snapshots.add(ImageIO.read(picture));
        }
        catch (Exception e){
            System.out.println(e);

        }
    }
    //TODO:InetAdress?
    /*public InetAddress getIp() {
        return null;
    }*/
    public String getIp() {
        return this.ip;
    }
}
