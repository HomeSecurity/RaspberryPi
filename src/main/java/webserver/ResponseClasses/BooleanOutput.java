package webserver.ResponseClasses;

//class to convert a json object easily into a java class
//therefore a boolean value is wrapped into a json-object when returning the method with spring boot
public class BooleanOutput {

    private final boolean output;

    public BooleanOutput(boolean output){
        this.output = output;
    }

    public boolean getOutput() {
        return output;
    }
}
