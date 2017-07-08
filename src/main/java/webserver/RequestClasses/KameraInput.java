package webserver.RequestClasses;

//class to convert a json object easily into a java class
//an original component would be to complex
public class KameraInput extends ComponentInput{
    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
