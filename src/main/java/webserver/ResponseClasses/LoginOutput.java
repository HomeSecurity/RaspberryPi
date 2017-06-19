package webserver.ResponseClasses;

public class LoginOutput {

    private final boolean output;

    public LoginOutput(boolean output){
        this.output = output;
    }

    public boolean getOutput() {
        return output;
    }
}
