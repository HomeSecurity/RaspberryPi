package model;

import java.net.InetAddress;

/**
 * Created by Jonas on 29.05.2017.
 */
public class Kamera extends Aktor implements Wlan {

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    private InetAddress ip;

    public Kamera(int id) {
        super(id);
    }
    @Override
    public void activate(){
        //take picture with this ip adress
    }

    @Override
    public InetAddress getIp() {
        return null;
    }
}
