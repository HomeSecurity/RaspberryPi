package model;

import java.net.InetAddress;

/**
 * Created by Jonas on 29.05.2017.
 */
public class Kamera extends Aktor {

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    private InetAddress ip;

    public Kamera(int id, double voltage) {
        super(id, voltage);
    }


    @Override
    public void activate(){
        //take picture with this ip adress
        //override activation behavior, because its no 868-communication
    }
    public InetAddress getIp() {
        return null;
    }
}
