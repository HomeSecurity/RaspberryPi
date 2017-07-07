package webserver.ResponseClasses;

import java.util.Date;

public class NotificationOutput {
    private Date date;
    private int ruleid;
    private byte[] image;
    private boolean triggered;

    public NotificationOutput(){}

    public NotificationOutput(Date date, int ruleid, boolean isTriggered) {
        this.date = date;
        this.ruleid = ruleid;
        this.triggered = isTriggered;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRuleid() {
        return ruleid;
    }

    public void setRuleid(int ruleid) {
        this.ruleid = ruleid;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }
}
