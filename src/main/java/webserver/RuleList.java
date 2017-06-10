package webserver;

import model.*;

/**
 * Created by Tim on 10.06.2017.
 */
public class RuleList {

    private final Rule[] rules;

    public RuleList() {

        Component dummyAktor1 = new Aktor(1,0);
        Component dummyAktor2 = new Aktor(2,0);
        Component dummySensor1 = new Sensor(3,0);
        Component dummySensor2 = new Sensor(4,0);

        Rule dummyRule1 = new Rule();
        Rule dummyRule2 = new Rule();

        dummyRule1.addComponent(dummyAktor1, false);
        dummyRule1.addComponent(dummySensor1, false);

        dummyRule2.addComponent(dummyAktor2, false);
        dummyRule2.addComponent(dummySensor2, false);

        this.rules = new Rule[]{ dummyRule1, dummyRule2 };
    }

    public Rule[] getRules() {
        return rules;
    }
}
