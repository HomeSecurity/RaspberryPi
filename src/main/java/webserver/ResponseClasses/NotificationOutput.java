package webserver.ResponseClasses;

import model.Rule;

import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.Date;

public class NotificationOutput {
    private Date date;
    private Rule rule;
    private BufferedImage bufferedImage;

    public NotificationOutput(){}

    public NotificationOutput(Date date, Rule rule, BufferedImage bufferedImage) {
        this.date = date;
        this.rule = rule;
        this.bufferedImage = bufferedImage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }
}
