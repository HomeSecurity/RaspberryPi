package webserver.RequestClasses;

import java.io.Serializable;

/**
 * Created by Tim on 12.06.2017.
 */
public class Credentials implements Serializable{
    private String username;
    private String password;

    public Credentials(){};

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
